package ru.kettu.moviesearcher.viewholder

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main_header.view.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity
import ru.kettu.moviesearcher.operations.getActivity

class MainHeaderViewHolder(itemOfRecycler: View, val isNightModeOn: Boolean): RecyclerView.ViewHolder(itemOfRecycler) {

    fun bind() {
        itemView.mode.isChecked = isNightModeOn
        val activity = getActivity(itemView)
        itemView.inviteFriend.setOnClickListener {
            if (activity is MainActivity) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val title = activity.resources.getString(R.string.chooser)
                val chooser = Intent.createChooser(intent, title)
                intent.resolveActivity(activity.packageManager)?.let {
                    activity.startActivity(chooser)
                }
            }
        }
        itemView.mode.setOnClickListener {
            if (activity is MainActivity) {
                if (itemView.mode.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                recreate(activity)
            }

        }
    }
}