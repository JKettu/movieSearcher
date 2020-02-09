package ru.kettu.moviesearcher.models.item

import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import java.io.Serializable

data class FilmItem (val title: String, val description: String, var posterPath: String): Serializable, Comparable<FilmItem> {

    init {
        this.posterPath = POSTER_PREFIX + this.posterPath
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is FilmItem) return false
        return this.title.equals(other.title)
    }

    override fun hashCode(): Int {
        var hash = 37
        hash = hash * 17 + this.title.hashCode()
        return hash
    }

    override fun compareTo(other: FilmItem): Int {
        return if(this.title > other.title) 1 else if (this.title < other.title) -1 else 0
    }
}