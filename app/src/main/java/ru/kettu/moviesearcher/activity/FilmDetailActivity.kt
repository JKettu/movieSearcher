package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.constants.Constants.EMPTY_STRING
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.models.FilmInfo

class FilmDetailActivity : AppCompatActivity() {

    companion object {
        const val DETAILS_INFO = "DETAILS_INFO"
    }

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

    override fun onBackPressed() {
        val intent = Intent()
        val likeCheckBox = likeCheckBox
        val commentText = comment
        intent.putExtra(DETAILS_INFO,
            FilmDetailsInfo(likeCheckBox.isChecked, if (commentText.text == null) EMPTY_STRING
                                                        else commentText.text.toString()))
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
