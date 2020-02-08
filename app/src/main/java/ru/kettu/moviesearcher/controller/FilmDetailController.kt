package ru.kettu.moviesearcher.controller

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment

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