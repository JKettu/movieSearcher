package ru.kettu.moviesearcher.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.kettu.moviesearcher.models.database.Movie

@Dao
interface MovieDao {

    @Insert (onConflict = REPLACE)
    fun addMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("delete from movie where id = :id")
    fun deleteMovie(id: Long)

    @Update (onConflict = REPLACE)
    fun updateMovie(movie: Movie)

    @Query("select * from movie")
    fun selectAll(): List<Movie>

    @Query("select * from movie where id = :id")
    fun selectById(id: Long): Movie
}