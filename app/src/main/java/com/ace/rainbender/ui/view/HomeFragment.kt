package com.ace.rainbender.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.ace.rainbender.R
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherEntity
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
import com.ace.rainbender.data.model.weather.Daily
import com.ace.rainbender.data.model.weather.Hourly
import com.ace.rainbender.data.model.weather.WeatherResponse
import com.ace.rainbender.databinding.FragmentHomeBinding
import com.ace.rainbender.ui.adapter.DailyWeatherAdapter
import com.ace.rainbender.ui.adapter.HourlyWeatherAdapter
import com.ace.rainbender.ui.adapter.TabAdapter
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_LOCATION
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TEMPERATURE
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIME
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIMEZONE
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_WEATHERCODE
import com.ace.rainbender.ui.view.MainActivity.Companion.HOME
import com.ace.rainbender.ui.viewmodel.HomeFragmentViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeFragmentViewModel by viewModels()

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null

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

        HOME = true

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        setTabLayout()


        hourlyWeatherRv = binding.rvHourlyForecast
        dailyWeatherRv = binding.rvDailyForecast

        setLayouts()
        setWeatherAdapters()
        getWeatherForecast()
        loadWeatherIcon()

        binding.tvLocation.text = CURRENT_LOCATION
        binding.tvTime.text = CURRENT_TIME.subSequence(11, 16)
        binding.tvTimezone.text = CURRENT_TIMEZONE
        binding.tvTemperature.text = CURRENT_TEMPERATURE + "°C"

    }

    private fun setTabLayout() {
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Today"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Tomorrow"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val tabAdapter = TabAdapter(
            (activity as AppCompatActivity).supportFragmentManager,
            tabLayout!!.tabCount,
            lifecycle
        )
        viewPager!!.adapter = tabAdapter

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun loadWeatherIcon() {
        val weatherCode = CURRENT_WEATHERCODE

        with(binding) {

            when (weatherCode) {
                0 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wesun)
                    tvWeather.text = getString(R.string.we_clear)
                }
                3 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                    tvWeather.text = getString(R.string.we_overcast)
                }
                77 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                    tvWeather.text = getString(R.string.we_snow_grains)
                }
                in 1..2 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wesuncloudy)
                    tvWeather.text = getString(R.string.we_partly_cloudy)
                }
                in 45..48 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                    tvWeather.text = getString(R.string.we_fog)
                }
                in 51..57 -> {
                    ivWeatherIcon.setImageResource(R.drawable.werain)
                    tvWeather.text = getString(R.string.we_drizzle)
                }
                in 61..67 -> {
                    ivWeatherIcon.setImageResource(R.drawable.werain)
                    tvWeather.text = getString(R.string.we_rain)
                }
                in 71..75 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                    tvWeather.text = getString(R.string.we_snowfall)
                }
                in 80..82 -> {
                    ivWeatherIcon.setImageResource(R.drawable.werain)
                    tvWeather.text = getString(R.string.we_rainshowers)
                }
                in 85..86 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wesnow)
                    tvWeather.text = getString(R.string.we_snowshow)
                }
                in 95..99 -> {
                    ivWeatherIcon.setImageResource(R.drawable.wethunderstorm)
                    tvWeather.text = getString(R.string.we_thunderstorm)
                }
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

        viewModel.weatherForecast.observe(viewLifecycleOwner) {
            loadHomeWeather(it)
            loadHourlyWeather(it.hourly!!)
            loadDailyWeather(it.daily!!)
        }

        viewModel.dailyForecast.observe(viewLifecycleOwner) {
            fetchDailyWeather(it)
        }
        viewModel.hourlyForecast.observe(viewLifecycleOwner) {
            fetchHourlyWeather(it)
        }
    }

    private var iDaily = 0
    private fun loadDailyWeather(daily: Daily) {
        if (iDaily < 7) {
            val dailyWeather = DailyWeatherEntity(
                dailyId = iDaily + 1.toLong(),
                time = daily.time!![iDaily]!!,
                temperatureMin = daily.temperature2mMin!![iDaily]!!,
                temperatureMax = daily.temperature2mMax!![iDaily]!!,
                weatherCode = daily.weathercode!![iDaily]!!
            )
            viewModel.insertDailyWeather(dailyWeather)

            iDaily += 1
            loadDailyWeather(daily)
            Log.d("daitemp", daily.temperature2mMin[4].toString())
            Log.d("daitemp", daily.temperature2mMin[5].toString())
            Log.d("daitemp", daily.temperature2mMin[6].toString())
        }

    }

    private var iHourly = 0
    private fun loadHourlyWeather(hourly: Hourly) {
        if (iHourly < 49) {
            val dailyWeather = HourlyWeatherEntity(
                hourlyId = iHourly + 1.toLong(),
                time = hourly.time!![iHourly]!!,
                temperature = hourly.temperature2m!![iHourly]!!,
                humidity = hourly.relativehumidity2m!![iHourly]!!,
                weatherCode = hourly.weathercode!![iHourly]!!
            )
            viewModel.insertHourlyWeather(dailyWeather)

            iHourly += 1
            loadHourlyWeather(hourly)
            Log.d("daitemp", hourly.temperature2m[0].toString())

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
        val position = Integer.parseInt(CURRENT_TIME.subSequence(11, 13).toString())
        hourlyWeatherRv.layoutManager?.scrollToPosition(position)
    }

    companion object {
        var REFRESH = true
    }

}