package com.ace.rainbender.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.ace.rainbender.R
import com.ace.rainbender.databinding.FragmentNewsBinding
import com.ace.rainbender.databinding.FragmentRadarMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_radar_map.*
import java.util.*

class RadarMapFragment : Fragment() {

    private lateinit var binding: FragmentRadarMapBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRadarMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var latitude: Double
        var longitude: Double
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location!!.latitude
                longitude = location.longitude

                webView.webViewClient = WebViewClient()
                val radarEmbed = "<iframe src=\"https://www.rainviewer.com/map.html?loc=" +
                        latitude + "," + longitude +
                        ",12&oFa=0&oC=1&oU=0&oCS=1&oF=0&oAP=0&c=3&o=90&lm=1&layer=radar&sm=1&sn=1&hu=0\" width=\"100%\" frameborder=\"0\" style=\"border:0;height:96vh;\" allowfullscreen></iframe>"
                webView.loadData(radarEmbed, "text/html", "UTF-8")
                webView.settings.javaScriptEnabled = true
                webView.settings.setSupportZoom(true)


            }


    }

}