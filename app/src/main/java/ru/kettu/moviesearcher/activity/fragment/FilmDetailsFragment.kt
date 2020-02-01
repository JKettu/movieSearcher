package ru.kettu.moviesearcher.activity.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.models.FilmInfo

class FilmDetailsFragment: Fragment(R.layout.fragment_film_detail) {

    companion object {
        const val FILM_DETAILS_FRAGMENT = "FILM_DETAILS_FRAGMENT"

        fun newInstance(bundle: Bundle): FilmDetailsFragment{
            val fragment = FilmDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmInfo = arguments?.get(FILM_INFO) as FilmInfo

        filmInfo?.let {
            filmDesc.text = getString(filmInfo.filmDescriptionId)
            val poster = filmInfo.filmPosterId
            poster?.let {
                filmImg.setImageResource(poster)
            }
        }
    }
}