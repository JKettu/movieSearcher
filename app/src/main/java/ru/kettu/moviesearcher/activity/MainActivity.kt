package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import kotlinx.android.synthetic.main.content_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity.Companion.DETAILS_INFO
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.operations.getSelectedTextView
import ru.kettu.moviesearcher.operations.openFilmDescriptionActivity
import ru.kettu.moviesearcher.operations.setDefaultTextColor
import ru.kettu.moviesearcher.operations.showAlertDialog

class MainActivity : AppCompatActivity() {

    var selectedFilmNameId: Int = 0

    companion object {
        const val SELECTED_FILM_COLOR = "SELECTED_FILM_COLOR"
        const val SELECTED_FILM_TEXT_ID = "SELECTED_FILM_TEXT_ID"
        const val FILM_INFO = "FILM_INFO"
        const val FILM_DETAILS_INFO_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            val selectedTextColor = it.getInt(SELECTED_FILM_COLOR)
            selectedFilmNameId = it.getInt(SELECTED_FILM_TEXT_ID)
            if (selectedTextColor == 0 || selectedFilmNameId == 0) return
            val selectedText = getSelectedTextView(selectedFilmNameId)
            selectedText?.setTextColor(selectedTextColor)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (selectedFilmNameId == 0) return
        val selectedFilm = getSelectedTextView(selectedFilmNameId)
        selectedFilm?.let {
            outState.putInt(SELECTED_FILM_COLOR, it.currentTextColor)
            outState.putInt(SELECTED_FILM_TEXT_ID, selectedFilmNameId)
        }
    }

    fun onDetailsBtnClick(view: View?){
        if (view == null || view !is Button) return
        if (selectedFilmNameId != 0) {
            val selectedText = getSelectedTextView(selectedFilmNameId)
            selectedText?.setDefaultTextColor()
        }
        when(view.id) {
            R.id.dogsPurposeDetailsBtn ->
                openFilmDescriptionActivity(dogsPurposeText, R.string.dogsPurposeDesc, R.drawable.dogs_purpose)
            R.id.harryPotter1DetailsBtn ->
                openFilmDescriptionActivity(harryPotterText, R.string.harryPotterDesc, R.drawable.harry_potter_1)
            R.id.sweeneyToddDetailsBtn ->
                openFilmDescriptionActivity(sweeneyToddText, R.string.sweeneyToddDesc, R.drawable.sweeney_todd)
            R.id.ageOfAdalineDetailsBtn ->
                openFilmDescriptionActivity(ageOfAdalineText, R.string.ageOfAdalineDesc, R.drawable.age_of_adaline)
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
                       Log.i(MainActivity::class.java.toString(), "FilmInfo: is liked - $isLiked")
                       if (comment.isNotEmpty())
                        Log.i(MainActivity::class.java.toString(), "FilmInfo: comment - $comment")
                   }
                }
            }
        }
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    fun onModeSwitchClick(view: View?) {
        if (view == null || view !is Switch) return
        if (mode.isChecked) {
            setDefaultNightMode(MODE_NIGHT_YES)
            val selectedFilm = getSelectedTextView(selectedFilmNameId)
            val colorAccentDark = resources.getColor(R.color.colorAccentDark)
            selectedFilm?.setTextColor(colorAccentDark)
        } else {
            setDefaultNightMode(MODE_NIGHT_NO)
            val selectedFilm = getSelectedTextView(selectedFilmNameId)
            val colorAccentDark = resources.getColor(R.color.colorAccentDark)
            selectedFilm?.setTextColor(colorAccentDark)
        }
    }
}
