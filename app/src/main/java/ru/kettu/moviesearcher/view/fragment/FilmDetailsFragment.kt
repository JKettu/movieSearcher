package ru.kettu.moviesearcher.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.ofFloat
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.TRANSLATION_X
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.Constants
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.viewmodel.FilmDetailsViewModel

class FilmDetailsFragment: Fragment(R.layout.fragment_film_detail) {

    private val detailsViewModel by lazy {
        ViewModelProvider(activity!!).get(FilmDetailsViewModel::class.java)
    }

    var listener: OnFilmDetailsAction? = null
    private var currentOffset = 0

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

        detailsViewModel.loadFilm(resources, filmInfo.id, view.context)

        detailAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            run{
                currentOffset = setPosterRoundImgAnimation(verticalOffset, currentOffset, filmImg, appBarLayout)
            }

        })

        listener?.onFragmentCreatedInitToolbar(this)

        detailsViewModel.film.observe(viewLifecycleOwner, Observer {filmItem ->
            filmItem?.let {
                val releaseDate = filmItem.releaseDate.substringBefore(Constants.DASH)
                val title = "${filmItem.title} ($releaseDate)"
                filmDesc?.text = filmItem.description
                filmItem.genres?.let {
                    filmGenres?.text = detailsViewModel.convertGenresListToString(resources, filmItem.genres)
                }
                filmTitle?.text = title
                filmRating?.text = filmItem.rating
                loadImage(filmImg, filmItem.posterPath)
                loadImage(filmBack, filmItem.posterPath)
            }
            circle_progress_bar.visibility = INVISIBLE
        })
    }

    private fun setPosterRoundImgAnimation(verticalOffset: Int, currentOffset: Int,
                                   filmImg: ImageView, detailAppbar: AppBarLayout): Int {
        var dif = 0F
        val expandedImgSize = resources.getDimension(R.dimen.default_expanded_image_size)
        val offsetDif = ((-1) * verticalOffset.toFloat()/detailAppbar.height)
        if (currentOffset + verticalOffset > currentOffset) {
            dif = expandedImgSize + offsetDif * expandedImgSize
        } else if (currentOffset + verticalOffset < currentOffset) {
            dif = expandedImgSize - offsetDif * expandedImgSize
        }

        if (verticalOffset != 0) {
            val animationScaleX = ofFloat(filmImg, View.SCALE_X, dif/expandedImgSize)
            val animationScaleY = ofFloat(filmImg, View.SCALE_Y, dif/expandedImgSize)

            val animatorTranslateXToRight = ofFloat(filmImg,
                    TRANSLATION_X, 0F, detailAppbar.width.toFloat() / 2 - 130).apply {
                    duration = 300
                }

            val animatorTranslateXToCenter = ofFloat(filmImg,
                    TRANSLATION_X, detailAppbar.width.toFloat() / 2 - 130, 0F).apply {
                    duration = 300
                }

            AnimatorSet().apply {
                play(animationScaleX)
                play(animationScaleY)
                if (filmImg.translationX != 0F)
                    play(animatorTranslateXToCenter)
                if (offsetDif == resources.getInteger(R.integer.maxCollapsing).toFloat()/100)
                    play(animatorTranslateXToRight)
                start()
            }
        }
        return verticalOffset
    }

    interface OnFilmDetailsAction: OnFragmentAction {
        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}