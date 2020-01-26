package ru.kettu.moviesearcher.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.item_favourite.view.*
import ru.kettu.moviesearcher.activity.FavouritesActivity
import ru.kettu.moviesearcher.operations.getActivity

class FavouriteViewHolder(itemOfRecycler: View) : RecyclerView.ViewHolder(itemOfRecycler) {
    val poster: ImageView = itemOfRecycler.posterFav
    val filmName: TextView = itemOfRecycler.filmNameFav
    val deleteBtn: Button = itemOfRecycler.deleteFav

    fun bind(posterId: Int?, text: String?) {
        if (posterId == null || text == null) return
        poster.setImageResource(posterId)
        filmName.text = text
        deleteBtn.setOnClickListener {
            val activity = getActivity(itemView)
            if (activity is FavouritesActivity) {
                val elementToRemove = activity.favourites.elementAt(layoutPosition)
                activity.favourites.remove(elementToRemove)
                activity.recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
            }
        }
    }
}