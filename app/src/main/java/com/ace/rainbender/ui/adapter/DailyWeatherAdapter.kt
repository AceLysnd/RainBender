package com.ace.rainbender.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherEntity
import com.ace.rainbender.databinding.ItemDailyForecastBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyWeatherAdapter(
    private var dailyWeather: MutableList<DailyWeatherEntity>
) : RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        val binding =
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyWeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = dailyWeather.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        holder.bind(dailyWeather[position])
    }

    fun setData(daily: List<DailyWeatherEntity>) {
        this.dailyWeather.clear()
        this.dailyWeather = daily as MutableList<DailyWeatherEntity>
        notifyDataSetChanged()
        Log.d("daiweasize", dailyWeather.size.toString())
    }

    fun addData(daily: List<DailyWeatherEntity>) {
        this.dailyWeather.clear()
        this.dailyWeather.addAll(daily)
        notifyDataSetChanged()
    }

    inner class DailyWeatherViewHolder(private val binding: ItemDailyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(daily: DailyWeatherEntity) {
            with(binding) {
                tvDate.text = daily.time.subSequence(8, 10)

                val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDate.parse(daily.time, monthFormatter)
                var month = date.month.toString().substring(0, 1).toUpperCase() +
                        date.month.toString().substring(1).toLowerCase()
                tvMonth.text = month
                Log.d("date", daily.time)

                tvTempMin.text = daily.temperatureMin.toString()
                tvTempMax.text = daily.temperatureMax.toString() + "°C"

                when (daily.weatherCode) {
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
