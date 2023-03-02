package com.ace.rainbender.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.ace.rainbender.R
import com.ace.rainbender.ui.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        getLoginStatus()

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return view
    }

    private fun getLoginStatus() {
        viewModel.getLoginStatus().observe(viewLifecycleOwner){
            if(it) {
                goToHome()
                STATUS_LOGIN = true
                activity?.findViewById(R.id.botnav)
            } else {
                goToLogin()
                STATUS_LOGIN = false
            }
        }
    }

    private fun goToLogin() {
        val postDelayed = Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }, 1500)
    }

    private fun goToHome() {
        val postDelayed = Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setHideBotnav(STATUS_LOGIN)
    }
    companion object {
        var STATUS_LOGIN = false
    }

}