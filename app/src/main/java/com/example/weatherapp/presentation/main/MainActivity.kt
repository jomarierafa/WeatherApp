package com.example.weatherapp.presentation.main

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.presentation.login.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val logoutItem = menu.findItem(R.id.logout)
        logoutItem.setOnMenuItemClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        return true
    }


    private fun initViews() {
        val toolbar = binding.toolbarLayout.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.welcome)

        binding.viewPager.apply {
            adapter = PagerAdapter(supportFragmentManager, lifecycle)
            offscreenPageLimit = 2
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.current_weather)
                1 -> tab.text = getString(R.string.weather_list)
            }
        }.attach()
    }
}