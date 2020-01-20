package ru.kettu.moviesearcher.operations

import android.content.Intent
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.models.FilmInfo

fun TextView.setDefaultTextColor(){
    val colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}

fun MainActivity.openFilmDescriptionActivity(text: TextView, descriptionId: Int, posterId: Int) {
    val intent = Intent(this, FilmDetailActivity::class.java)
    val colorAccentDark = resources.getColor(R.color.colorAccentDark)
    text.setTextColor(colorAccentDark)
    this.selectedFilmNameId = text.id

    intent.putExtra(MainActivity.FILM_INFO, FilmInfo(descriptionId, posterId))
    startActivityForResult(intent, MainActivity.FILM_DETAILS_INFO_REQUEST_CODE)
}

fun MainActivity.getSelectedTextView(textId: Int): TextView? {
    return when (textId) {
        R.id.harryPotterText -> harryPotterText
        R.id.sweeneyToddText -> sweeneyToddText
        R.id.dogsPurposeText -> dogsPurposeText
        R.id.ageOfAdalineText -> ageOfAdalineText
        else -> null
    }
}