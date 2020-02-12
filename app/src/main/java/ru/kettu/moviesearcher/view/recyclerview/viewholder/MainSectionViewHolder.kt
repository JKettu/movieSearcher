package ru.kettu.moviesearcher.view.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_section.view.*

class MainSectionViewHolder(itemOfRecycler: View): RecyclerView.ViewHolder(itemOfRecycler) {

    val title = itemView.sectionTitle

    fun bind(sectionTitle: String) {
        title.text = sectionTitle
    }
}