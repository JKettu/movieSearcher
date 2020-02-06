package ru.kettu.moviesearcher.operations

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FavouritesActivity
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
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

fun MainActivity.showAlertDialog() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage(R.string.exitQuestion)
    builder.setNegativeButton(R.string.no) { dialog, which ->
        dialog.dismiss()
    }
    builder.setPositiveButton(R.string.yes) { dialog, which ->
        run {
            dialog.dismiss()
            this.finish()
        }
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun MainActivity.initFilmItems(): ArrayList<FilmItem> {
    return arrayListOf<FilmItem>(
        FilmItem(R.string.harryPotter1Film, R.drawable.harry_potter_1),
        FilmItem(R.string.sweeneyToddFilm, R.drawable.sweeney_todd),
        FilmItem(R.string.dogsPurposeFilm, R.drawable.dogs_purpose),
        FilmItem(R.string.ageOfAdalineFilm, R.drawable.age_of_adaline),
        FilmItem(R.string.theAvengersFilm, R.drawable.the_avengers),
        FilmItem(R.string.morningGloryFilm, R.drawable.morning_glory),
        FilmItem(R.string.theLordOfTheRings1Film, R.drawable.the_lord_of_the_rings1),
        FilmItem(R.string.treasurePlanetFilm, R.drawable.treasure_planet),
        FilmItem(R.string.cocoFilm, R.drawable.coco),
        FilmItem(R.string.klausFilm, R.drawable.klaus),
        FilmItem(R.string.theNightmareBeforeChristmasFilm, R.drawable.the_nightmare_before_christmas)
    )
}

fun MainActivity.openFavouritesActivity(favourites: TreeSet<FavouriteItem>, allFilms: List<FilmItem>, reqCode: Int) {
    val intent = Intent(this, FavouritesActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(FILM_INFO, favourites)
    bundle.putParcelableArrayList(ALL_FILMS, allFilms as ArrayList<FilmItem>)
    intent.putExtra(FILM_INFO, bundle)
    this.startActivityForResult(intent, reqCode)
}