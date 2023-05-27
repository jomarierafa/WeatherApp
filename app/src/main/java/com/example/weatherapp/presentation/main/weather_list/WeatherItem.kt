package com.example.weatherapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.common.*
import com.example.weatherapp.domain.model.Weather

@Composable
fun WeatherItem(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {

            AsyncImage(
                model = "${Constant.OPEN_WEATHER_IMAGE_URL}${weather.icon}@4x.png",
                contentDescription = "weather image",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text =  "${weather.temperature.kelvinToCelsius()} ${context.getString(R.string.celsius)}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "${weather.city}, ${weather.country.getCountryName()}",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Sunrise ${weather.sunriseTime.convertUtcToLocaleTime()}",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Sunset ${weather.sunsetTime.convertUtcToLocaleTime()}",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = weather.timeCreated.epochToString("MMMM dd, yyyy hh:mm a"),
                    style = MaterialTheme.typography.caption
                )
            }
        }
        Spacer(modifier = Modifier.height(1.dp).background(Color.LightGray))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherCard() {
//    WeatherItem()
}