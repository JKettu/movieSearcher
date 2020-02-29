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
import ru.kettu.moviesearcher.models.enum.LoadResult
import ru.kettu.moviesearcher.models.enum.LoadResult.FAILED
import ru.kettu.moviesearcher.models.enum.LoadResult.SUCCESS
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.LoaderResponse
import ru.kettu.moviesearcher.network.FilmSearchApp
import ru.kettu.moviesearcher.network.interactor.Loader
import ru.kettu.moviesearcher.network.interactor.TheMovieDbLoader

class FavouritesViewModel: ViewModel() {
    private val notInFavouriteLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val favouritesLoadedLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val favInitLoadResultLiveData = MutableLiveData<LoadResult>()
    private val notInFavInitLoadResultLiveData = MutableLiveData<LoadResult>()

    private val theMovieDb = FilmSearchApp.theMovieDbApi

    val notInFavourite: LiveData<LinkedHashSet<FilmItem>>
        get() = notInFavouriteLiveData

    val favouritesLoaded: LiveData<LinkedHashSet<FilmItem>>
        get() = favouritesLoadedLiveData

    val favInitLoadResult: LiveData<LoadResult>
        get() = favInitLoadResultLiveData

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
                    favouritesLoadedLiveData.value?.let { loadedFavs ->
                        if (loadedFavs.isEmpty())
                            favInitLoadResultLiveData.postValue(FAILED)
                    }
                }

                override fun onSucceed(item: LinkedHashSet<FilmItem>) {
                    val currentItem = item.elementAt(0)
                    favouritesLoadedLiveData.value?.add(currentItem)
                    favouritesLoadedLiveData.postValue(favouritesLoadedLiveData.value)
                    notInFavouriteLiveData.value?.let { notInFavs ->
                        notInFavs.removeAll(item)
                        notInFavouriteLiveData.postValue(notInFavouriteLiveData.value)
                    }
                    progressBar?.visibility = INVISIBLE
                    favInitLoadResultLiveData.postValue(SUCCESS)
                }
            })
        }
    }

    fun onNotInFavouritesScroll(resources: Resources, context: Context, currentLoadedPage: Int, progressBar: ProgressBar) {
        val call = theMovieDb?.getNowPlayingFilms(resources.configuration.locale.language, currentLoadedPage)
        call?.let {
            TheMovieDbLoader.loadFilmSet(call as Call<LoaderResponse>, object: Loader.LoaderCallback {
                override fun onFailed(errorIntId: Int) {
                    Toast.makeText(context, errorIntId, Toast.LENGTH_LONG).show()
                    progressBar.visibility = INVISIBLE
                    if (currentLoadedPage == 1)
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
                    progressBar.visibility = INVISIBLE
                    notInFavInitLoadResultLiveData.postValue(SUCCESS)
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
}