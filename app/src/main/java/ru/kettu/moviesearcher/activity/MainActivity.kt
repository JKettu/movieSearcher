package ru.kettu.moviesearcher.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.operations.setDefaultTextColor
import ru.kettu.moviesearcher.operations.setTextViewColor

class MainActivity : AppCompatActivity() {

    private val films: MutableMap<String, Int> = hashMapOf(
        DOGS_PURPOSE to R.id.dogsPurposeText,
        HARRY_POTTER_1 to R.id.harryPotterText,
        SWEENEY_TODD to R.id.sweeneyToddText
    )

    companion object {
        //Films
        const val DOGS_PURPOSE = "DOGS_PURPOSE"
        const val HARRY_POTTER_1 = "HARRY_POTTER_1"
        const val SWEENEY_TODD = "SWEENEY_TODD"


        const val DETAILS = "DETAILS"
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) return

        films.forEach { film ->
            run {
                val currentFilm = findViewById<TextView>(film.value)
                currentFilm.setTextViewColor(savedInstanceState, film.key)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val dogsPurpose = findViewById<TextView>(R.id.dogsPurposeText)
        val harryPotter1 = findViewById<TextView>(R.id.harryPotterText)
        val sweeneyTodd = findViewById<TextView>(R.id.sweeneyToddText)
        outState.putInt(DOGS_PURPOSE, dogsPurpose.currentTextColor)
        outState.putInt(HARRY_POTTER_1, harryPotter1.currentTextColor)
        outState.putInt(SWEENEY_TODD, sweeneyTodd.currentTextColor)
    }

    fun onDetailsBtnClick(view: View?){
        if (view == null || view !is Button) return
        val intent = Intent(this@MainActivity, FilmDetailActivity::class.java)
        var colorAccentDark = resources.getColor(R.color.colorAccentDark)
        when(view.id) {
            R.id.dogsPurposeDetailsBtn -> {
                val text = findViewById<TextView>(R.id.dogsPurposeText)
                films.forEach{film ->
                    val currentFilm = findViewById<TextView>(film.value)
                    currentFilm.setDefaultTextColor()
                }
                text.setTextColor(colorAccentDark)

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.dogsPurposeDesc, R.drawable.dogspurpose))
                startActivity(intent)
            }
            R.id.harryPotter1DetailsBtn -> {
                val text = findViewById<TextView>(R.id.harryPotterText)
                films.forEach{film ->
                    val currentFilm = findViewById<TextView>(film.value)
                    currentFilm.setDefaultTextColor()
                }
                text.setTextColor(colorAccentDark)

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.harryPotterDesc, R.drawable.harrypotter))
                startActivity(intent)
            }
            R.id.sweeneyToddDetailsBtn -> {
                val text = findViewById<TextView>(R.id.sweeneyToddText)
                films.forEach{film ->
                    val currentFilm = findViewById<TextView>(film.value)
                    currentFilm.setDefaultTextColor()
                }
                text.setTextColor(colorAccentDark)

                intent.putExtra(DETAILS, FilmDetailsInfo(R.string.sweeneyToddDesc, R.drawable.sweeneytodd))
                startActivity(intent)
            }
        }
    }
}
