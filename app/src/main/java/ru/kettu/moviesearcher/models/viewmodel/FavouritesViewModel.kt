package ru.kettu.moviesearcher.models.viewmodel

import android.content.Context
import android.content.res.Resources
import android.view.View.INVISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import ru.kettu.moviesearcher.models.enums.LoadResult
import ru.kettu.moviesearcher.models.enums.LoadResult.FAILED
import ru.kettu.moviesearcher.models.enums.LoadResult.SUCCESS
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.LoaderResponse
import ru.kettu.moviesearcher.FilmSearchApp
import ru.kettu.moviesearcher.network.interactor.Loader
import ru.kettu.moviesearcher.network.interactor.TheMovieDbLoader

class FavouritesViewModel: ViewModel() {
    private val favouritesLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val notInFavouriteLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val favouritesLoadedLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val notInFavInitLoadResultLiveData = MutableLiveData<LoadResult>()

    private val theMovieDb = FilmSearchApp.theMovieDbApi

    val favourites: LiveData<LinkedHashSet<FilmItem>>
        get() = favouritesLiveData

    val notInFavourite: LiveData<LinkedHashSet<FilmItem>>
        get() = notInFavouriteLiveData

    val favouritesLoaded: LiveData<LinkedHashSet<FilmItem>>
        get() = favouritesLoadedLiveData

    val notInFavInitLoadResult: LiveData<LoadResult>
        get() = notInFavInitLoadResultLiveData

    fun initFavouritesLoadedLiveData(favourites: LinkedHashSet<FilmItem>) {
        if (favouritesLoadedLiveData.value == null) {
            favouritesLoadedLiveData.value = LinkedHashSet()
        }
        (favouritesLoadedLiveData.value as LinkedHashSet).addAll(favourites)
        favouritesLoadedLiveData.postValue(favouritesLoadedLiveData.value)
    }

    fun initNotInFavouritesLiveData() {
        notInFavInitLoadResultLiveData.value = SUCCESS
        if (notInFavouriteLiveData.value == null) {
            notInFavouriteLiveData.value = LinkedHashSet()
        }
    }

    fun loadFavouritesList(favourites: LinkedHashSet<FilmItem>, resources: Resources, context: Context, progressBar: ProgressBar) {
        favourites.forEach {item ->
            filmLoad(item, resources, context, progressBar)
        }
    }

    fun filmLoad(item: FilmItem, resources: Resources, context: Context, progressBar: ProgressBar?) {
        val call = theMovieDb?.getFilmDetails(item.id, resources.configuration.locale.language)
        call?.let { currentCall ->
            TheMovieDbLoader.loadFilm(currentCall as Call<LoaderResponse>, object: Loader.LoaderCallback {
                override fun onFailed(errorIntId: Int) {
                    Toast.makeText(context, errorIntId, Toast.LENGTH_LONG).show()
                    progressBar?.visibility = INVISIBLE
                }

                override fun onSucceed(item: LinkedHashSet<FilmItem>) {
                    val currentItem = item.elementAt(0)
                    favouritesLoadedLiveData.value?.add(currentItem)
                    favouritesLoadedLiveData.postValue(favouritesLoadedLiveData.value)
                    notInFavouriteLiveData.value?.let { notInFavs ->
                        notInFavs.removeAll(item)
                        notInFavouriteLiveData.postValue(notInFavouriteLiveData.value)
                    }
                }
            })
        }
    }

    fun onNotInFavouritesScroll(resources: Resources, context: Context, currentLoadedPage: Int) {
        val call = theMovieDb?.getNowPlayingFilms(resources.configuration.locale.language, currentLoadedPage)
        call?.let {
            TheMovieDbLoader.loadFilmSet(call as Call<LoaderResponse>, object: Loader.LoaderCallback {
                override fun onFailed(errorIntId: Int) {
                    Toast.makeText(context, errorIntId, Toast.LENGTH_LONG).show()
                    notInFavInitLoadResultLiveData.postValue(FAILED)
                }

                override fun onSucceed(item: LinkedHashSet<FilmItem>) {
                    val newList = LinkedHashSet<FilmItem>()
                    item.forEach { x ->
                        favouritesLoadedLiveData.value?.let {
                            if (!it.contains(x))
                                newList.add(x)
                        }
                    }
                    (notInFavouriteLiveData.value as LinkedHashSet<FilmItem>).addAll(newList)
                    notInFavouriteLiveData.postValue(notInFavouriteLiveData.value)
                    notInFavInitLoadResultLiveData.value = SUCCESS
                }
            })
        }
    }

    fun onAddToFavourites(resources: Resources, context: Context, film: FilmItem) {
        filmLoad(film, resources, context, null)
    }

    fun onRemoveFromFavourites(film: FilmItem) {
        notInFavouriteLiveData.value?.add(film)
        notInFavouriteLiveData.postValue(notInFavouriteLiveData.value)
        favouritesLoadedLiveData.value?.remove(film)
        favouritesLoadedLiveData.postValue(favouritesLoadedLiveData.value)
    }

    fun onFilmItemLongPress(item: FilmItem, isDeleted: Boolean) {
        if (favouritesLiveData.value == null) {
            favouritesLiveData.value = LinkedHashSet()
        }
        if (isDeleted) {
            favouritesLiveData.value?.let {
                it.remove(item)
            }
        } else {
            favouritesLiveData.value?.let {
                it.add(item)
            }
        }
        favouritesLiveData.postValue(favouritesLiveData.value)
    }
}