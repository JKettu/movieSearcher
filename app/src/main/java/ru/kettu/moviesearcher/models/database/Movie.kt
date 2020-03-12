package ru.kettu.moviesearcher.models.database

import androidx.room.*
import java.util.*

@Entity (indices = [Index(value = ["id"]), Index(value = ["name"])])
data class Movie (@PrimaryKey (autoGenerate = true) var id: Long,
             @ColumnInfo(name = "storage_id") var storageId: Long,
             var name: String?,
             var rating: Double,
             var description: String?,
             @ColumnInfo(name = "release_date") var releaseDate: Date,
             var poster: String?,
             @Relation(parentColumn = "id", entityColumn = "id",
                 associateBy = Junction(MovieCountryCrossRef::class)
             )
             var country: List<Country>?,
             @Relation(parentColumn = "id", entityColumn = "id",
                 associateBy = Junction(MovieGenreCrossRef::class)
             )
             var genreId: List<Genre>?) {
}