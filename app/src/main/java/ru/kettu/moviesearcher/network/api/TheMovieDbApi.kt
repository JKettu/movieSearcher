package ru.kettu.moviesearcher.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kettu.moviesearcher.constants.NetworkConstants.THE_MOVIE_DB_API_KEY
import ru.kettu.moviesearcher.constants.QueryParamsConstants.API_KEY
import ru.kettu.moviesearcher.constants.QueryParamsConstants.LANGUAGE
import ru.kettu.moviesearcher.models.network.FilmListResponse

interface TheMovieDbApi {

    @GET("movie/now-playing")
    fun getNowPlayingFilms(@Query(LANGUAGE) lang: String, @Query(API_KEY) apiKey: String = THE_MOVIE_DB_API_KEY): Call<FilmListResponse>

    @GET("movie/{id}")
    fun getFilmDetails(@Path("id") id: Int,
                       @Query(LANGUAGE) lang: String,
                       @Query(API_KEY) apiKey: String = THE_MOVIE_DB_API_KEY): Call<FilmListResponse>
}