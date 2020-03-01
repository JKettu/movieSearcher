package ru.kettu.moviesearcher.models.item

import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import ru.kettu.moviesearcher.models.network.Genres
import java.io.Serializable

data class FilmItem (val id: Int, val title: String,
                     val description: String, var posterPath: String?,
                     val rating: String, val genres: List<Genres>?,
                     val releaseDate: String, var isSelected: Boolean = false): Serializable {

    init {
        this.posterPath = POSTER_PREFIX + this.posterPath
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilmItem

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (posterPath != other.posterPath) return false
        if (rating != other.rating) return false
        if (releaseDate != other.releaseDate) return false
        if (isSelected != other.isSelected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}