package ru.kettu.moviesearcher.models.network

import com.google.gson.annotations.SerializedName
import ru.kettu.moviesearcher.constants.QueryParamsConstants.ADULT
import ru.kettu.moviesearcher.constants.QueryParamsConstants.BACKDROP_PATH
import ru.kettu.moviesearcher.constants.QueryParamsConstants.GENRES
import ru.kettu.moviesearcher.constants.QueryParamsConstants.GENRE_IDS
import ru.kettu.moviesearcher.constants.QueryParamsConstants.ID
import ru.kettu.moviesearcher.constants.QueryParamsConstants.ORIGINAL_LANGUAGE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.ORIGINAL_TITLE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.OVERVIEW
import ru.kettu.moviesearcher.constants.QueryParamsConstants.POPULARITY
import ru.kettu.moviesearcher.constants.QueryParamsConstants.POSTER_PATH
import ru.kettu.moviesearcher.constants.QueryParamsConstants.PRODUCTION_COUNTRIES
import ru.kettu.moviesearcher.constants.QueryParamsConstants.RELEASE_DATE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.TAGLINE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.TITLE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.VOTE_AVERAGE
import ru.kettu.moviesearcher.constants.QueryParamsConstants.VOTE_COUNT

data class FilmDetails (@SerializedName(POPULARITY) var popularity: Double,
                        @SerializedName(VOTE_COUNT) var voteCount: Int,
                        @SerializedName(POSTER_PATH) var posterPath : String,
                        @SerializedName(ID) var id: Int,
                        @SerializedName(ADULT) var adult: Boolean,
                        @SerializedName(BACKDROP_PATH) var backdropPath: String,
                        @SerializedName(ORIGINAL_LANGUAGE) var originalLang: String,
                        @SerializedName(ORIGINAL_TITLE) var originalTitle: String,
                        @SerializedName(GENRES) var genres: List<Genres>,
                        @SerializedName(GENRE_IDS) var genreIds: List<Int>,
                        @SerializedName(TITLE) var title: String,
                        @SerializedName(VOTE_AVERAGE) var voteAverage: Double,
                        @SerializedName(OVERVIEW) var overview: String,
                        @SerializedName(RELEASE_DATE) var releaseDate: String,
                        @SerializedName(TAGLINE) var tagline: String,
                        @SerializedName(PRODUCTION_COUNTRIES) var productionCountries: List<ProductionCountries>) {
}