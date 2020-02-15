package ru.kettu.moviesearcher.network.client

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kettu.moviesearcher.constants.NetworkConstants.THE_MOVIE_DB_URL
import ru.kettu.moviesearcher.network.interceptor.LoggingInterceptor.getLoggerInterceptor

object RetrofitClient {

    var retrofit: Retrofit? = null

    fun getRetrofitClient(): Retrofit? {
        val client = OkHttpClient.Builder()
            .addInterceptor(getLoggerInterceptor())
            .build()
        return if (retrofit == null)
            Retrofit.Builder()
                .client(client)
                .baseUrl(THE_MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            else retrofit
    }
}