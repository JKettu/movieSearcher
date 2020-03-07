package ru.kettu.moviesearcher.view.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.view.recyclerview.viewholder.FavouriteViewHolder
import java.util.*

class FavouritesAdapter (val inflater: LayoutInflater, val items: LinkedHashSet<FilmItem>,
                         val listener: OnFavouritesFragmentAction?)
    : RecyclerView.Adapter<FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(inflater.inflate(R.layout.item_favourite, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        if (items.size == 0 ) return
        val filmInfo = items.elementAt(position)
        return holder.bind(filmInfo, listener)
    }
}