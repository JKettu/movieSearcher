package ru.kettu.moviesearcher.models.item

import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import java.io.Serializable

data class FilmItem (val id: Int, val title: String, val description: String, var posterPath: String?): Serializable {

    init {
        this.posterPath = POSTER_PREFIX + this.posterPath
    }
}