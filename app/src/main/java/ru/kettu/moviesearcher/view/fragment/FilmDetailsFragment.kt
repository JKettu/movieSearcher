package ru.kettu.moviesearcher.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.initFilmDetailLoading
import ru.kettu.moviesearcher.controller.setPosterRoundImgAnimation
import ru.kettu.moviesearcher.models.item.FilmItem

class FilmDetailsFragment: Fragment(R.layout.fragment_film_detail) {

    var listener: OnFilmDetailsAction? = null
    var currentOffset = 0

    companion object {
        const val FILM_DETAILS_FRAGMENT = "FILM_DETAILS_FRAGMENT"
        const val FILM_INFO = "FILM_INFO"

        fun newInstance(item: FilmItem): FilmDetailsFragment {
            val fragment = FilmDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(FILM_INFO, item)
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

        initFilmDetailLoading(filmInfo.id, this)
        detailAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            run{
                currentOffset = setPosterRoundImgAnimation(verticalOffset, currentOffset)
            }

        })
        listener?.onFragmentCreatedInitToolbar(this)
    }

    interface OnFilmDetailsAction {
        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}