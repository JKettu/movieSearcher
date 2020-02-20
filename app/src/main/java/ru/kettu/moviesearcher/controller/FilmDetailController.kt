package ru.kettu.moviesearcher.controller

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.util.Log
import android.view.View.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_film_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.Constants.COMMA_AND_SPACE
import ru.kettu.moviesearcher.constants.Constants.DASH
import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.models.network.Genres
import ru.kettu.moviesearcher.network.RetrofitApp
import ru.kettu.moviesearcher.view.fragment.FilmDetailsFragment


fun FilmDetailsFragment.setPosterRoundImgAnimation(verticalOffset: Int, currentOffset: Int): Int {
    val fragment = this
    var dif = 0F
    val expandedImgSize = resources.getDimension(R.dimen.default_expanded_image_size)
    val offsetDif = ((-1) * verticalOffset.toFloat()/fragment.detailAppbar.height)
    if (currentOffset + verticalOffset > currentOffset) {
        dif = expandedImgSize + offsetDif * expandedImgSize
    } else if (currentOffset + verticalOffset < currentOffset) {
        dif = expandedImgSize - offsetDif * expandedImgSize
    }

    if (verticalOffset != 0) {
        val animationScaleX =
            ObjectAnimator.ofFloat(fragment.filmImg, SCALE_X, dif/expandedImgSize)
        val animationScaleY =
            ObjectAnimator.ofFloat(fragment.filmImg, SCALE_Y, dif/expandedImgSize)

        val animatorTranslateXToRight =
            ObjectAnimator.ofFloat(fragment.filmImg,
                TRANSLATION_X, 0F, fragment.detailAppbar.width.toFloat() / 2 - 130).apply {
                duration = 300
            }

        val animatorTranslateXToCenter =
            ObjectAnimator.ofFloat(fragment.filmImg,
                TRANSLATION_X, fragment.detailAppbar.width.toFloat() / 2 - 130, 0F).apply {
                duration = 300
            }

        AnimatorSet().apply {
            play(animationScaleX)
            play(animationScaleY)
            if (fragment.filmImg.translationX != 0F)
                play(animatorTranslateXToCenter)
            if (offsetDif == resources.getInteger(R.integer.maxCollapsing).toFloat()/100)
                play(animatorTranslateXToRight)
            start()
        }
    }
    return verticalOffset
}

fun initFilmDetailLoading(filmId: Int, fragment: FilmDetailsFragment) {
    val resources = fragment.resources
    val movieDbApi = RetrofitApp.theMovieDbApi
    val call = movieDbApi?.getFilmDetails(filmId, resources.configuration.locale.language)
    call?.enqueue(object : Callback<FilmDetails> {
        override fun onResponse(call: Call<FilmDetails>, response: Response<FilmDetails>) {
            try {
                val film = response.body()
                film?.let {
                    val releaseDate = film.releaseDate.substringBefore(DASH)
                    val title = "${film.title} ($releaseDate)"
                    fragment.filmDesc.text = film.overview
                    fragment.filmTitle.text = title
                    fragment.filmGenres.text = resources.convertGenresListToString(film.genres)
                    fragment.filmRating.text = film.voteAverage.toString()
                    loadImage(fragment.filmImg, POSTER_PREFIX + film.posterPath)
                    loadImage(fragment.filmBack, POSTER_PREFIX + film.posterPath)
                }
                fragment.circle_progress_bar.visibility = INVISIBLE
            } catch (exception: Throwable) {
                Log.e("Details:loadFilm", exception.localizedMessage, exception)
            }
        }

        override fun onFailure(call: Call<FilmDetails>, t: Throwable) {
            try {
                Log.e("Details:loadFilm",t.localizedMessage, t)
                fragment.circle_progress_bar.visibility = INVISIBLE
                Toast.makeText(fragment.view?.context, R.string.filmLoadingFailed, Toast.LENGTH_SHORT).show()
            } catch (exception: Throwable) {
                Log.e("Details:loadFilm", exception.localizedMessage, exception)
            }
        }
    })
}

private fun Resources.convertGenresListToString (list: List<Genres>): String {
    var genres = this.getString(R.string.genres)
    list.forEachIndexed { index, elem ->
        genres += elem.name
        if (!list.lastIndex.equals(index))
            genres += COMMA_AND_SPACE
    }
    return genres
}