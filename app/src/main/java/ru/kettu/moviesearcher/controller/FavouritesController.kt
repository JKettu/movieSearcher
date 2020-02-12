package ru.kettu.moviesearcher.controller

import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment

fun addToFavourites(fragment: FavouritesFragment, film: FilmItem) {
    fragment.films.add(film)
    val elemPosition = fragment.notInFavourites.indexOf(film)
    fragment.notInFavourites.remove(film)
    fragment.recycleViewFav.adapter?.notifyItemInserted(fragment.films.indexOf(film))
    fragment.filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
}

fun deleteFromFavourites(fragment: FavouritesFragment, film: FilmItem, layoutPosition: Int) {
    fragment.films.remove(film)
    fragment.notInFavourites.add(film)
    fragment.recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
    fragment.filmsToAddRV.adapter?.notifyItemInserted(fragment.notInFavourites.indexOf(film))
}