package ru.kettu.moviesearcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.viewholder.FavouriteViewHolder

class FavouritesActivityAdapter (val inflater: LayoutInflater, val items: HashSet<FavouriteInfo>): RecyclerView.Adapter<FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(inflater.inflate(R.layout.item_favourite, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        if (items.size == 0) return holder.bind(null, null)
        val filmInfo = items.elementAt(position)
        return holder.bind(filmInfo.filmPosterId, filmInfo.filmName)
    }
}