package com.ace.rainbender.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
import com.ace.rainbender.data.model.weather.Hourly
import com.ace.rainbender.databinding.FragmentTodayBinding
import com.ace.rainbender.ui.adapter.HourlyWeatherAdapter
import com.ace.rainbender.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var hourlyWeatherRv: RecyclerView

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyWeatherRv = binding.rvHourlyForecast

        setLayouts()
        setWeatherAdapters()
        getWeatherForecast()
    }

    private fun getWeatherForecast() {
        viewModel.getWeatherForecast()
        viewModel.getHourlyWeather()

        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbPost.isVisible = isLoading
            binding.rvHourlyForecast.isVisible = !isLoading
        }

        viewModel.errorState.observe(viewLifecycleOwner) { errorData ->
            binding.tvError.isVisible = errorData.first
            errorData.second?.message?.let {
                binding.tvError.text = it
            }
        }

        viewModel.weatherForecast.observe(viewLifecycleOwner){
            loadHourlyWeather(it.hourly!!)
        }

        viewModel.hourlyForecast.observe(viewLifecycleOwner){
            fetchHourlyWeather(it)
        }
    }

    private fun setWeatherAdapters() {
        hourlyWeatherAdapter = HourlyWeatherAdapter(mutableListOf())
        hourlyWeatherRv.adapter = hourlyWeatherAdapter
    }

    private fun setLayouts() {
        hourlyWeatherRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
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

    private fun fetchHourlyWeather(hourly: List<HourlyWeatherEntity>) {

        hourlyWeatherAdapter.addData(hourly)
        val position = Integer.parseInt(MainActivity.CURRENT_TIME.subSequence(11,13).toString())
        hourlyWeatherRv.layoutManager?.scrollToPosition(position)
    }
}