package com.example.weatherapp.presentation.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.common.buildAlertDialog
import com.example.weatherapp.common.isAutomaticTimeEnabled
import com.example.weatherapp.common.toast
import com.example.weatherapp.databinding.ActivityLoginBinding
import com.example.weatherapp.presentation.main.MainActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.somePermissionPermanentlyDenied
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
        initObservers()

    }

    private fun initViews() {
        if(!isAutomaticTimeEnabled(this)) {
            buildAlertDialog(
                this,
                getString(R.string.please_set_your_time_automatically),
                getString(R.string.go_to_settings),
            ) {
                startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
            }
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun initListeners() {
        binding.loginButton.setOnClickListener {
            checkPermission()
        }

        binding.signupButton.setOnClickListener {
            val signupFragment = SignupFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, signupFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when(event) {
                    is LoginViewModel.UiEvent.ShowToast -> {
                        toast(event.message)
                    }
                    is LoginViewModel.UiEvent.SuccessfullyLogin -> {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private fun checkPermission() {
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )) {
            viewModel.login(
                binding.usernameInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        } else {
            EasyPermissions.requestPermissions(
                host = this,
                rationale = getString(R.string.location_permission_required),
                requestCode = REQUEST_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            toast(getString(R.string.location_permission_required))
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        private const val REQUEST_PERMISSION = 1
    }

}