package ru.kettu.moviesearcher.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
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

private fun initFirstFilmLoading(fragment: MainFilmListFragment, layoutManager: LayoutManager) {
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
            val films: List<FilmDetails>? = response.body()?.results
            films?.forEach {
                fragment.filmItems.add(FilmItem(it.id, it.title, it.overview, it.posterPath))
            }

            films?.let {
                if (response.body()?.page == 1) {
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
            fragment.listener?.onFragmentCreatedInitToolbar(fragment)
            fragment.circle_progress_bar.visibility = INVISIBLE
        }

        override fun onFailure(call: Call<FilmListResponse>, t: Throwable) {
            Log.e("Main:loadFilmList",t.localizedMessage, t)
            fragment.circle_progress_bar.visibility = INVISIBLE
        }
    })
}