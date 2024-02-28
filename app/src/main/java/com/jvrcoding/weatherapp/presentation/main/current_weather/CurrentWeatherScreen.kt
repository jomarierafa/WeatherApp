package com.jvrcoding.weatherapp.presentation.main.current_weather

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.*
import com.jvrcoding.weatherapp.domain.model.Weather
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun CurrentWeatherScreen(
    state: CurrentWeatherState,
    onEvent: (CurrentWeatherEvent) -> Unit
) {
    val context = LocalContext.current

    SwipeRefresh(
        state = rememberSwipeRefreshState(state.isLoading),
        onRefresh = {
            onEvent(CurrentWeatherEvent.SwipeRefresh)
        },
        modifier = Modifier.fillMaxSize(),
        indicator = { _state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = _state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            val contentLayout = createRef()

            Card(
                modifier = Modifier
                    .constrainAs(contentLayout) {
                        top.linkTo(parent.top)
                    }
                    .padding(dimensionResource(R.dimen.space_lg))
                    .fillMaxWidth(),
                elevation = Dp(4f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.space_md))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = state.weather?.let { "Today ${it.timeCreated.epochToString("h:mm a")}" }  ?: "",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )

                        AsyncImage(
                            model = "${Constant.OPEN_WEATHER_IMAGE_URL}${state.weather?.icon}@4x.png",
                            contentDescription = "weather image",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = state.weather?.let { "${it.temperature.kelvinToCelsius()} ${context.getString(R.string.celsius)}" } ?: "",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.weather?.description ?: "",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.weather?.let { "${it.city}, ${it.country.getCountryName()}" } ?: "",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.weather?.let { "Sunrise ${it.sunriseTime.convertUtcToLocaleTime()}" } ?: "",
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = state.weather?.let { "Sunset ${it.sunsetTime.convertUtcToLocaleTime()}" } ?: "",
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewMyApp() {
    CurrentWeatherScreen(
        state = CurrentWeatherState(
            isLoading = false,
            weather = Weather(
                id = 0,
                weather = "sun",
                description = "awit",
                city = "Lian",
                country = "Philippines",
                icon = "",
                sunsetTime = 0L,
                sunriseTime = 0L,
                username = "",
                timeCreated = 0L,
                temperature = 5.6
            )
        ),
        onEvent = {}
    )
}