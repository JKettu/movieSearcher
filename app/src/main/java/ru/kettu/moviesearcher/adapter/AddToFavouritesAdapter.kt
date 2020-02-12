package ru.kettu.moviesearcher.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.viewholder.AddFavouriteViewHolder
import java.util.*

class AddToFavouritesAdapter(val inflater: LayoutInflater, val addItems: TreeSet<FilmItem>,
                             val listener: OnFavouritesFragmentAction?, val res: Resources):
    RecyclerView.Adapter<AddFavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFavouriteViewHolder {
        return AddFavouriteViewHolder(inflater.inflate(R.layout.item_add_to_favourite, parent, false))
    }

    override fun getItemCount() = addItems.size

    override fun onBindViewHolder(holder: AddFavouriteViewHolder, position: Int) {
        if (addItems.size == 0 ) return holder.bind(null, -1, null, listener)
        val filmInfo = addItems.elementAt(position)
        return holder.bind(filmInfo.filmPosterId, filmInfo.filmNameId, res, listener)
    }
}