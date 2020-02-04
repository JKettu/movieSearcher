package ru.kettu.moviesearcher.operations

import android.app.Activity
import android.content.res.Resources
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FilmInfo
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

fun initFilmItems(): ArrayList<FilmItem> {
    return arrayListOf<FilmItem>(
        FilmItem(R.drawable.harry_potter_1, R.string.harryPotter1Film),
        FilmItem(R.drawable.sweeney_todd, R.string.sweeneyToddFilm),
        FilmItem(R.drawable.dogs_purpose, R.string.dogsPurposeFilm),
        FilmItem(R.drawable.age_of_adaline, R.string.ageOfAdalineFilm),
        FilmItem(R.drawable.the_avengers, R.string.theAvengersFilm),
        FilmItem(R.drawable.morning_glory, R.string.morningGloryFilm),
        FilmItem(R.drawable.the_lord_of_the_rings1, R.string.theLordOfTheRings1Film),
        FilmItem(R.drawable.treasure_planet, R.string.treasurePlanetFilm),
        FilmItem(R.drawable.coco, R.string.cocoFilm),
        FilmItem(R.drawable.klaus, R.string.klausFilm),
        FilmItem(R.drawable.the_nightmare_before_christmas, R.string.theNightmareBeforeChristmasFilm)
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
                                  films: TreeSet<FilmItem>?, notInFilms: TreeSet<FilmItem>) {
    if (films == null) return
    allFilms?.forEach { film ->
        run {
            if (!films.contains(FilmItem(film.filmPosterId, film.filmNameId)))
                notInFilms.add(FilmItem(film.filmPosterId, film.filmNameId))
        }
    }
}

fun onDayNightModeSwitch(mode: SwitchCompat, activity: Activity) {
    if (mode.isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    ActivityCompat.recreate(activity)
}