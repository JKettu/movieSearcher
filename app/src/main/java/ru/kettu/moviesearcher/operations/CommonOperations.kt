package ru.kettu.moviesearcher.operations

import android.content.res.Resources
import android.widget.TextView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FilmInfo
import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

fun TextView.setDefaultTextColor() {
    val colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}

fun TextView.setSelectedTextColor() {
    val colorAccentDark = resources.getColor(R.color.colorAccentDark)
    this.setTextColor(colorAccentDark)
}

fun Resources.initFilmItems(): ArrayList<FilmItem> {
    return arrayListOf<FilmItem>(
        FilmItem(R.string.harryPotter1Film, R.drawable.harry_potter_1, getString(R.string.harryPotter1Film)),
        FilmItem(R.string.sweeneyToddFilm, R.drawable.sweeney_todd, getString(R.string.sweeneyToddFilm)),
        FilmItem(R.string.dogsPurposeFilm, R.drawable.dogs_purpose, getString(R.string.dogsPurposeFilm)),
        FilmItem(R.string.ageOfAdalineFilm, R.drawable.age_of_adaline, getString(R.string.ageOfAdalineFilm)),
        FilmItem(R.string.theAvengersFilm, R.drawable.the_avengers, getString(R.string.theAvengersFilm)),
        FilmItem(R.string.morningGloryFilm, R.drawable.morning_glory, getString(R.string.morningGloryFilm)),
        FilmItem(R.string.theLordOfTheRings1Film, R.drawable.the_lord_of_the_rings1, getString(R.string.theLordOfTheRings1Film)),
        FilmItem(R.string.treasurePlanetFilm, R.drawable.treasure_planet, getString(R.string.treasurePlanetFilm)),
        FilmItem(R.string.cocoFilm, R.drawable.coco, getString(R.string.cocoFilm)),
        FilmItem(R.string.klausFilm, R.drawable.klaus, getString(R.string.klausFilm)),
        FilmItem(
            R.string.theNightmareBeforeChristmasFilm, R.drawable.the_nightmare_before_christmas,
            getString(R.string.theNightmareBeforeChristmasFilm))
    )
}

fun Resources.getFilmInfoByFilmName(filmName: String?): FilmInfo? {
    return when (filmName) {
        getString(R.string.dogsPurposeFilm) ->
            FilmInfo(R.string.dogsPurposeDesc, R.drawable.dogs_purpose)
        getString(R.string.harryPotter1Film) ->
            FilmInfo(R.string.harryPotterDesc, R.drawable.harry_potter_1)
        getString(R.string.sweeneyToddFilm) ->
            FilmInfo(R.string.sweeneyToddDesc, R.drawable.sweeney_todd)
        getString(R.string.ageOfAdalineFilm) ->
            FilmInfo(R.string.ageOfAdalineDesc, R.drawable.age_of_adaline)
        getString(R.string.theAvengersFilm) ->
            FilmInfo(R.string.theAvengersDesc, R.drawable.the_avengers)
        getString(R.string.morningGloryFilm) ->
            FilmInfo(R.string.morningGloryDesc, R.drawable.morning_glory)
        getString(R.string.theLordOfTheRings1Film) ->
            FilmInfo(R.string.theLordOfTheRings1Desc, R.drawable.the_lord_of_the_rings1)
        getString(R.string.treasurePlanetFilm) ->
            FilmInfo(R.string.treasurePlanetDesc, R.drawable.treasure_planet)
        getString(R.string.cocoFilm) ->
            FilmInfo(R.string.cocoDesc, R.drawable.coco)
        getString(R.string.klausFilm) ->
            FilmInfo(R.string.klausDesc, R.drawable.klaus)
        getString(R.string.theNightmareBeforeChristmasFilm) ->
            FilmInfo(
                R.string.theNightmareBeforeChristmasDesc,
                R.drawable.the_nightmare_before_christmas
            )
        else -> null
    }
}

fun Resources.initNotInFavourites(allFilms: ArrayList<FilmItem>?,
                                  favourites: TreeSet<FavouriteItem>?, notInFavourites: TreeSet<FavouriteItem>) {
    if (favourites == null) return
    allFilms?.forEach { film ->
        run {
            if (!favourites.contains(FavouriteItem(film.posterId, film.filmNameId)))
                notInFavourites.add(FavouriteItem(film.posterId, film.filmNameId))
        }
    }
}