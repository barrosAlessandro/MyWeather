package br.com.wcabral.myweather.ui.main.search

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
import br.com.wcabral.myweather.databinding.ItemCityBinding
import coil.load
import java.text.DecimalFormat

class SearchAdapter : ListAdapter<City, SearchAdapter.ViewHolder>(SearchDiff()) {

    private var onClick: OnClick? = null
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: City) {
            binding.tvCityName.text = city.name
            binding.tvCountry.text = city.country.name
            binding.tvValue.text = city.main.temp

            val temp = createShared()
            if (temp == "metric") {
                binding.txUnit.text = "°C"
                binding.tvValue.text = city.main.temp
            } else if(temp == "imperial") {
                binding.txUnit.text = "°F"
                val calc = (city.main.temp.toDouble() * 9 / 5) + 32
                val parsed = DecimalFormat("##.####").format(calc).toString()
                binding.tvValue.text = parsed
            }

            val imgUrl = "http://openweathermap.org/img/wn/${city.weathers[0].icon}@4x.png"
            binding.imgWeather.load(imgUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_weather_placeholder)
            }
            binding.forecastActivityOpen.setOnClickListener {
                onClick?.onClick(city = city)
            }
        }

    }

    private fun createShared(): String? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString("pref_temp_key", "C")
    }

    fun setOnClickListener(onclick: OnClick) {
        this.onClick = onclick
    }

    fun setContext(context: Context) {
        this.context = context
    }

    interface OnClick {
        fun onClick(
            city: City?
        )
    }

    class SearchDiff : DiffUtil.ItemCallback<City>() {

        override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem == newItem

        override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem

    }
}