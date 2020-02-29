package ru.kettu.moviesearcher.models.network

import com.google.gson.annotations.SerializedName
import ru.kettu.moviesearcher.constants.QueryParamsConstants.ID
import ru.kettu.moviesearcher.constants.QueryParamsConstants.NAME
import java.io.Serializable

data class Genres (@SerializedName(ID) var id: Int,
                   @SerializedName(NAME) var name: String?): Serializable {
}