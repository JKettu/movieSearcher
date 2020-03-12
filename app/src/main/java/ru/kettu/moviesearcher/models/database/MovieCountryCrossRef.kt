package ru.kettu.moviesearcher.models.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["movieId", "countryId"],
    foreignKeys = [ForeignKey( entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"]),
                ForeignKey(entity = Country::class, parentColumns = ["id"], childColumns = ["countryId"])])
data class MovieCountryCrossRef (val movieId: Long, val countryId: Long) {
}