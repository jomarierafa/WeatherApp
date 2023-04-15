package com.example.weatherapp.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.common.toast
import com.example.weatherapp.data.local.User
import com.example.weatherapp.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()

    }

    private fun initListeners() {
        binding.signupButton.setOnClickListener {
            viewModel.addUser(User(
                firstname = binding.firstNameInput.text.toString(),
                lastname = binding.lastNameInput.text.toString(),
                username = binding.usernameInput.text.toString(),
                password = binding.passwordInput.text.toString(),
                confirmPassword = binding.confirmPasswordInput.text.toString()
            ))
        }

        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when(event) {
                    is SignupViewModel.UiEvent.ShowToast -> {
                        requireContext().toast(event.message)
                    }
                    is SignupViewModel.UiEvent.SaveUser -> {
                        requireContext().toast(getString(R.string.successfully_registered))
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
    }

}