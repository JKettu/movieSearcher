package ru.kettu.moviesearcher.operations

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.item.FilmItem

fun TextView.setDefaultTextColor(){
    val colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}

fun TextView.setSelectedTextColor(){
    val colorAccentDark = resources.getColor(R.color.colorAccentDark)
    this.setTextColor(colorAccentDark)
}

fun MainActivity.showAlertDialog() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage(R.string.exitQuestion)
    builder.setNegativeButton(R.string.no) { dialog, which ->  dialog.dismiss()
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
        FilmItem(resources.getString(R.string.harryPotterFilm), R.drawable.harry_potter_1),
        FilmItem(resources.getString(R.string.sweeneyToddFilm), R.drawable.sweeney_todd),
        FilmItem(resources.getString(R.string.dogsPurposeFilm), R.drawable.dogs_purpose),
        FilmItem(resources.getString(R.string.ageOfAdalineFilm), R.drawable.age_of_adaline)
    )
}