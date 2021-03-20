package br.com.wcabral.myweather.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.wcabral.myweather.R
import br.com.wcabral.myweather.data.remote.RetrofitManager
import br.com.wcabral.myweather.data.remote.model.City
import br.com.wcabral.myweather.data.remote.model.FindForecast
import br.com.wcabral.myweather.extension.isInternetAvailable
import br.com.wcabral.myweather.extension.toPx
import br.com.wcabral.myweather.ui.main.forecast.ForecastAdapter
import br.com.wcabral.myweather.ui.main.search.SearchFragment.Companion.CITY
import br.com.wcabral.myweather.util.MarginItemDecoration
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ForecastActivity
    private val city by lazy { intent.getSerializableExtra(CITY) as City }
    private val forecastAdapter by lazy { ForecastAdapter() }
    private val TAG = "MYTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        initUi()
        setTexts()
        setImage()
        doDetail()
        setupListeners()
    }

    private fun initUi() {
        val rvValues = findViewById<RecyclerView>(R.id.rvValues)
        val btnFav = findViewById<Button>(R.id.btnFavForecast)
        rvValues.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = forecastAdapter
            addItemDecoration(MarginItemDecoration(16.toPx()))
        }

        forecastAdapter.setContext(applicationContext)

        btnFav.setOnClickListener {
            Log.i("MYTAG", "FAV")
        }
    }

    private fun setTexts() {
        val title = findViewById<TextView>(R.id.textTitle)
        val value = findViewById<TextView>(R.id.textValue)
        val unit = findViewById<TextView>(R.id.textUnit)

        title.text = "Weather in ${city.name}, ${city.country.name}"
        val temp = createShared()
        if (temp == "metric") {
            value.text = "°C"
            unit.text = city.main.temp
        } else if (temp == "imperial") {
            unit.text = "°F"
            val calc = (city.main.temp.toDouble() * 9 / 5) + 32
            val parsed = DecimalFormat("##.####").format(calc).toString()
            value.text = parsed
        }
    }

    private fun createShared(): String? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getString("pref_temp_key", "C")
    }

    private fun setImage() {
        val imgWeatherDetail = findViewById<ImageView>(R.id.imgWeatherDetail)
        val imgUrl = "http://openweathermap.org/img/wn/${city.weathers[0].icon}@4x.png"
        imgWeatherDetail.load(imgUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_weather_placeholder)
        }
    }

    private fun doDetail() {
        if (applicationContext.isInternetAvailable()) {
            val call = RetrofitManager.getOpenWeatherService().findForecast(
                city.id.toString(),
                "5fde54966e3e1c8a80e436245bdf9672"
            )

            call.enqueue(object : Callback<FindForecast> {
                override fun onResponse(
                    call: Call<FindForecast>,
                    response: Response<FindForecast>
                ) {
                    if (response.isSuccessful) {
                        forecastAdapter.submitList(response.body()?.cities)
                    } else {
                        Log.w(TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<FindForecast>, t: Throwable) {
                    Log.e(TAG, "onFailure", t)
                }
            })
        }
    }

    private fun setupListeners() {
        btnFav()
    }

    private fun btnFav() {
        val btnFav = findViewById<Button>(R.id.btnFavForecast)
        btnFav.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(CITY, city)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

}