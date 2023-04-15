package com.example.weatherapp.presentation.main.fragments.weather_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.common.longToast
import com.example.weatherapp.databinding.FragmentWeatherListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherListFragment : Fragment() {

    private val viewModel: WeatherListViewModel by viewModels()
    private lateinit var binding: FragmentWeatherListBinding

    private var adapter: WeatherListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }

    private fun initViews() {
        adapter = WeatherListAdapter(requireContext())

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WeatherListFragment.adapter
        }

    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { currentState ->
                currentState.weathers.let {
                   adapter?.submitUpdate(it.toMutableList())
                }
                if(currentState.error.isNotBlank()) {
                    requireContext().longToast(currentState.error)
                }
            }
        }
    }



    companion object {
        fun newInstance() = WeatherListFragment()
    }
}