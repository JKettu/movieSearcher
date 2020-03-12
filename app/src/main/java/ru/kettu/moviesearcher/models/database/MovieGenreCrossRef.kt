package ru.kettu.moviesearcher.models.database

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRef (val movieId: Long, val genreId: Long) {
}