package ru.kettu.moviesearcher.models

import java.io.Serializable

data class FavouriteInfo (val filmPosterId: Int, val filmName: String): Serializable, Comparable<FavouriteInfo> {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is FavouriteInfo) return false
        return this.filmName.equals(other.filmName)
    }

    override fun hashCode(): Int {
        var hash = 37
        hash = hash * 17 + this.filmName.hashCode()
        return hash
    }

    override fun compareTo(other: FavouriteInfo): Int {
        return if(this.filmName > other.filmName) 1 else if (this.filmName < other.filmName) -1 else 0
    }
}