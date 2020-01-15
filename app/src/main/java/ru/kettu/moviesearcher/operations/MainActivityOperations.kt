package ru.kettu.moviesearcher.operations

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity



fun TextView.setTextViewColor(bundle: Bundle, id: String) {
    this.setTextColor(getColorFromBundleById(bundle, id))
}

fun getColorFromBundleById(bundle: Bundle, id: String): Int {
    return if (bundle.getInt(id) == 0) R.color.colorAccent else bundle.getInt(id)
}

fun TextView.setDefaultTextColor(){
    var colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}