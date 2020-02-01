package ru.kettu.moviesearcher.viewholder

import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.kettu.moviesearcher.activity.fragment.MainFragment


class FilmViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val filmName: TextView = itemOfRecycler.filmName
    val poster: ImageView = itemOfRecycler.poster
    val detailBtn: Button = itemOfRecycler.detailBtn

    fun bind(nameId: Int, posterId: Int, position: Int, res: Resources, listener: MainFragment.OnMainFragmentAction?) {
            itemView.setOnLongClickListener {
            run {
                listener?.onAddToFavourites(posterId, nameId)
                true
            }
        }
        this.filmName.text = res.getString(nameId)
        listener?.onRestoreMarkedFilmName(this.filmName, position)
        poster.setImageResource(posterId)
        detailBtn.setOnClickListener {
            listener?.onDetailsBtnPressed(this.filmName, layoutPosition)
        }
    }
}