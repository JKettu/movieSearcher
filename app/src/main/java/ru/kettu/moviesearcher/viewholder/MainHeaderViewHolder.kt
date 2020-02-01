package ru.kettu.moviesearcher.viewholder

import android.view.View
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main_header.view.*
import ru.kettu.moviesearcher.activity.fragment.MainFragment

class MainHeaderViewHolder(itemOfRecycler: View, val isNightModeOn: Boolean): RecyclerView.ViewHolder(itemOfRecycler) {

    fun bind(listeners: MainFragment.OnMainFragmentAction?) {
        itemView.mode.isChecked = isNightModeOn
        itemView.inviteFriend.setOnClickListener {
            listeners?.onPressInviteBtn()
        }
        itemView.mode.setOnClickListener {
            listeners?.onModeSwitch(it as Switch)
        }
        itemView.favouritesIcon.setOnClickListener {
            listeners?.onPressFavouritesIcon()
        }
    }
}