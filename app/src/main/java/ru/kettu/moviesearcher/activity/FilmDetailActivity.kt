package ru.kettu.moviesearcher.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.DETAILS
import ru.kettu.moviesearcher.models.FilmDetailsInfo

class FilmDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        val intentExtras = intent.extras
        val img = findViewById<ImageView>(R.id.filmImg)
        val text = findViewById<TextView>(R.id.filmDesc)
        if (intentExtras == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }
        val details = intentExtras.getParcelable<FilmDetailsInfo>(DETAILS)
        if (details == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }

        img.setImageResource(details.filmPosterId)
        text.setText(details.filmDescriptionId)
    }
}
