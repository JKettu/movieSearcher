package ru.kettu.moviesearcher.models.network

import com.google.gson.annotations.SerializedName
import ru.kettu.moviesearcher.constants.QueryParamsConstants.NAME
import ru.kettu.moviesearcher.constants.QueryParamsConstants.PRODUCTION_COUNTRY_ID

data class ProductionCountries (@SerializedName(NAME) var name: String,
                                @SerializedName(PRODUCTION_COUNTRY_ID) var countryId: String) {
}