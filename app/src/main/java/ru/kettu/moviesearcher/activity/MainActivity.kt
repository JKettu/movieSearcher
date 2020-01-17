package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity.Companion.DETAILS_INFO
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.operations.openFilmDescriptionActivity
import ru.kettu.moviesearcher.operations.setDefaultTextColor

class MainActivity : AppCompatActivity() {

    var selectedFilmNameId: Int = 0

    companion object {
        const val SELECTED_FILM_COLOR = "SELECTED_FILM_COLOR"
        const val FILM_INFO = "FILM_INFO"
        const val FILM_DETAILS_INFO_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            val selectedTextColor = it.getInt(SELECTED_FILM_COLOR)
            if (selectedTextColor == 0 || selectedFilmNameId == 0) return
            val selectedText = findViewById<TextView>(selectedFilmNameId)
            selectedText.setTextColor(selectedTextColor)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (selectedFilmNameId == 0) return
        val selectedFilm = findViewById<TextView>(selectedFilmNameId)
        outState.putInt(SELECTED_FILM_COLOR, selectedFilm.currentTextColor)
    }

    fun onDetailsBtnClick(view: View?){
        if (view == null || view !is Button) return
        val intent = Intent(this@MainActivity, FilmDetailActivity::class.java)
        if (selectedFilmNameId != 0) {
            val selectedText = findViewById<TextView>(selectedFilmNameId)
            selectedText.setDefaultTextColor()
        }
        when(view.id) {
            R.id.dogsPurposeDetailsBtn ->{
                openFilmDescriptionActivity(intent, R.id.dogsPurposeText, R.string.dogsPurposeDesc, R.drawable.dogs_purpose)
            }
            R.id.harryPotter1DetailsBtn -> {
                openFilmDescriptionActivity(intent, R.id.harryPotterText, R.string.harryPotterDesc, R.drawable.harry_potter_1)
            }
            R.id.sweeneyToddDetailsBtn -> {
                openFilmDescriptionActivity(intent, R.id.sweeneyToddText, R.string.sweeneyToddDesc, R.drawable.sweeney_todd)
            }
            R.id.ageOfAdalineDetailsBtn -> {
                openFilmDescriptionActivity(intent, R.id.ageOfAdalineText, R.string.ageOfAdalineDesc, R.drawable.age_of_adaline)
            }
        }
    }
    
    fun onInviteBtnClick(view: View?) {
        if (view == null || view !is Button) return
        val intent = Intent(ACTION_SEND)
        intent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        val chooser = Intent.createChooser(intent, title)
        intent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILM_DETAILS_INFO_REQUEST_CODE -> {
                if (RESULT_OK == (resultCode) && data != null) {
                   val detailInfo = data.getParcelableExtra<FilmDetailsInfo>(DETAILS_INFO)
                   detailInfo?.apply {
                       Log.i(MainActivity::class.java.toString(), "is liked: $isLiked")
                       Log.i(MainActivity::class.java.toString(), "comment: $comment")
                   }
                }
            }
        }
    }
}
