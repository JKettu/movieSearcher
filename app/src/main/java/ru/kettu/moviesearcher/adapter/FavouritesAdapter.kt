package ru.kettu.moviesearcher.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.viewholder.FavouriteViewHolder
import java.util.*

class FavouritesAdapter (val inflater: LayoutInflater, val items: TreeSet<FilmItem>,
                         val listener: OnFavouritesFragmentAction?, val res: Resources)
    : RecyclerView.Adapter<FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(inflater.inflate(R.layout.item_favourite, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        if (items.size == 0 ) return holder.bind(null, -1,null, listener)
        val filmInfo = items.elementAt(position)
        return holder.bind(filmInfo.filmPosterId, filmInfo.filmNameId, res, listener)
    }
}