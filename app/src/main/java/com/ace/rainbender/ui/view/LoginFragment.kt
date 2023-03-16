package com.ace.rainbender.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ace.rainbender.R
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.databinding.FragmentLoginBinding
import com.ace.rainbender.ui.viewmodel.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setOnclickListeners()
    }

    private fun setOnclickListeners() {
        binding.btnLogin.setOnClickListener{checkLogin()}
        binding.tvGoToRegister.setOnClickListener{goToRegister()}
    }

    private fun goToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun checkLogin(){
        if (validateInput()) {
            val username = binding.etUsername.text.toString()
            viewModel.getUser(username)

            viewModel.getUser.observe(viewLifecycleOwner) {
                checkAccount(it)
            }
        }
    }

    private fun checkAccount(account: AccountEntity?) {
        account?.let {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            Log.d("Check Login", account.username)

            val verified = username == account.username && password == account.password
            if (verified) {
                goToHome()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.username_password_incorrect),
                    Toast.LENGTH_SHORT
                ).show()
            }
            saveLoginInfo(
                account.username,
                account.email,
                account.password,
                account.accountId,
                verified
            )
        }
    }

    private fun saveLoginInfo(
        username: String,
        email: String,
        password: String,
        accountId: Long,
        verified: Boolean
    ) {
        viewModel.setAccount(username, email, password, accountId)
        viewModel.saveLoginStatus(verified)

    }

    fun validateInput(): Boolean {
        var isValid = true
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = getString(R.string.username_empty)
        }
        if (password.isEmpty()) {
            isValid = false
            binding.etPassword.error = getString(R.string.password_empty)
        }
        return isValid
    }

    private fun goToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
}