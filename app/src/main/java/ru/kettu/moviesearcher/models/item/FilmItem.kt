package ru.kettu.moviesearcher.models.item

import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import ru.kettu.moviesearcher.models.network.Genres
import java.io.Serializable

data class FilmItem (val id: Int, val title: String,
                     val description: String, var posterPath: String?,
                     val rating: String, val genres: List<Genres>?, val releaseDate: String): Serializable {

    init {
        this.posterPath = POSTER_PREFIX + this.posterPath
    }
}