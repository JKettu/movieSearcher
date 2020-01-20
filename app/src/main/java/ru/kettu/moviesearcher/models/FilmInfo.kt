package ru.kettu.moviesearcher.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmInfo (val filmDescriptionId: Int, val filmPosterId: Int)
    : Parcelable {
}