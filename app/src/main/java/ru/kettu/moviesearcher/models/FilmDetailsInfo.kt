package ru.kettu.moviesearcher.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmDetailsInfo (val isLiked: Boolean, val comment: String)
    : Parcelable {

}