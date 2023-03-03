package com.ace.rainbender.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Address
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ace.rainbender.R
import com.ace.rainbender.databinding.ActivityMainBinding
import com.ace.rainbender.ui.viewmodel.MainActivityViewModel
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var bottomNav : BottomNavigationView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment ||
                destination.id == R.id.bookmarkFragment ||
                destination.id == R.id.newsFragment ||
                destination.id == R.id.radarMapFragment) {

                binding.botnav.visibility = View.VISIBLE
            } else {

                binding.botnav.visibility = View.GONE
            }
        }

        bottomNav = binding.botnav as BottomNavigationView
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

        getLocation()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000000, 170f, this)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())

        val list: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)

        LATITUDE = location.latitude
        LONGITUDE = location.longitude
        CURRENT_LOCATION = list[0].locality

//        Toast.makeText(this,list[0].getAddressLine(0).toString(), Toast.LENGTH_LONG).show()
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


    override fun onBackPressed() {
//        super.onBackPressed()
    }

    companion object {
        var LATITUDE:Double = 0.0
        var LONGITUDE:Double = 0.0
        var CURRENT_LOCATION = ""
        var CURRENT_TIME = ""
        var CURRENT_TIMEZONE = ""
    }
}