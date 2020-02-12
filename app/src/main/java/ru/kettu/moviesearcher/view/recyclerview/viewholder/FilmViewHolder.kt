package ru.kettu.moviesearcher.view.recyclerview.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment


class FilmViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val filmName: TextView = itemOfRecycler.filmName
    val poster: ImageView = itemOfRecycler.poster
    val detailBtn: Button = itemOfRecycler.detailBtn

    fun bind(item: FilmItem, position: Int, listener: MainFilmListFragment.OnMainFragmentAction?) {
        itemView.setOnLongClickListener {
            run {
                listener?.onAddToFavourites(item)
                true
            }
        }
        this.filmName.text = item.title
        loadImage(poster, item.posterPath)
        listener?.onRestoreMarkedFilmName(this.filmName, position)
        detailBtn.setOnClickListener {
            listener?.onDetailsBtnPressed(this.filmName, item, layoutPosition)
        }
    }
}