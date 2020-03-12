package ru.kettu.moviesearcher.models.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"])])
data class Country (@PrimaryKey (autoGenerate = true) var id: Long,
               var name: String?) {

}