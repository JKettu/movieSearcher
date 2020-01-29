package ru.kettu.moviesearcher.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.item_add_to_favourite.view.*
import ru.kettu.moviesearcher.activity.FavouritesActivity
import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.operations.getActivity

class AddFavouriteViewHolder(itemOfRecycler: View): RecyclerView.ViewHolder(itemOfRecycler) {
    val poster = itemOfRecycler.addToFavPoster
    var filmName = itemOfRecycler.addToFavFilmName

    fun bind(posterId: Int?, filmNameText: String?) {
        if (posterId == null || filmNameText == null) return
        poster.setImageResource(posterId)
        filmName.text = filmNameText
        itemView.setOnClickListener {
            val activity = getActivity(itemView)
            if (activity is FavouritesActivity) {
                val newFav = FavouriteInfo(posterId, filmNameText)
                activity.favourites.add(newFav)
                val elemPosition = activity.notInFavourites.indexOf(newFav)
                activity.notInFavourites.remove(newFav)
                activity.recycleViewFav.adapter?.notifyItemInserted(activity.favourites.indexOf(newFav))
                activity.filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
            }
        }
    }
}