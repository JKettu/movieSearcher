package ru.kettu.moviesearcher.models.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmItem (val filmNameId: Int, val posterId: Int, val isInFavourite : Boolean = false): Parcelable {
}