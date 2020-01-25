package ru.kettu.moviesearcher.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main_header.view.*

class MainHeaderViewHolder(itemLayout: View, val isNightModeOn: Boolean): RecyclerView.ViewHolder(itemLayout) {

    fun bind() {
        itemView.mode.isChecked = isNightModeOn
    }
}