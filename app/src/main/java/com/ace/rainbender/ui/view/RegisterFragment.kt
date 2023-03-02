package com.ace.rainbender.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ace.rainbender.R
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.databinding.FragmentRegisterBinding
import com.ace.rainbender.ui.viewmodel.RegisterFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            createAccount()
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun createAccount() {
        if (validateInput()) {
            val user = AccountEntity(
                username = binding.etUsername.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
            viewModel.registerUser(user)
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (firstName.length < 3){
            isValid = false
            binding.etFirstName.error = getString(R.string.too_short)
        }

        if (username.length < 3){
            isValid = false
            binding.etUsername.error = getString(R.string.too_short)
        }

        if (lastName.length < 3){
            isValid = false
            binding.etLastName.error = getString(R.string.too_short)
        }

        if (password.length < 8){
            isValid = false
            binding.etPassword.error = getString(R.string.password_too_short)
        }

        if (firstName.isEmpty()) {
            isValid = false
            binding.etFirstName.error = getString(R.string.error_empty)
        }


        if (lastName.isEmpty()) {
            isValid = false
            binding.etLastName.error = getString(R.string.error_empty)
        }

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = getString(R.string.username_empty)
        }

        if (email.isEmpty()) {
            isValid = false
            binding.etEmail.error = getString(R.string.email_empty)
        }
        if (email.contains("@")) {

        } else {
            isValid = false
            binding.etEmail.error = getString(R.string.invalid_email)
        }
        if (password.isEmpty()) {
            isValid = false
            binding.etPassword.error = getString(R.string.password_empty)
        }
        if (confirmPassword.isEmpty()) {
            isValid = false
            binding.etConfirmPassword.error = getString(R.string.confirm_password)
        }
        if (password != confirmPassword) {
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }
}