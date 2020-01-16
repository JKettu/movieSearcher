package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.operations.setDefaultTextColor

class MainActivity : AppCompatActivity() {

    private var selectedFilmNameId: Int = 0

    companion object {
        const val SELECTED_FILM_COLOR = "SELECTED_FILM_COLOR"

        const val DETAILS = "DETAILS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val selectedTextColor = savedInstanceState.getInt(SELECTED_FILM_COLOR)
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
        var colorAccentDark = resources.getColor(R.color.colorAccentDark)
        if (selectedFilmNameId != 0) {
            val selectedText = findViewById<TextView>(selectedFilmNameId)
            selectedText.setDefaultTextColor()
        }
        when(view.id) {
            R.id.dogsPurposeDetailsBtn -> {
                val text = findViewById<TextView>(R.id.dogsPurposeText)
                text.setTextColor(colorAccentDark)
                selectedFilmNameId = R.id.dogsPurposeText

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.dogsPurposeDesc, R.drawable.dogs_purpose))
                startActivity(intent)
            }
            R.id.harryPotter1DetailsBtn -> {
                val text = findViewById<TextView>(R.id.harryPotterText)
                text.setTextColor(colorAccentDark)
                selectedFilmNameId = R.id.harryPotterText

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.harryPotterDesc, R.drawable.harry_potter_1))
                startActivity(intent)
            }
            R.id.sweeneyToddDetailsBtn -> {
                val text = findViewById<TextView>(R.id.sweeneyToddText)
                text.setTextColor(colorAccentDark)
                selectedFilmNameId = R.id.sweeneyToddText

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.sweeneyToddDesc, R.drawable.sweeney_todd))
                startActivity(intent)
            }
            R.id.ageOfAdalineDetailsBtn -> {
                val text = findViewById<TextView>(R.id.ageOfAdalineText)
                text.setTextColor(colorAccentDark)
                selectedFilmNameId = R.id.ageOfAdalineText

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.ageOfAdalineDesc, R.drawable.age_of_adaline))
                startActivity(intent)
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
}
