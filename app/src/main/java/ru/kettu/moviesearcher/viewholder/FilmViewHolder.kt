package ru.kettu.moviesearcher.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.operations.getActivity
import ru.kettu.moviesearcher.operations.openFilmDescriptionActivity
import ru.kettu.moviesearcher.operations.setDefaultTextColor
import ru.kettu.moviesearcher.operations.setSelectedTextColor


class FilmViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout) {
    val filmName: TextView = itemLayout.filmName
    val poster: ImageView = itemLayout.poster
    val detailBtn: Button = itemLayout.detailBtn

    fun bind(name: String, posterId: Int, position: Int) {
        filmName.text = name
        var activity = getActivity(itemView)
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
                var activity = getActivity(itemView)
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

    private fun MainActivity.onDetailsBtnClick(view: View?){
        if (view == null || view !is TextView) return
        val requestCode = MainActivity.FILM_DETAILS_INFO_REQUEST_CODE
        val extraName = MainActivity.FILM_INFO
        when(view.text) {
            resources.getString(R.string.dogsPurposeFilm) ->
                openFilmDescriptionActivity(R.string.dogsPurposeDesc, R.drawable.dogs_purpose, requestCode, extraName)
            resources.getString(R.string.harryPotterFilm) ->
                openFilmDescriptionActivity(R.string.harryPotterDesc, R.drawable.harry_potter_1, requestCode, extraName)
            resources.getString(R.string.sweeneyToddFilm) ->
                openFilmDescriptionActivity(R.string.sweeneyToddDesc, R.drawable.sweeney_todd, requestCode, extraName)
            resources.getString(R.string.ageOfAdalineFilm) ->
                openFilmDescriptionActivity(R.string.ageOfAdalineDesc, R.drawable.age_of_adaline, requestCode, extraName)
        }
    }
}