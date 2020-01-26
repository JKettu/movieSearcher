package ru.kettu.moviesearcher.operations

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity
import ru.kettu.moviesearcher.models.FilmInfo

fun getActivity(view: View): Activity? {
    var context: Context? = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Activity.openFilmDescriptionActivity(descriptionId: Int, posterId: Int, reqCode: Int, extraName: String) {
    val intent = Intent(this, FilmDetailActivity::class.java)
    intent.putExtra(extraName, FilmInfo(descriptionId, posterId, -1))
    this.startActivityForResult(intent, reqCode)
}

fun Activity.getFilmInfoByFilmName(filmName: String): FilmInfo? {
    return when (filmName) {
        resources.getString(R.string.harryPotter1Film) ->
            FilmInfo(R.string.harryPotterDesc, R.drawable.harry_potter_1, R.string.harryPotter1Film)
        resources.getString(R.string.sweeneyToddFilm) ->
            FilmInfo(R.string.sweeneyToddDesc, R.drawable.sweeney_todd, R.string.sweeneyToddFilm)
        resources.getString(R.string.dogsPurposeFilm) ->
            FilmInfo(R.string.dogsPurposeDesc, R.drawable.dogs_purpose, R.string.dogsPurposeFilm)
        resources.getString(R.string.ageOfAdalineFilm) ->
            FilmInfo(R.string.ageOfAdalineDesc, R.drawable.age_of_adaline, R.string.ageOfAdalineFilm)
        resources.getString(R.string.theAvengersFilm) ->
            FilmInfo(R.string.theAvengersDesc, R.drawable.the_avengers, R.string.theAvengersFilm)
        resources.getString(R.string.morningGloryFilm) ->
            FilmInfo(R.string.morningGloryDesc, R.drawable.morning_glory, R.string.morningGloryFilm)
        resources.getString(R.string.theLordOfTheRings1Film) ->
            FilmInfo(R.string.theLordOfTheRings1Desc, R.drawable.the_lord_of_the_rings1, R.string.theLordOfTheRings1Film)
        resources.getString(R.string.treasurePlanetFilm) ->
            FilmInfo(R.string.treasurePlanetDesc, R.drawable.treasure_planet, R.string.treasurePlanetFilm)
        resources.getString(R.string.cocoFilm) ->
            FilmInfo(R.string.cocoFilm, R.drawable.coco, R.string.cocoDesc)
        resources.getString(R.string.klausFilm) ->
            FilmInfo(R.string.klausDesc, R.drawable.klaus, R.string.klausFilm)
        resources.getString(R.string.theNightmareBeforeChristmasFilm) ->
            FilmInfo(R.string.theNightmareBeforeChristmasDesc, R.drawable.the_nightmare_before_christmas, R.string.theNightmareBeforeChristmasFilm)
        else -> null
    }
}