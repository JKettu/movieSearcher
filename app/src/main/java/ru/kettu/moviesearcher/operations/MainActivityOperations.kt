package ru.kettu.moviesearcher.operations

import android.content.Intent
import android.widget.TextView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.models.FilmInfo

fun TextView.setDefaultTextColor(){
    val colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}

fun MainActivity.openFilmDescriptionActivity(textId: Int, descriptionId: Int, posterId: Int) {
    val intent = Intent(this, FilmDetailActivity::class.java)
    val text = findViewById<TextView>(textId)
    val colorAccentDark = resources.getColor(R.color.colorAccentDark)
    text.setTextColor(colorAccentDark)
    this.selectedFilmNameId = textId

    intent.putExtra(MainActivity.FILM_INFO, FilmInfo(descriptionId, posterId))
    startActivityForResult(intent, MainActivity.FILM_DETAILS_INFO_REQUEST_CODE)
}