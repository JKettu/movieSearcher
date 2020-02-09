package ru.kettu.moviesearcher.controller

import android.app.Activity
import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import ru.kettu.moviesearcher.R
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

fun Resources.initNotInFavourites(allFilms: HashSet<FilmItem>?,
                                  films: TreeSet<FilmItem>?, notInFilms: TreeSet<FilmItem>) {
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

fun loadImage(imageView: ImageView, fullImagePath: String) {
    Glide.with(imageView.context)
        .load(fullImagePath)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(imageView)
}
