package ru.kettu.moviesearcher

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ru.kettu.moviesearcher.constants.Constants.SharedPrefs.PREF_NAME
import ru.kettu.moviesearcher.database.MovieDatabase
import ru.kettu.moviesearcher.network.api.TheMovieDbApi
import ru.kettu.moviesearcher.network.client.RetrofitClient.getRetrofitClient

object FilmSearchApp: Application() {

    var theMovieDbApi: TheMovieDbApi? = null
    var movieDatabase: MovieDatabase? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        initRetrofitClient()
        initSharedPreferences()
    }

    private fun initRetrofitClient() {
        val retrofit = getRetrofitClient()
        theMovieDbApi = retrofit?.create(TheMovieDbApi::class.java)
    }

    fun initMovieDatabase(context: Context) {
        synchronized(FilmSearchApp::class) {
            movieDatabase = Room.databaseBuilder(
                context, MovieDatabase::class.java, "movie_database")
                .build()
        }
    }

    fun destroyMovieDatabase() {
        movieDatabase?.close()
        movieDatabase = null
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}