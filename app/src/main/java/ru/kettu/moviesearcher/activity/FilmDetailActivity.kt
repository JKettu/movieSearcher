package ru.kettu.moviesearcher.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.models.FilmInfo

class FilmDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        setSupportActionBar(filmDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.empty)
        val intentExtras = intent.extras
        val img = filmImg
        val text = filmDesc
        if (intentExtras == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }
        val details = intentExtras.getParcelable<FilmInfo>(FILM_INFO)
        if (details == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }

        img.setImageResource(details.filmPosterId)
        text.setText(details.filmDescriptionId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
