package com.ace.rainbender.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ace.rainbender.R
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.databinding.FragmentProfileBinding
import com.ace.rainbender.ui.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var uri: Uri

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
        viewModel.getAccountId().observe(requireActivity()) {
            viewModel.getAccountById(it)
            viewModel.profileData.observe(requireActivity()) {
                binding.etUsername.setText(it.username)
                binding.etEmail.setText(it.email)
                if (it.profilePicture != null) {
                    setProfilePicture(convertStringToBitmap(it.profilePicture!!))
                }
                Log.d("profilePic", it.profilePicture.toString())
            }
        }
    }

    private fun convertStringToBitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun convertBitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun setProfilePicture(bitmap: Bitmap) {

        binding.ivProfilePic.load(bitmap) {
            crossfade(true)
            placeholder(R.drawable.ic_image)
            transformations(RoundedCornersTransformation())
        }

//        Glide.with(binding.ivProfilePic.context)
//            .load(bitmap)
//            .centerCrop()
//            .placeholder(R.drawable.ic_image)
//            .into(binding.ivProfilePic)

    }

    private fun setOnclickListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.setLoginStatus(false)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnUpdateProfile.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("Update Profile")
                .setMessage("Are you sure to update profile?")
                .setPositiveButton(
                    "Yep"
                ) { _, _ ->
                    updateProfile()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .show()
        }

        binding.btnEditImage.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        val REQUEST_CODE_PERMISSION = 200
        if (isGranted(
                requireActivity(), Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("Select Picture")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            requireActivity(),
            "${requireActivity().packageName}.provider",
            photoFile
        )
        cameraResult.launch(uri)
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                handleCameraImage(uri)
            }
        }

    private fun handleCameraImage(uri: Uri) {

        binding.ivProfilePic.load(uri) {
            crossfade(true)
            placeholder(R.drawable.ic_image)
            target { result ->
                val bitmap = (result as BitmapDrawable).bitmap
                saveProfilePicture(bitmap)
            }
            transformations(RoundedCornersTransformation())
        }
    }

    private fun saveProfilePicture(bitmap: Bitmap) {
        var convertedImg = convertBitMapToString(bitmap)
        Log.d("converted", convertedImg)

        viewModel.getAccountId().observe(requireActivity()){
            viewModel.getAccountById(it)
            viewModel.profileData.observe(requireActivity()) {
                val accounEntity = AccountEntity(
                    accountId = it.accountId,
                    username = it.username,
                    email = it.email,
                    password = it.password,
                    profilePicture = convertedImg,
                    bookmark = it.bookmark
                )
                viewModel.updateUser(accounEntity)

                Log.d("conv ent", accounEntity.profilePicture.toString())
            }
        }
        findNavController().navigate(R.id.action_profileFragment_self)
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivProfilePic.load(result) {
                crossfade(true)
                placeholder(R.drawable.ic_image)
                target { result ->
                    val bitmap = (binding.ivProfilePic.drawable as BitmapDrawable).bitmap
//                    val bitmap = (result as BitmapDrawable).bitmap
                    saveProfilePicture(bitmap)
                }
                transformations(RoundedCornersTransformation())
            }

        }

    private fun isGranted(
        requireActivity: Activity,
        camera: String,
        permissions: Array<String>,
        requestCodePermission: Int
    ): Boolean {
        val permissionCheck = checkSelfPermission(requireActivity, camera)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity, camera)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity,
                    permissions,
                    requestCodePermission
                )
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, please allow from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("Package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun updateProfile() {
        viewModel.profileData.observe(viewLifecycleOwner) {
            val accounEntity = AccountEntity(
                accountId = it.accountId,
                username = binding.etUsername.text.toString(),
                email = binding.etEmail.text.toString(),
                password = it.password,
                profilePicture = it.profilePicture,
                bookmark = it.bookmark
            )
            viewModel.updateUser(accounEntity)
        }
    }

}