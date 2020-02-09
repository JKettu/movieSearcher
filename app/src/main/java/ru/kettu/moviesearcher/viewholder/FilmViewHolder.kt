package ru.kettu.moviesearcher.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.activity.fragment.MainFilmListFragment
import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.network.FilmDetails


class FilmViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val filmName: TextView = itemOfRecycler.filmName
    val poster: ImageView = itemOfRecycler.poster
    val detailBtn: Button = itemOfRecycler.detailBtn

    fun bind(details: FilmDetails, position: Int, listener: MainFilmListFragment.OnMainFragmentAction?) {
        itemView.setOnLongClickListener {
            run {
                listener?.onAddToFavourites(details)
                true
            }
        }
        this.filmName.text = details.title
        loadImage(poster, POSTER_PREFIX + details.posterPath)
        listener?.onRestoreMarkedFilmName(this.filmName, position)
        detailBtn.setOnClickListener {
            listener?.onDetailsBtnPressed(this.filmName, details, layoutPosition)
        }
    }
}