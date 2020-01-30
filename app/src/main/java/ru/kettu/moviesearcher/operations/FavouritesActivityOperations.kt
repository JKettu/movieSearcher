package ru.kettu.moviesearcher.operations

import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

fun TreeSet<FavouriteItem>.initNotInFavourites(allFilms: ArrayList<FilmItem>?, favourites: TreeSet<FavouriteItem>?) {
    if (favourites == null) return
    allFilms?.forEach { film ->
        run {
            if (!favourites.contains(FavouriteItem(film.posterId, film.filmNameId)))
                this.add(FavouriteItem(film.posterId, film.filmNameId)
                )
        }
    }
}