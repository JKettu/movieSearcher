package ru.kettu.moviesearcher.viewholder

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_add_to_favourite.view.*
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.models.item.FilmItem

class AddFavouriteViewHolder(itemOfRecycler: View): RecyclerView.ViewHolder(itemOfRecycler) {
    val poster = itemOfRecycler.addToFavPoster
    var filmName = itemOfRecycler.addToFavFilmName

    fun bind(posterId: Int?, filmNameId: Int, res: Resources?, listener: OnFavouritesFragmentAction?) {
        if (posterId == null || filmNameId == -1) return
        poster.setImageResource(posterId)
        this.filmName.text = res?.getString(filmNameId)
        itemView.setOnClickListener {
            listener?.onAddFilm(FilmItem(posterId, filmNameId))
        }
    }
}