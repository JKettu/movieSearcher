package ru.kettu.moviesearcher.controller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.Constants.FAVOURITES_LOAD_AMOUNT
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.FilmListResponse
import ru.kettu.moviesearcher.network.RetrofitApp
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment
import ru.kettu.moviesearcher.view.recyclerview.adapter.AddToFavouritesAdapter
import ru.kettu.moviesearcher.view.recyclerview.adapter.FavouritesAdapter

fun addToFavourites(fragment: FavouritesFragment, film: FilmItem) {
    val resources = fragment.resources
    val movieDbApi = RetrofitApp.theMovieDbApi
    fragment.favourites.add(film)
    val call = movieDbApi?.getFilmDetails(fragment.favourites.toList().last().id, resources.configuration.locale.language)
    loadFilm(fragment, call)

    val elemPosition = fragment.notInFavourites.indexOf(film)
    fragment.notInFavourites.remove(film)
    fragment.filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
}

fun deleteFromFavourites(fragment: FavouritesFragment, film: FilmItem, layoutPosition: Int) {
    fragment.favourites.remove(film)
    fragment.favouritesLoaded.remove(film)
    fragment.notInFavourites.add(film)
    fragment.recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
    fragment.filmsToAddRV.adapter?.let {
        it.notifyItemInserted(it.itemCount)
    }
}

fun initFirstFavouritesLoading(fragment: FavouritesFragment, favourites: Set<FilmItem>) {
    initFavouritesRecyclerView(fragment, fragment.view?.context)
    if (fragment.favourites.isEmpty()
            || fragment.favourites.size == fragment.favouritesLoaded.size
            || fragment.favouritesLoaded.size >= FAVOURITES_LOAD_AMOUNT) {
        fragment.circle_progress_bar.visibility = INVISIBLE
        return
    }
    favouritesLoading(fragment, favourites)
}

private fun favouritesLoading(fragment: FavouritesFragment, favourites: Set<FilmItem>) {
    val resources = fragment.resources
    val movieDbApi = RetrofitApp.theMovieDbApi
    val loadedFavouritesAmount = fragment.favouritesLoaded.size
    val rangeEnd =  if (favourites.size > loadedFavouritesAmount + FAVOURITES_LOAD_AMOUNT)
        loadedFavouritesAmount + FAVOURITES_LOAD_AMOUNT
    else favourites.size

    //Favourites will be loaded by 10 movies at once
    for (i in loadedFavouritesAmount until rangeEnd) {
        val call = movieDbApi?.getFilmDetails(favourites.toList()[i].id, resources.configuration.locale.language)
        loadFilm(fragment, call)
    }
}

private fun initFavouritesRecyclerView(fragment: FavouritesFragment, context: Context?) {
    val layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val itemDecorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    itemDecorator.setDrawable(fragment.resources.getDrawable(R.drawable.separator_line))
    fragment.recycleViewFav?.addItemDecoration(itemDecorator)
    fragment.recycleViewFav?.adapter =
        FavouritesAdapter(LayoutInflater.from(context), fragment.favouritesLoaded, fragment.listener)
    fragment.recycleViewFav?.layoutManager = layoutManager

    fragment.recycleViewFav?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    == recyclerView.size - 1
                && fragment.favourites.size != recyclerView.size) {
                favouritesLoading(fragment, fragment.favourites)
            }
        }
    })
}

fun initFirstNotInFavouritesLoading(fragment: FavouritesFragment) {
    initNotInFavouritesRecyclerView(fragment, fragment.view?.context)
    if (fragment.notInFavourites.isNotEmpty()) {
        fragment.circle_progress_bar.visibility = INVISIBLE
        return
    }
    getNotInFavouritesList(fragment, fragment.currentLoadedPage)
}

fun getNotInFavouritesList(fragment: FavouritesFragment, page: Int) {
    val call = getFilmsFromPage(fragment.resources, page)
    loadFilmList(fragment, call)
}

private fun initNotInFavouritesRecyclerView(fragment: FavouritesFragment, context: Context?) {
    val layoutManager =
        GridLayoutManager( context, fragment.resources.getInteger(R.integer.columns),
            RecyclerView.VERTICAL, false)
    fragment.filmsToAddRV?.adapter =
        AddToFavouritesAdapter(LayoutInflater.from(context), fragment.notInFavourites, fragment.listener)
    fragment.filmsToAddRV?.layoutManager = layoutManager
}

private fun loadFilm(fragment: FavouritesFragment, call: Call<FilmDetails>?) {
    call?.enqueue(object: Callback<FilmDetails> {
        override fun onFailure(call: Call<FilmDetails>, t: Throwable) {
            fragment.circle_progress_bar.visibility = INVISIBLE
            Log.e("Favourites:loadFilm",t.localizedMessage, t)
        }

        override fun onResponse(call: Call<FilmDetails>, response: Response<FilmDetails>) {
            response.body()?.let { film ->
                val newFav = FilmItem(film.id, film.title, film.overview, film.posterPath)
                fragment.favouritesLoaded.add(newFav)
                fragment.recycleViewFav.adapter?.let {
                    it.notifyItemInserted(it.itemCount)
                }
            }
            fragment.circle_progress_bar.visibility = INVISIBLE
        }
    })
}

private fun loadFilmList(fragment: FavouritesFragment, call: Call<FilmListResponse>?) {
    call?.enqueue(object: Callback<FilmListResponse> {
        override fun onFailure(call: Call<FilmListResponse>, t: Throwable) {
            fragment.circle_progress_bar.visibility = INVISIBLE
            Log.e("Favourites:loadFilmList",t.localizedMessage, t)
        }

        override fun onResponse(call: Call<FilmListResponse>, response: Response<FilmListResponse>) {
            val films: List<FilmDetails>? = response.body()?.results
            var itemsAddedAmount = 0
            films?.forEach {
                val currentFilm = FilmItem(it.id, it.title, it.overview, it.posterPath)
                if (!fragment.favourites.contains(currentFilm)) {
                    fragment.notInFavourites.add(currentFilm)
                    fragment.filmsToAddRV?.adapter?.notifyItemInserted(fragment.notInFavourites.size - 1)
                    itemsAddedAmount++
                }
            }
            fragment.currentLoadedPage++
            if (itemsAddedAmount < FAVOURITES_LOAD_AMOUNT) {
                getNotInFavouritesList(fragment, fragment.currentLoadedPage)
            }
            fragment.circle_progress_bar.visibility = INVISIBLE
        }
    })
}