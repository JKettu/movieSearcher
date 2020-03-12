package ru.kettu.moviesearcher.models.viewmodel

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.Constants
import ru.kettu.moviesearcher.models.enums.LoadResult
import ru.kettu.moviesearcher.models.enums.LoadResult.FAILED
import ru.kettu.moviesearcher.models.enums.LoadResult.SUCCESS
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.Genres
import ru.kettu.moviesearcher.models.network.LoaderResponse
import ru.kettu.moviesearcher.FilmSearchApp
import ru.kettu.moviesearcher.network.interactor.Loader
import ru.kettu.moviesearcher.network.interactor.TheMovieDbLoader

class FilmDetailsViewModel: ViewModel() {
    private val filmLiveData = MutableLiveData<FilmItem>()
    private val detailsLoadResultLiveData = MutableLiveData<LoadResult>()
    private val theMovieDb = FilmSearchApp.theMovieDbApi

    val film: LiveData<FilmItem>
        get() = filmLiveData

    val  detailsLoadResult: LiveData<LoadResult>
        get() = detailsLoadResultLiveData

    fun loadFilm(resources: Resources, filmId: Int, context: Context) {
        val call = theMovieDb?.getFilmDetails(filmId, resources.configuration.locale.language)
        TheMovieDbLoader.loadFilm(call as Call<LoaderResponse>, object: Loader.LoaderCallback {
            override fun onFailed(errorIntId: Int) {
                Toast.makeText(context, errorIntId, Toast.LENGTH_LONG).show()
                detailsLoadResultLiveData.postValue(FAILED)
            }

            override fun onSucceed(item: LinkedHashSet<FilmItem>) {
                filmLiveData.postValue(item.elementAt(0))
                detailsLoadResultLiveData.postValue(SUCCESS)
            }
        })
    }

    fun convertGenresListToString (resources: Resources, list: List<Genres>): String {
        var genres = resources.getString(R.string.genres)
        list.forEachIndexed { index, elem ->
            genres += elem.name
            if (!list.lastIndex.equals(index))
                genres += Constants.COMMA_AND_SPACE
        }
        return genres
    }

}