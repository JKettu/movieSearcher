package ru.kettu.moviesearcher.operations

import android.widget.TextView
import ru.kettu.moviesearcher.R

fun TextView.setDefaultTextColor(){
    var colorAccent = resources.getColor(R.color.colorAccent)
    this.setTextColor(colorAccent)
}