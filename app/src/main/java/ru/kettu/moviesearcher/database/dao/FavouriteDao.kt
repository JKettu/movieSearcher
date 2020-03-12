package ru.kettu.moviesearcher.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kettu.moviesearcher.models.database.Favourite
import ru.kettu.moviesearcher.models.database.FavouriteMovie

@Dao
interface FavouriteDao {

    @Insert (onConflict = REPLACE)
    fun addFavourite(favourite: Favourite)

    @Delete
    fun deleteFavourite(favourite: Favourite)

    @Query("select * from favourite")
    fun getFavourites(): List<FavouriteMovie>

    @Query("select * from favourite where movie_id = :id")
    fun getFavouriteByMovieId(id: Long)
}