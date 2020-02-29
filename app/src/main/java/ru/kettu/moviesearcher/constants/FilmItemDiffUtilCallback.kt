package ru.kettu.moviesearcher.constants

import androidx.recyclerview.widget.DiffUtil
import ru.kettu.moviesearcher.models.item.FilmItem

class FilmItemDiffUtilCallback (val oldSet: LinkedHashSet<FilmItem>, val newSet: LinkedHashSet<FilmItem>)
    : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: FilmItem = oldSet.elementAt(oldItemPosition)
        val newItem: FilmItem = newSet.elementAt(newItemPosition)
        return oldItem.id == newItem.id
    }

    override fun getOldListSize(): Int {
        return oldSet.size
    }

    override fun getNewListSize(): Int {
        return newSet.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: FilmItem = oldSet.elementAt(oldItemPosition)
        val newItem: FilmItem = newSet.elementAt(newItemPosition)
        return oldItem.equals(newItem)
    }
}