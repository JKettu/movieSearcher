package ru.kettu.moviesearcher.operations

import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

fun TreeSet<FavouriteInfo>.initNotInFavourites(allFilms: ArrayList<FilmItem>?, favourites: TreeSet<FavouriteInfo>?) {
    if (favourites == null) return
    allFilms?.forEach { film ->
        run {
            if (!favourites.contains(FavouriteInfo(film.posterId, film.filmName)))
                this.add(FavouriteInfo(film.posterId, film.filmName))
        }
    }
}