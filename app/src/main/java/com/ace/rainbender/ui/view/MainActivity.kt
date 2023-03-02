package com.ace.rainbender.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ace.rainbender.R
import com.ace.rainbender.databinding.ActivityMainBinding
import com.ace.rainbender.ui.viewmodel.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()


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
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host,fragment)
        transaction.commit()

    }


    override fun onBackPressed() {
//        super.onBackPressed()
    }
}