package ru.kettu.moviesearcher.models.network

import com.google.gson.annotations.SerializedName
import ru.kettu.moviesearcher.constants.QueryParamsConstants.PAGE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.RESULTS
import ru.kettu.moviesearcher.constants.QueryParamsConstants.TOTAL_PAGES
import ru.kettu.moviesearcher.constants.QueryParamsConstants.TOTAL_RESULTS

data class FilmListResponse (@SerializedName(PAGE) var page: Int,
                             @SerializedName(TOTAL_RESULTS) var totalResults: Int,
                             @SerializedName(TOTAL_PAGES) var totalPages: Int,
                             @SerializedName(RESULTS) var results: List<FilmDetails>) {

}