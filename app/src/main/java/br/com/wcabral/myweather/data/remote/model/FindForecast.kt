package br.com.wcabral.myweather.data.remote.model

import com.google.gson.annotations.SerializedName

data class FindForecast(
    @SerializedName("cod")
    var cod: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("list")
    var cities: List<City>
)