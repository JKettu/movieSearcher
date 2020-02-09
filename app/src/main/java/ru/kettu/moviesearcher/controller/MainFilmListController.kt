package ru.kettu.moviesearcher.controller

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.MainFilmListFragment
import ru.kettu.moviesearcher.adapter.FilmListAdapter
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.FilmListResponse
import ru.kettu.moviesearcher.network.RetrofitApp


fun MainFilmListFragment.initRecycleView(currentView: View) {
    val layoutManager =
        GridLayoutManager( currentView?.context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (position == 0) resources.getInteger(R.integer.columns) else 1 //set header width
        }
    }
    initFilmLoading(this, currentView, layoutManager)
}

fun initFilmLoading(fragment: MainFilmListFragment, currentView: View?, layoutManager: LayoutManager) {
    val resources = fragment.resources
    val movieDbApi = RetrofitApp.theMovieDbApi
    val call = movieDbApi?.getNowPlayingFilms(resources.configuration.locale.language)
    call?.enqueue(object : Callback<FilmListResponse> {
        override fun onResponse(call: Call<FilmListResponse>, response: Response<FilmListResponse>) {
            val films: List<FilmDetails>? = response.body()?.results
            films?.forEach {
                fragment.filmItems.add(FilmItem(it.title, it.overview, it.posterPath))
            }
            films?.let {
                fragment.recycleView?.adapter = FilmListAdapter(LayoutInflater.from(currentView?.context),
                    films, fragment.listener, resources)
                fragment.recycleView?.layoutManager = layoutManager
            }
            fragment.listener?.onItemsInitFinish(fragment.filmItems)
            fragment.listener?.onFragmentCreatedInitToolbar(fragment)
            fragment.circle_progress_bar.visibility = View.INVISIBLE
        }

        override fun onFailure(call: Call<FilmListResponse>, t: Throwable) {
            System.out.println(t.stackTrace)
        }
    })
}