package br.com.wcabral.myweather.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FindResult(
    @SerializedName("cod")
    var cod: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("list")
    var cities: List<City>
)

data class City(
    @SerializedName("id")
    var id: Long,

    @SerializedName("name")
    var name: String,

    @SerializedName("sys")
    var country: Country,

    @SerializedName("main")
    var main: Main,

    @SerializedName("dt")
    var dt: Long,

    @SerializedName("weather")
    var weathers: List<Weather>
) : Serializable

data class Country(
    @SerializedName("country")
    var name: String
) : Serializable

data class Weather(
    @SerializedName("main")
    var main: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("icon")
    var icon: String
) : Serializable

data class Main(
    @SerializedName("temp")
    var temp: String,
    @SerializedName("feels_like")
    var feels_like: String,
    @SerializedName("temp_min")
    var temp_min: String,
    @SerializedName("temp_max")
    var temp_max: String,
): Serializable