package ru.kettu.moviesearcher.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (indices = [Index(value = ["id"])])
data class Favourite (@PrimaryKey(autoGenerate = true) var id: Long,
                      @ColumnInfo(name = "movie_id") var movieId: Long) {
}