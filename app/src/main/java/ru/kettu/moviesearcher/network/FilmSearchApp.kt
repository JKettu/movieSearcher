package ru.kettu.moviesearcher.network

import android.app.Application
import ru.kettu.moviesearcher.network.api.TheMovieDbApi
import ru.kettu.moviesearcher.network.client.RetrofitClient.getRetrofitClient

object FilmSearchApp: Application() {

    var theMovieDbApi: TheMovieDbApi? = null

    override fun onCreate() {
        super.onCreate()
        initRetrofitClient()
    }

    private fun initRetrofitClient() {
        val retrofit = getRetrofitClient()
        theMovieDbApi = retrofit?.create(TheMovieDbApi::class.java)
    }
}