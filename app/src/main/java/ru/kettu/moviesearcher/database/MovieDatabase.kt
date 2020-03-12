package ru.kettu.moviesearcher.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kettu.moviesearcher.database.dao.FavouriteDao
import ru.kettu.moviesearcher.database.dao.MovieDao
import ru.kettu.moviesearcher.models.database.*

@Database(entities = [Movie::class, Country::class, Genre::class,
    MovieCountryCrossRef::class, MovieGenreCrossRef::class, Favourite::class],
    version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    abstract fun getFavouriteDao(): FavouriteDao
}