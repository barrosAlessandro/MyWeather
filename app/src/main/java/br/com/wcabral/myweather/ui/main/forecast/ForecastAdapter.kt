package br.com.wcabral.myweather.ui.main.forecast

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.wcabral.myweather.R
import br.com.wcabral.myweather.data.remote.model.City
import br.com.wcabral.myweather.databinding.ItemForecastBinding
import coil.load
import java.text.DecimalFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter : ListAdapter<City, ForecastAdapter.ViewHolder>(SearchDiff()) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: City) {
            val imgUrl = "http://openweathermap.org/img/wn/${city.weathers[0].icon}@4x.png"

            binding.tvDate.text = convertTime(city.dt)
            binding.tvDescription.text = city.weathers[0].description

            val temp = createShared()
            if (temp == "metric") {
                binding.tvUnit.text = "°C"
                binding.tvValue.text = (city.main.temp.toDouble() / 12).toString()
            } else if (temp == "imperial") {
                binding.tvUnit.text = "°F"
                val calc = ((city.main.temp.toDouble() * 9 / 5) + 32) / 12
                val parsed = DecimalFormat("##.####").format(calc).toString()
                binding.tvValue.text = parsed
            }

            binding.imgWeather.load(imgUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_weather_placeholder)
            }
        }

    }

    private fun createShared(): String? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString("pref_temp_key", "C")
    }

    fun convertTime(time: Long): String? {
        val date = Date(time * 1000)
        val format: Format = SimpleDateFormat("MMMM dd, yyyy HH:mm a")
        return format.format(date)
    }

    fun setContext(context: Context) {
        this.context = context
    }

    class SearchDiff : DiffUtil.ItemCallback<City>() {

        override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem == newItem

        override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem

    }
}