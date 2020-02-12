package ru.kettu.moviesearcher.view.recyclerview.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.view.recyclerview.viewholder.FilmViewHolder
import ru.kettu.moviesearcher.view.recyclerview.viewholder.MainSectionViewHolder


class FilmListAdapter (val inflater: LayoutInflater, val items: Set<FilmItem>, val listener: OnMainFragmentAction?, val res: Resources)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FILMS ->
                FilmViewHolder(
                    inflater.inflate(
                        R.layout.item_film,
                        parent,
                        false
                    )
                )
            else ->
                MainSectionViewHolder(
                    inflater.inflate(
                        R.layout.item_section,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemCount() = items.size + 1 //1 - header

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilmViewHolder) {
            val currentItem = items.toList()[position - 1]
            currentItem?.let {
                holder.bind(it, position, listener)
            }
        } else if (holder is MainSectionViewHolder) {
            holder.bind(res.getString(R.string.filmList))
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