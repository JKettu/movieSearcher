package ru.kettu.moviesearcher.network.interactor

import retrofit2.Call
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.LoaderResponse

interface Loader {

    fun loadFilm(call: Call<LoaderResponse>, callback: LoaderCallback)

    fun loadFilmSet(call: Call<LoaderResponse>, callback: LoaderCallback)

    interface LoaderCallback {
        fun onFailed(errorIntId: Int)

        fun onSucceed(item: LinkedHashSet<FilmItem>)
    }
}