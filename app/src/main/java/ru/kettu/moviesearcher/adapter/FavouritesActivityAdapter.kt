package ru.kettu.moviesearcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.viewholder.FavouriteViewHolder
import java.util.*

class FavouritesActivityAdapter (val inflater: LayoutInflater, val items: TreeSet<FavouriteItem>)
    : RecyclerView.Adapter<FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(inflater.inflate(R.layout.item_favourite, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        if (items.size == 0 ) return holder.bind(null, -1)
        val filmInfo = items.elementAt(position)
        return holder.bind(filmInfo.filmPosterId, filmInfo.filmNameId)
    }
}