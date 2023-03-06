package com.ace.rainbender.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ace.rainbender.R
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherEntity
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
import com.ace.rainbender.data.model.weather.Daily
import com.ace.rainbender.data.model.weather.Hourly
import com.ace.rainbender.data.model.weather.WeatherResponse
import com.ace.rainbender.databinding.FragmentHomeBinding
import com.ace.rainbender.ui.adapter.DailyWeatherAdapter
import com.ace.rainbender.ui.adapter.HourlyWeatherAdapter
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_LOCATION
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TEMPERATURE
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIME
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIMEZONE
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_WEATHERCODE
import com.ace.rainbender.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var hourlyWeatherRv: RecyclerView
    private lateinit var dailyWeatherRv: RecyclerView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CURRENT_TIME = LocalDateTime.now().toString()
        CURRENT_TIMEZONE = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
        Log.d("Timezone", CURRENT_TIMEZONE)

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if (REFRESH) {
            findNavController().navigate(R.id.action_homeFragment_self)
            REFRESH = false
        }

        hourlyWeatherRv = binding.rvHourlyForecast
        dailyWeatherRv = view.findViewById(R.id.rv_daily_forecast)

        setLayouts()
        setWeatherAdapters()
        getWeatherForecast()
        loadWeatherIcon()

        binding.tvLocation.text = CURRENT_LOCATION
        binding.tvTime.text = CURRENT_TIME.subSequence(11,16)
        binding.tvTimezone.text = CURRENT_TIMEZONE
        binding.tvTemperature.text = CURRENT_TEMPERATURE + "°C"

    }

    private fun loadWeatherIcon() {
        val weatherCode = CURRENT_WEATHERCODE

        with(binding) {
            if (weatherCode == 0) {
                ivWeatherIcon.setImageResource(R.drawable.wesun)
                tvWeather.text = "Clear"
            } else if (weatherCode in 1..2) {
                ivWeatherIcon.setImageResource(R.drawable.wesuncloudy)
                tvWeather.text = "Partly Cloudy"
            } else if (weatherCode == 3) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                tvWeather.text = "Overcast"
            } else if (weatherCode in 45..48) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                tvWeather.text = "Fog"
            } else if (weatherCode in 51..57) {
                ivWeatherIcon.setImageResource(R.drawable.werain)
                tvWeather.text = "Drizzle"
            } else if (weatherCode in 61..67) {
                ivWeatherIcon.setImageResource(R.drawable.werain)
                tvWeather.text = "Rain"
            }else if (weatherCode in 71..75) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                tvWeather.text = "Snowfall"
            } else if (weatherCode == 77) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                tvWeather.text = "Snow grains"
            } else if (weatherCode in 80..82) {
                ivWeatherIcon.setImageResource(R.drawable.werain)
                tvWeather.text = "Rain showers"
            } else if (weatherCode in 85..86) {
                ivWeatherIcon.setImageResource(R.drawable.wesnow)
                tvWeather.text = "Snow showers"
            } else if (weatherCode in 95..99) {
                ivWeatherIcon.setImageResource(R.drawable.wethunderstorm)
                tvWeather.text = "Thunderstorm"
            }
        }

    }

    private fun setLayouts() {
        hourlyWeatherRv.layoutManager = LinearLayoutManager(
                requireContext(),
        LinearLayoutManager.HORIZONTAL,
        false
        )
        dailyWeatherRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setWeatherAdapters() {
        hourlyWeatherAdapter = HourlyWeatherAdapter(mutableListOf())
        hourlyWeatherRv.adapter = hourlyWeatherAdapter

        dailyWeatherAdapter = DailyWeatherAdapter(mutableListOf())
        dailyWeatherRv.adapter = dailyWeatherAdapter

    }

    private fun getWeatherForecast() {
        viewModel.getWeatherForecast()
        viewModel.getDailyWeather()
        viewModel.getHourlyWeather()

        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbPost.isVisible = isLoading
            binding.rvHourlyForecast.isVisible = !isLoading
            binding.rvDailyForecast.isVisible = !isLoading
        }

        viewModel.errorState.observe(viewLifecycleOwner) { errorData ->
            binding.tvError.isVisible = errorData.first
            errorData.second?.message?.let {
                binding.tvError.text = it
            }
        }

        viewModel.weatherForecast.observe(viewLifecycleOwner){
            loadHomeWeather(it)
            loadHourlyWeather(it.hourly!!)
            loadDailyWeather(it.daily!!)
        }

        viewModel.dailyForecast.observe(viewLifecycleOwner){
            fetchDailyWeather(it)
        }
        viewModel.hourlyForecast.observe(viewLifecycleOwner){
            fetchHourlyWeather(it)
        }
    }

    private var iDaily = 0
    private fun loadDailyWeather(daily: Daily) {
        if (iDaily < 7) {
            val dailyWeather = DailyWeatherEntity (
                dailyId = iDaily+1.toLong(),
                time = daily.time!![iDaily]!!,
                temperatureMin = daily.temperature2mMin!![iDaily]!!,
                temperatureMax = daily.temperature2mMax!![iDaily]!!,
                weatherCode = daily.weathercode!![iDaily]!!
            )
            viewModel.insertDailyWeather(dailyWeather)

            iDaily += 1
            loadDailyWeather(daily)
            Log.d("daitemp",daily.temperature2mMin[4].toString())
            Log.d("daitemp",daily.temperature2mMin[5].toString())
            Log.d("daitemp",daily.temperature2mMin[6].toString())
        }

    }

    private var iHourly = 0
    private fun loadHourlyWeather(hourly: Hourly) {
        if (iHourly < 49) {
            val dailyWeather = HourlyWeatherEntity (
                hourlyId = iHourly+1.toLong(),
                time = hourly.time!![iHourly]!!,
                temperature = hourly.temperature2m!![iHourly]!!,
                humidity = hourly.relativehumidity2m!![iHourly]!!,
                weatherCode = hourly.weathercode!![iHourly]!!
            )
            viewModel.insertHourlyWeather(dailyWeather)

            iHourly += 1
            loadHourlyWeather(hourly)
            Log.d("daitemp",hourly.temperature2m[0].toString())

        }
    }

    private fun loadHomeWeather(it: WeatherResponse?) {
        MainActivity.TEST_TEMP = it?.hourly?.temperature2m!![1].toString().substring(0, 2)
        var time = CURRENT_TIME.subSequence(11, 13)
        Log.d("time", time.toString())
        var currentTime = 0 + Integer.parseInt(time.toString())
        CURRENT_TEMPERATURE = it.hourly.temperature2m[currentTime].toString().substring(0, 2)
        CURRENT_WEATHERCODE = it.hourly.weathercode!![currentTime]!!
    }




    private fun fetchDailyWeather(daily: List<DailyWeatherEntity>) {
        dailyWeatherAdapter.addData(daily)
    }

    private fun fetchHourlyWeather(hourly: List<HourlyWeatherEntity>) {

        hourlyWeatherAdapter.addData(hourly)
        val position = Integer.parseInt(CURRENT_TIME.subSequence(11,13).toString())
        hourlyWeatherRv.layoutManager?.scrollToPosition(position)
    }

    companion object{
        var REFRESH = true
    }

}