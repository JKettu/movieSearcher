package ru.kettu.moviesearcher.controller

import android.app.Activity
import android.content.res.Resources
import android.text.TextUtils.isEmpty
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

fun TextView.setDefaultTextColor() {
    val colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}

fun TextView.setSelectedTextColor() {
    val colorAccentDark = resources.getColor(R.color.colorAccentDark)
    this.setTextColor(colorAccentDark)
}

fun Resources.initNotInFavourites(allFilms: LinkedHashSet<FilmItem>?,
                                  films: LinkedHashSet<FilmItem>?, notInFilms: LinkedHashSet<FilmItem>) {
    if (films == null) return
    allFilms?.forEach { film ->
        run {
            if (!films.contains(film))
                notInFilms.add(film)
        }
    }
}

fun onDayNightModeSwitch(mode: SwitchCompat, activity: Activity) {
    if (mode.isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    ActivityCompat.recreate(activity)
}

fun loadImage(imageView: ImageView, fullImagePath: String?) {
    if (isEmpty(fullImagePath) || POSTER_PREFIX.equals(fullImagePath)) return
    Glide.with(imageView.context)
        .load(fullImagePath)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(imageView)
}


fun FragmentManager.loadFragmentWithoutBackStack(fragmentId: Int, fragment: Fragment, name: String) {
    this.beginTransaction()
        .replace(fragmentId, fragment, name)
        .commit()
}

fun FragmentManager.loadFragmentWithBackStack(fragmentId: Int, fragment: Fragment, name: String) {
    this.beginTransaction()
        .replace(fragmentId, fragment, name)
        .addToBackStack(name)
        .commit()
}
