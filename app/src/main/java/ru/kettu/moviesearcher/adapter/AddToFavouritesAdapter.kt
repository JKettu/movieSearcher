package ru.kettu.moviesearcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.viewholder.AddFavouriteViewHolder
import java.util.*

//How to do through FavouritesActivityAdapter?
class AddToFavouritesAdapter(val inflater: LayoutInflater, val addItems: TreeSet<FavouriteInfo>):
    RecyclerView.Adapter<AddFavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFavouriteViewHolder {
        return AddFavouriteViewHolder(inflater.inflate(R.layout.item_add_to_favourite, parent, false))
    }

    override fun getItemCount() = addItems.size

    override fun onBindViewHolder(holder: AddFavouriteViewHolder, position: Int) {
        if (addItems.size == 0 ) return holder.bind(null, null)
        val filmInfo = addItems.elementAt(position)
        return holder.bind(filmInfo.filmPosterId, filmInfo.filmName)
    }
}