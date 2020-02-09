package ru.kettu.moviesearcher.network.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kettu.moviesearcher.constants.NetworkConstants.THE_MOVIE_DB_URL

object RetrofitClient {

    var retrofit: Retrofit? = null

    fun getRetrofitClient(): Retrofit? {
        return if (retrofit == null)
            Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            else retrofit
    }
}