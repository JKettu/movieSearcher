package ru.kettu.moviesearcher.viewholder

import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_favourite.view.*
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.models.item.FilmItem

class FavouriteViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val poster: ImageView = itemOfRecycler.posterFav
    val filmName: TextView = itemOfRecycler.filmNameFav
    val deleteBtn: Button = itemOfRecycler.deleteFav

    fun bind(posterId: Int?, filmNameId: Int, res: Resources?, listener: OnFavouritesFragmentAction?) {
        if (posterId == null || filmNameId == -1) return
        poster.setImageResource(posterId)
        this.filmName.text = res?.getString(filmNameId)
        deleteBtn.setOnClickListener {
            listener?.onDeleteFilm(layoutPosition, FilmItem(posterId, filmNameId))
        }
    }
}