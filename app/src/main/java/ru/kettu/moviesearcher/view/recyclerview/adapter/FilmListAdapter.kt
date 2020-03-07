package ru.kettu.moviesearcher.view.recyclerview.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.view.fragment.OnFragmentAction
import ru.kettu.moviesearcher.view.recyclerview.viewholder.FilmViewHolder


class FilmListAdapter (val inflater: LayoutInflater, val items: Set<FilmItem>, val listener: OnFragmentAction?, val res: Resources)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(inflater.inflate(R.layout.item_film, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilmViewHolder) {
            val currentItem = items.toList()[position]
            currentItem?.let {
                holder.bind(it, listener as OnMainFragmentAction)
            }
        }
    }
}