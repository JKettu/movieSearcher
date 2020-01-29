package ru.kettu.moviesearcher.operations

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FavouritesActivity
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*
import kotlin.collections.ArrayList

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
        FilmItem(resources.getString(R.string.harryPotter1Film), R.drawable.harry_potter_1),
        FilmItem(resources.getString(R.string.sweeneyToddFilm), R.drawable.sweeney_todd),
        FilmItem(resources.getString(R.string.dogsPurposeFilm), R.drawable.dogs_purpose),
        FilmItem(resources.getString(R.string.ageOfAdalineFilm), R.drawable.age_of_adaline),
        FilmItem(resources.getString(R.string.theAvengersFilm), R.drawable.the_avengers),
        FilmItem(resources.getString(R.string.morningGloryFilm), R.drawable.morning_glory),
        FilmItem(
            resources.getString(R.string.theLordOfTheRings1Film),
            R.drawable.the_lord_of_the_rings1
        ),
        FilmItem(resources.getString(R.string.treasurePlanetFilm), R.drawable.treasure_planet),
        FilmItem(resources.getString(R.string.cocoFilm), R.drawable.coco),
        FilmItem(resources.getString(R.string.klausFilm), R.drawable.klaus),
        FilmItem(
            resources.getString(R.string.theNightmareBeforeChristmasFilm),
            R.drawable.the_nightmare_before_christmas
        )
    )
}

fun MainActivity.openFavouritesActivity(favourites: TreeSet<FavouriteInfo>, allFilms: List<FilmItem>, reqCode: Int) {
    val intent = Intent(this, FavouritesActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(FILM_INFO, favourites)
    bundle.putParcelableArrayList(ALL_FILMS, allFilms as ArrayList<FilmItem>)
    intent.putExtra(FILM_INFO, bundle)
    this.startActivityForResult(intent, reqCode)
}