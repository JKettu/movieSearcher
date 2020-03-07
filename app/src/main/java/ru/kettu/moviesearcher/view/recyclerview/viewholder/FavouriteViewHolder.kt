package ru.kettu.moviesearcher.view.recyclerview.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_favourite.view.*
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment.OnFavouritesFragmentAction

class FavouriteViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val poster: ImageView = itemOfRecycler.posterFav
    val filmName: TextView = itemOfRecycler.filmNameFav
    val deleteBtn: Button = itemOfRecycler.deleteFav

    fun bind(filmItem: FilmItem, listener: OnFavouritesFragmentAction?) {
        loadImage(poster, filmItem.posterPath)
        this.filmName.text = filmItem.title
        deleteBtn.setOnClickListener {
            listener?.onDeleteFilm(layoutPosition, filmItem)
        }
    }
}