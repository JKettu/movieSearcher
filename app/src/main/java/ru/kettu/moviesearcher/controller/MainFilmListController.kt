package ru.kettu.moviesearcher.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.FilmListResponse
import ru.kettu.moviesearcher.network.RetrofitApp
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment
import ru.kettu.moviesearcher.view.recyclerview.adapter.FilmListAdapter


fun MainFilmListFragment.initRecycleView() {
    circle_progress_bar.visibility = View.VISIBLE
    val layoutManager =
        GridLayoutManager(this.view?.context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (position == 0) resources.getInteger(R.integer.columns) else 1 //set header width
        }
    }
    initFirstFilmLoading(this, layoutManager)
}

fun initFirstFilmLoading(fragment: MainFilmListFragment, layoutManager: LayoutManager) {
    if (fragment.filmItems.isEmpty()) {
        val resources = fragment.resources
        val movieDbApi = RetrofitApp.theMovieDbApi
        val call = movieDbApi?.getNowPlayingFilms(resources.configuration.locale.language)
        loadFilmList(fragment, call, layoutManager)
    } else {
        fragment.recycleView?.adapter =
            FilmListAdapter(LayoutInflater.from(fragment.view?.context), fragment.filmItems, fragment.listener, fragment.resources)
        fragment.recycleView?.layoutManager = layoutManager
        fragment.circle_progress_bar.visibility = INVISIBLE
    }
}

fun getAllFilmsList(fragment: MainFilmListFragment, page: Int, layoutManager: LayoutManager?) {
    if (fragment.currentLoadedPage == page) return
    val call = getFilmsFromPage(fragment.resources, page)
    loadFilmList(fragment, call, layoutManager)
}

private fun loadFilmList(fragment: MainFilmListFragment, call: Call<FilmListResponse>?, layoutManager: LayoutManager?) {
    val currentView = fragment.view
    val resources = fragment.resources
    call?.enqueue(object : Callback<FilmListResponse> {
        override fun onResponse(call: Call<FilmListResponse>, response: Response<FilmListResponse>) {
            try {
                fragment.recycleView?.let {
                    val films: List<FilmDetails>? = response.body()?.results
                    films?.forEach {
                        it.genres = fillGenres(it.genres, it.genreIds)
                        fragment.filmItems.add(FilmItem(it.id, it.title, it.overview, it.posterPath,
                            it.voteAverage.toString(), it.genres, it.releaseDate))
                    }

                    films?.let {
                        if (fragment.recycleView?.adapter == null) {
                            fragment.recycleView?.adapter =
                                FilmListAdapter(LayoutInflater.from(currentView?.context), fragment.filmItems, fragment.listener, resources)
                            fragment.recycleView?.layoutManager = layoutManager
                        } else {
                            fragment.recycleView?.adapter?.let {
                                it.notifyItemRangeInserted(it.itemCount + 1, films.size)
                                fragment.currentLoadedPage = if (response.body()?.page == null) 0 else response.body()?.page as Int
                            }
                        }
                    }

                    fragment.listener?.onItemsInitFinish(fragment.filmItems)
                    fragment.circle_progress_bar.visibility = INVISIBLE
                }
            } catch (exception: Throwable) {
                Log.e("Main:loadFilmList", exception.localizedMessage, exception)
            }
        }

        override fun onFailure(call: Call<FilmListResponse>, t: Throwable) {
            try {
                fragment.circle_progress_bar.visibility = INVISIBLE
                Toast.makeText(
                    fragment.view?.context,
                    R.string.filmLoadingFailed,
                    Toast.LENGTH_SHORT
                ).show()
            }
            catch (exception: Throwable) {
                Log.e("Main:loadFilmList", exception.localizedMessage, exception)
            }
        }
    })
}