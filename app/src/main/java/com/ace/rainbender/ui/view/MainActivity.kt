package com.ace.rainbender.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ace.rainbender.R
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherEntity
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
import com.ace.rainbender.data.model.weather.Daily
import com.ace.rainbender.data.model.weather.Hourly
import com.ace.rainbender.databinding.ActivityMainBinding
import com.ace.rainbender.ui.viewmodel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.deleteDailyWeather()

        CURRENT_TIME = LocalDateTime.now().toString()
        CURRENT_TIMEZONE = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)

        lateinit var bottomNav : BottomNavigationView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment ||
                destination.id == R.id.bookmarkFragment ||
                destination.id == R.id.newsFragment ||
                destination.id == R.id.radarMapFragment) {

                supportActionBar!!.show()
                binding.botnav.visibility = View.VISIBLE
            } else {
                supportActionBar!!.hide()
                binding.botnav.visibility = View.GONE
            }
        }

        bottomNav = binding.botnav
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.bookmark -> {
                    loadFragment(BookmarkFragment())
                    true
                }
                R.id.radar -> {
                    loadFragment(RadarMapFragment())
                    true
                }
                R.id.news -> {
                    loadFragment(NewsFragment())
                    true
                } else -> {
                    false
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        getWeatherForecast()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){
            R.id.profile -> {
                goToProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToProfile() {
//        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        loadFragment(ProfileFragment())
        supportActionBar!!.hide()
        onBackPressed = 1
        binding.botnav.visibility = View.GONE
    }

    private fun getWeatherForecast() {
        viewModel.getWeatherForecast()

        viewModel.weatherForecast.observe(this){
            fetchHourlyWeather(it.hourly!!)
            fetchDailyWeather(it.daily!!)
        }
    }

    private var iDaily = 0
    private fun fetchDailyWeather(daily: Daily) {
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
            fetchDailyWeather(daily)
            Log.d("daitemp",daily.temperature2mMin[4].toString())
            Log.d("daitemp",daily.temperature2mMin[5].toString())
            Log.d("daitemp",daily.temperature2mMin[6].toString())
        }


    }

    private var iHourly = 0
    private fun fetchHourlyWeather(hourly: Hourly) {
        TEST_TEMP = hourly.temperature2m!![1].toString().substring(0,2) + "°C"
        var time = CURRENT_TIME.subSequence(11,13)
        var currentTime = 0 + Integer.parseInt(time.toString())
        CURRENT_TEMPERATURE = hourly.temperature2m!![currentTime].toString().substring(0,2)
        CURRENT_WEATHERCODE = hourly.weathercode!![currentTime]!!

        if (iHourly < 49) {
            val dailyWeather = HourlyWeatherEntity (
                hourlyId = iHourly+1.toLong(),
                time = hourly.time!![iHourly]!!,
                temperature = hourly.temperature2m[iHourly]!!,
                humidity = hourly.relativehumidity2m!![iHourly]!!,
                weatherCode = hourly.weathercode[iHourly]!!
            )
            viewModel.insertHourlyWeather(dailyWeather)

            iHourly += 1
            fetchHourlyWeather(hourly)
            Log.d("daitemp",hourly.temperature2m[0].toString())

        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.removeUpdates(this)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("fusedloctest",location?.latitude.toString())
                Log.d("fusedloctest",location?.longitude.toString())

                val geocoder = Geocoder(this, Locale.getDefault())

                val list: List<Address> =
                    geocoder.getFromLocation(location?.latitude!!, location.longitude, 1)

                LATITUDE = location.latitude
                LONGITUDE = location.longitude
                CURRENT_LOCATION = list[0].locality

            }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())

        val list: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)

        Toast.makeText(this,list[0].getAddressLine(0).toString(), Toast.LENGTH_LONG).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host,fragment)
        transaction.commit()

    }

    var onBackPressed = 0
    override fun onBackPressed() {
        var currentFragment = supportFragmentManager.fragments.last()

        if (onBackPressed == 1){
//            super.onBackPressed()
            loadFragment(HomeFragment())
            supportActionBar!!.show()
            binding.botnav.visibility = View.VISIBLE
        } else if (currentFragment == LoginFragment()){

        } else {

        }
    }

    companion object {
        var LATITUDE:Double = 0.0
        var LONGITUDE:Double = 0.0
        var CURRENT_LOCATION = "location error"
        var CURRENT_TIME = ""
        var CURRENT_TIMEZONE = ""
        var CURRENT_TEMPERATURE = ""
        var CURRENT_WEATHERCODE = 0

        var TEST_TEMP = ""
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteDailyWeather()
    }
}