package ru.kettu.moviesearcher.activity.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.controller.setPosterRoundImgAnimation
import ru.kettu.moviesearcher.models.item.FilmItem

class FilmDetailsFragment: Fragment(R.layout.fragment_film_detail) {

    var listener: OnFilmDetailsAction? = null
    var currentOffset = 0

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
        val filmInfo = arguments?.get(FILM_INFO) as FilmItem

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(detailToolbar)
            val toolbar = (activity as AppCompatActivity).supportActionBar
            toolbar?.setTitle(R.string.empty)
            toolbar?.setDisplayHomeAsUpEnabled(true)
            toolbar?.setHomeButtonEnabled(true)
        }

        filmInfo?.let {
            filmDesc.text = filmInfo.description
            loadImage(filmImg, filmInfo.posterPath)
            loadImage(filmBack, filmInfo.posterPath)
        }
        listener?.onFragmentCreatedInitToolbar(this)
        detailAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            run{
                currentOffset = setPosterRoundImgAnimation(verticalOffset, currentOffset)
            }

        })
    }

    interface OnFilmDetailsAction {
        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}