package ru.kettu.moviesearcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.viewholder.FilmViewHolder
import ru.kettu.moviesearcher.viewholder.MainHeaderViewHolder

class MainActivityAdapter (val inflater: LayoutInflater, val items: List<FilmItem>, val isNightModeOn: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FILMS ->
                FilmViewHolder(inflater.inflate(R.layout.item_film, parent, false))
            else ->
                MainHeaderViewHolder(inflater.inflate(R.layout.item_main_header, parent, false), isNightModeOn)
        }
    }

    override fun getItemCount() = items.size + 1 //1 - header

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilmViewHolder) {
            val currentItem = items[position - 1]
            currentItem?.let {
                holder.bind(it.filmName, it.posterId, position, it.isInFavourite)
            }
        } else if (holder is MainHeaderViewHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_FILMS
        }
    }

    companion object {
        const val VIEW_TYPE_FILMS = 1
        const val VIEW_TYPE_HEADER = 0
    }

}