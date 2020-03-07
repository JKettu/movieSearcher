package ru.kettu.moviesearcher.network.interactor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.Constants
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.FilmListResponse
import ru.kettu.moviesearcher.models.network.Genres
import ru.kettu.moviesearcher.models.network.LoaderResponse

object TheMovieDbLoader: Loader {

    override fun loadFilmSet(call: Call<LoaderResponse>, callback: Loader.LoaderCallback) {
        call.enqueue(object: Callback<LoaderResponse> {
            override fun onFailure(call: Call<LoaderResponse>, t: Throwable) {
                callback.onFailed(R.string.filmLoadingFailed)
            }

            override fun onResponse(call: Call<LoaderResponse>, response: Response<LoaderResponse>) {
                val films: List<FilmDetails>? = (response as Response<FilmListResponse>).body()?.results
                val filmItems = LinkedHashSet<FilmItem>()
                films?.forEach {
                    it.genres = fillGenres(it.genres, it.genreIds)
                    filmItems.add(
                        FilmItem(it.id, it.title, it.overview, it.posterPath,
                            it.voteAverage.toString(), it.genres, it.releaseDate)
                    )
                }
                callback.onSucceed(filmItems)
            }
        })
    }

    override fun loadFilm(call: Call<LoaderResponse>, callback: Loader.LoaderCallback) {
        call.enqueue(object: Callback<LoaderResponse> {
            override fun onFailure(call: Call<LoaderResponse>, t: Throwable) {
                callback.onFailed(R.string.filmLoadingFailed)
            }

            override fun onResponse(call: Call<LoaderResponse>, response: Response<LoaderResponse>) {
                val responseItem = response.body() as FilmDetails
                responseItem.genres = fillGenres(responseItem.genres, responseItem.genreIds)
                val filmItem = FilmItem(responseItem.id, responseItem.title, responseItem.overview,
                    responseItem.posterPath, responseItem.voteAverage.toString(), responseItem.genres, responseItem.releaseDate)
                val set = LinkedHashSet<FilmItem>()
                set.add(filmItem)
                callback.onSucceed(set)
            }
        })
    }

    private fun fillGenres(genresList: List<Genres>?, genreIds: List<Int>?): List<Genres> {
        if (genresList == null || genresList.isEmpty()) {
            val list = ArrayList<Genres>()
            genreIds?.forEach { id ->
                list.add(Genres(id, Constants.EMPTY_STRING))
            }
            return list
        }
        return genresList
    }
}