package ru.kettu.moviesearcher.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.operations.*


class FilmViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val filmName: TextView = itemOfRecycler.filmName
    val poster: ImageView = itemOfRecycler.poster
    val detailBtn: Button = itemOfRecycler.detailBtn

    fun bind(nameId: Int, posterId: Int, position: Int) {
        val activity = getActivity(itemView)
        val filmNameText = activity?.getString(nameId)
            itemView.setOnLongClickListener {
            run {
                if (activity is MainActivity) {
                    val filmInfo = activity.getFilmInfoByFilmName(filmNameText)
                    filmInfo?.let {
                        val favourite = FavouriteItem(posterId, nameId)
                        activity.favourites.add(favourite)
                        val toast = makeText(activity, R.string.addToFavourite, LENGTH_SHORT)
                        toast.show()
                    }
                }
                true
            }
        }
        filmName.text = filmNameText
        activity?.let {
            if (activity is MainActivity) {
                if (activity.selectedSpan == position) {
                    filmName.setSelectedTextColor()
                    activity.selectedText = filmName
                }
            }
        }
        poster.setImageResource(posterId)
        detailBtn.setOnClickListener {
            run {
                val activity = getActivity(itemView)
                if (activity is MainActivity) {
                    filmName.setSelectedTextColor()
                    if (activity.selectedText != null)
                        activity.selectedText!!.setDefaultTextColor()

                    activity.selectedSpan = this.layoutPosition
                    activity.selectedText = filmName
                    activity.onDetailsBtnClick(filmName)
                }
            }
        }
    }

    private fun MainActivity.onDetailsBtnClick(view: View?) {
        if (view == null || view !is TextView) return
        val requestCode = MainActivity.FILM_DETAILS_INFO_REQUEST_CODE
        val extraName = MainActivity.FILM_INFO
        when (view.text) {
            resources.getString(R.string.dogsPurposeFilm) ->
                openFilmDescriptionActivity(
                    R.string.dogsPurposeDesc,
                    R.drawable.dogs_purpose,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.harryPotter1Film) ->
                openFilmDescriptionActivity(
                    R.string.harryPotterDesc,
                    R.drawable.harry_potter_1,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.sweeneyToddFilm) ->
                openFilmDescriptionActivity(
                    R.string.sweeneyToddDesc,
                    R.drawable.sweeney_todd,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.ageOfAdalineFilm) ->
                openFilmDescriptionActivity(
                    R.string.ageOfAdalineDesc,
                    R.drawable.age_of_adaline,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.theAvengersFilm) ->
                openFilmDescriptionActivity(
                    R.string.theAvengersDesc,
                    R.drawable.the_avengers,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.morningGloryFilm) ->
                openFilmDescriptionActivity(
                    R.string.morningGloryDesc,
                    R.drawable.morning_glory,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.theLordOfTheRings1Film) ->
                openFilmDescriptionActivity(
                    R.string.theLordOfTheRings1Desc,
                    R.drawable.the_lord_of_the_rings1,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.treasurePlanetFilm) ->
                openFilmDescriptionActivity(
                    R.string.treasurePlanetDesc,
                    R.drawable.treasure_planet,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.cocoFilm) ->
                openFilmDescriptionActivity(
                    R.string.cocoDesc,
                    R.drawable.coco,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.klausFilm) ->
                openFilmDescriptionActivity(
                    R.string.klausDesc,
                    R.drawable.klaus,
                    requestCode,
                    extraName
                )
            resources.getString(R.string.theNightmareBeforeChristmasFilm) ->
                openFilmDescriptionActivity(
                    R.string.theNightmareBeforeChristmasDesc,
                    R.drawable.the_nightmare_before_christmas,
                    requestCode,
                    extraName
                )
        }
    }
}