package com.ace.rainbender.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ace.rainbender.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}