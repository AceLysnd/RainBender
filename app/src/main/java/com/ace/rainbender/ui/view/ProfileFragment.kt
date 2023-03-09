package com.ace.rainbender.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
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

        loadProfile()
        setOnclickListeners()
    }

    private fun loadProfile() {
        viewModel.getAccountId().observe(viewLifecycleOwner){
            viewModel.getAccountById(it)
            viewModel.profileData.observe(viewLifecycleOwner){
                binding.etUsername.setText(it.username)
                binding.etEmail.setText(it.email)
                if (it.profilePicture != null){
                    setProfilePicture(convertStringToBitmap(it.profilePicture!!))
                }
            }
        }
    }

    private fun convertStringToBitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun setProfilePicture(bitmap: Bitmap) {

        binding.ivProfilePic.load(bitmap) {
            crossfade(true)
            placeholder(R.drawable.ic_image)
            transformations(RoundedCornersTransformation())
        }

    }

    private fun setOnclickListeners() {
        binding.btnLogout.setOnClickListener{
            viewModel.setLoginStatus(false)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }

}