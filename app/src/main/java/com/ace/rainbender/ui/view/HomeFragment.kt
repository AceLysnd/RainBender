package com.ace.rainbender.ui.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import com.ace.rainbender.data.local.localweather.DailyWeatherEntity
import com.ace.rainbender.data.model.weather.Daily
import com.ace.rainbender.data.model.weather.Hourly
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

//    private lateinit var hourlyWeatherRv: RecyclerView
    private lateinit var dailyWeatherRv: RecyclerView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        swipeRefreshLayout = binding.swipe

        swipeRefreshLayout.setOnRefreshListener {
            findNavController().navigate(R.id.action_homeFragment_self)
            Handler().postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 4000)
        }

        //        hourlyWeatherRv = binding.rvHourlyForecast
        dailyWeatherRv = view.findViewById(R.id.rv_daily_forecast)

        setLayouts()
        setWeatherAdapters()
        getWeatherForecast()


        binding.tvLocation.text = CURRENT_LOCATION
        binding.tvTime.text = CURRENT_TIME.subSequence(11,16)
        binding.tvTimezone.text = CURRENT_TIMEZONE
        binding.tvTemperature.text = CURRENT_TEMPERATURE + "°C"
        loadWeatherIcon()
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
            } else if (weatherCode in 3..48) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudy)
            } else if (weatherCode in 51..67) {
                ivWeatherIcon.setImageResource(R.drawable.werain)
            } else if (weatherCode in 71..75) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
            } else if (weatherCode == 77) {
                ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
            } else if (weatherCode in 80..82) {
                ivWeatherIcon.setImageResource(R.drawable.werain)
            } else if (weatherCode in 85..86) {
                ivWeatherIcon.setImageResource(R.drawable.wesnow)
            } else if (weatherCode in 95..99) {
                ivWeatherIcon.setImageResource(R.drawable.wethunderstorm)
            }
        }

    }

    private fun setLayouts() {
//        hourlyWeatherRv.layoutManager = LinearLayoutManager(
//                requireContext(),
//        LinearLayoutManager.HORIZONTAL,
//        false
//        )
        dailyWeatherRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setWeatherAdapters() {
        hourlyWeatherAdapter = HourlyWeatherAdapter()
//        hourlyWeatherRv.adapter = hourlyWeatherAdapter

        dailyWeatherAdapter = DailyWeatherAdapter(mutableListOf())
        dailyWeatherRv.adapter = dailyWeatherAdapter

    }

    private fun getWeatherForecast() {
        viewModel.getWeatherForecast()
        viewModel.getDailyWeather()

        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbPost.isVisible = isLoading
            binding.rvDailyForecast.isVisible = !isLoading
        }

        viewModel.errorState.observe(viewLifecycleOwner) { errorData ->
            binding.tvError.isVisible = errorData.first
            errorData.second?.message?.let {
                binding.tvError.text = it
            }
        }

        viewModel.dailyForecast.observe(viewLifecycleOwner){
            fetchDailyWeather(it)
        }
    }

    private fun fetchDailyWeather(daily: List<DailyWeatherEntity>) {
        dailyWeatherAdapter.addData(daily)
    }

    private fun fetchHourlyWeather(hourly: Hourly) {
        MainActivity.TEST_TEMP = hourly.temperature2m!![1].toString().substring(0,2)
        var time = CURRENT_TIME.subSequence(11,13)
        Log.d("time", time.toString())
        var currentTime = 0 + Integer.parseInt(time.toString())
        CURRENT_TEMPERATURE = hourly.temperature2m!![currentTime].toString().substring(0,2)

//        hourlyWeatherAdapter.





    }

}