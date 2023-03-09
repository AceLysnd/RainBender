package com.ace.rainbender.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ace.rainbender.R
import com.ace.rainbender.databinding.FragmentNewsBinding
import com.ace.rainbender.databinding.FragmentProfileBinding
import com.ace.rainbender.ui.viewmodel.HomeFragmentViewModel
import com.ace.rainbender.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnclickListeners()
    }

    private fun setOnclickListeners() {
        binding.btnLogout.setOnClickListener{
            viewModel.setLoginStatus(false)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }

}