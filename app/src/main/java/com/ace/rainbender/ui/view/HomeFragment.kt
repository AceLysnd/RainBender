package com.ace.rainbender.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.ace.rainbender.R
import com.ace.rainbender.databinding.FragmentHomeBinding
import com.ace.rainbender.databinding.FragmentLoginBinding
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_LOCATION
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIME
import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIMEZONE
import com.ace.rainbender.ui.view.MainActivity.Companion.LONGITUDE
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        CURRENT_TIME = LocalDateTime.now().toString()
        CURRENT_TIMEZONE = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
        Log.d("Timezone", CURRENT_TIMEZONE)

        binding.tvLocation.text = CURRENT_LOCATION
        binding.tvTime.text = CURRENT_TIME.subSequence(11,16)
        binding.tvTimezone.text = CURRENT_TIMEZONE
    }

}