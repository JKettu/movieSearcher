package ru.kettu.moviesearcher.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_add_to_favourite.view.*
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.controller.loadImage
import ru.kettu.moviesearcher.models.item.FilmItem

class AddFavouriteViewHolder(itemOfRecycler: View): RecyclerView.ViewHolder(itemOfRecycler) {
    val poster = itemOfRecycler.addToFavPoster
    var filmName = itemOfRecycler.addToFavFilmName

    fun bind(filmItem: FilmItem, listener: OnFavouritesFragmentAction?) {
        loadImage(poster, filmItem.posterPath)
        filmName.text = filmItem.title
        itemView.setOnClickListener {
            listener?.onAddFilm(filmItem)
        }
    }
}