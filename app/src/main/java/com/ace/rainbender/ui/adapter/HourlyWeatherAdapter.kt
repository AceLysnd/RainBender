package com.ace.rainbender.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
import com.ace.rainbender.databinding.ItemHourlyForecastBinding

class HourlyWeatherAdapter (
    private var hourlyWeather: MutableList<HourlyWeatherEntity>
): RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyWeatherViewHolder {
        val binding =
            ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyWeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = hourlyWeather.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(hourlyWeather[position])
    }


    fun setData(hourly: List<HourlyWeatherEntity>) {
        this.hourlyWeather.clear()
        this.hourlyWeather = hourly as MutableList<HourlyWeatherEntity>
        notifyDataSetChanged()
        Log.d("daiweasize", hourlyWeather.size.toString())
    }

    fun addData(hourly: List<HourlyWeatherEntity>) {
        this.hourlyWeather.clear()
        this.hourlyWeather.addAll(hourly)
        notifyDataSetChanged()
        Log.d("daiweasize", hourlyWeather.size.toString())
    }

    inner class HourlyWeatherViewHolder(private val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(hourly: HourlyWeatherEntity) {
            with(binding) {
                tvTemperature.text = hourly.temperature.toString().substring(0,2) + "°C"
                tvHumidity.text = hourly.humidity.toString() + "%"
                tvTime.text = hourly.time.subSequence(11, 16)

                when (hourly.weatherCode) {
                    0 -> ivWeatherIcon.setImageResource(R.drawable.wesun)
                    77 -> ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                    in 1..2 -> ivWeatherIcon.setImageResource(R.drawable.wesuncloudy)
                    in 3..48 -> ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                    in 51..67 -> ivWeatherIcon.setImageResource(R.drawable.werain)
                    in 71..75 -> ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                    in 80..82 -> ivWeatherIcon.setImageResource(R.drawable.werain)
                    in 85..86 -> ivWeatherIcon.setImageResource(R.drawable.wesnow)
                    in 95..99 -> ivWeatherIcon.setImageResource(R.drawable.wethunderstorm)
                }
            }
        }
    }
}