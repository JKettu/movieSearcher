package ru.kettu.moviesearcher.models.item

import java.io.Serializable

data class FilmItem (val filmPosterId: Int, val filmNameId: Int): Serializable, Comparable<FilmItem> {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is FilmItem) return false
        return this.filmNameId.equals(other.filmNameId)
    }

    override fun hashCode(): Int {
        var hash = 37
        hash = hash * 17 + this.filmNameId.hashCode()
        return hash
    }

    override fun compareTo(other: FilmItem): Int {
        return if(this.filmNameId > other.filmNameId) 1 else if (this.filmNameId < other.filmNameId) -1 else 0
    }
}