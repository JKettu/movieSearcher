package ru.kettu.moviesearcher.models.database

import androidx.room.Embedded
import androidx.room.Relation

data class FavouriteMovie (@Relation(parentColumn = "id", entityColumn = "movieId")
                           val favourite: Favourite,
                           @Embedded val movie: Movie) {
}