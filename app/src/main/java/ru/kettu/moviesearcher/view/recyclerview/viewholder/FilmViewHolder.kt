package ru.kettu.moviesearcher.view.recyclerview.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment


class FilmViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val filmName: TextView = itemOfRecycler.filmName
    val poster: ImageView = itemOfRecycler.poster
    val detailBtn: Button = itemOfRecycler.detailBtn
    val filmRate: TextView = itemOfRecycler.filmRate

    fun bind(item: FilmItem, listener: MainFilmListFragment.OnMainFragmentAction?) {
        itemView.setOnLongClickListener {
            run {
                listener?.onAddToFavourites(item)
                true
            }
        }
        this.filmName.text = item.title
        this.filmRate.text = item.rating
        loadImage(poster, item.posterPath)
        if (item.isSelected)
            filmName.apply {
                val colorAccentDark = resources.getColor(R.color.colorAccentDark)
                this.setTextColor(colorAccentDark)
            }
        detailBtn.setOnClickListener {
            listener?.onDetailsBtnPressed(this.filmName, item, layoutPosition)
        }
    }
}