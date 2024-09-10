package com.jvrcoding.weatherapp.presentation.main.current_weather

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.*
import com.jvrcoding.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    state: CurrentWeatherState,
    onEvent: (CurrentWeatherEvent) -> Unit,
    uiEvent: Flow<CurrentWeatherViewModel.UiEvent>
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is CurrentWeatherViewModel.UiEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.error.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = rememberPullToRefreshState(),
        isRefreshing = state.isLoading,
        onRefresh = { onEvent(CurrentWeatherEvent.SwipeRefresh) }) {
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
                elevation = CardDefaults.cardElevation(4.dp)
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
                            text = state.weather?.let { "Today ${it.timeCreated.epochToString("h:mm a")}" }
                                ?: "",
                            style = MaterialTheme.typography.bodyMedium,
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
                            text = state.weather?.let {
                                "${it.temperature.kelvinToCelsius()} ${
                                    context.getString(
                                        R.string.celsius
                                    )
                                }"
                            } ?: "",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.weather?.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.weather?.let { "${it.city}, ${it.country.getCountryName()}" }
                                ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.weather?.let { "Sunrise ${it.sunriseTime.convertUtcToLocaleTime()}" }
                                    ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = state.weather?.let { "Sunset ${it.sunsetTime.convertUtcToLocaleTime()}" }
                                    ?: "",
                                style = MaterialTheme.typography.bodyMedium,
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
        onEvent = {},
        uiEvent = emptyFlow()
    )
}