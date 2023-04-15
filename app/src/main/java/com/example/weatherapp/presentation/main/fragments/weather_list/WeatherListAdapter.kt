package com.example.weatherapp.presentation.main.fragments.weather_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.common.*
import com.example.weatherapp.domain.model.Weather

class WeatherListAdapter(private val context: Context): RecyclerView.Adapter<WeatherListAdapter.MyViewHolder>() {

    private var list = mutableListOf<Weather>()


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperature: TextView = itemView.findViewById(R.id.temp_text)
        val description: TextView = itemView.findViewById(R.id.desc_text)
        val location: TextView = itemView.findViewById(R.id.location_text)
        val sunrise: TextView = itemView.findViewById(R.id.sunrise_text)
        val sunset: TextView = itemView.findViewById(R.id.sunset_text)
        val timeCreated: TextView = itemView.findViewById(R.id.time_created_text)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.image.load("${Constant.OPEN_WEATHER_IMAGE_URL}${data.icon}@4x.png")

        val celsiusSymbol = context.getString(R.string.celsius)
        holder.temperature.text = "${data.temperature.kelvinToCelsius()} $celsiusSymbol"

        holder.description.text = data.description
        holder.location.text = "${data.city}, ${data.country.getCountryName()}"
        holder.sunrise.text = "Sunrise ${data.sunriseTime.convertUtcToLocaleTime()}"
        holder.sunset.text = "Sunset ${data.sunsetTime.convertUtcToLocaleTime()}"
        holder.timeCreated.text = data.timeCreated.epochToString("MMMM dd, yyyy hh:mm a")

    }

    override fun getItemCount(): Int {
        return list .size
    }

    fun submitUpdate(mutableList: MutableList<Weather>) {
        list.clear()
        list.addAll(mutableList)
        notifyDataSetChanged()
    }

}