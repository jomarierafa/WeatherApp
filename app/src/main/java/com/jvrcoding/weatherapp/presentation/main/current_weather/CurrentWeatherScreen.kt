package com.jvrcoding.weatherapp.presentation.main.current_weather

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.*
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.presentation.util.shareImage
import com.jvrcoding.weatherapp.presentation.util.toBitmap
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.space_sm)),
        state = rememberPullToRefreshState(),
        isRefreshing = state.isLoading,
        onRefresh = { onEvent(CurrentWeatherEvent.SwipeRefresh) }) {

        WeatherCard(
            state = state,
            context = context,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun WeatherCard(
    context: Context,
    state: CurrentWeatherState,
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .padding(dimensionResource(R.dimen.space_md))
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        val view = LocalView.current
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.space_md))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Weather",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_share),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            view.toBitmap(
                                onBitmapReady = { bitmap: Bitmap ->
                                    shareImage(context, bitmap)
                                },
                                onBitmapError = { exception: Exception ->
                                    Log.d("awit", "$exception")
                                }
                            )
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_rotate),
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (column, image) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(column) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(image.start)
                            width = Dimension.fillToConstraints
                        }
                ) {
                    Text(
                        text = state.weather?.let {
                            "${it.city}, ${it.country.getCountryName()}"
                        } ?: "",
                        maxLines = 2,
                        lineHeight = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = state.weather?.description ?: "",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                }

                AsyncImage(
                    model = "${Constant.OPEN_WEATHER_IMAGE_URL}${state.weather?.icon}@4x.png",
                    contentDescription = "weather image",
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(column.top)
                            bottom.linkTo(column.bottom)
                            end.linkTo(parent.end)
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .padding(dimensionResource(id = R.dimen.space_sm))
            ) {
                WeatherDetailRow(
                    label =  "Temp",
                    value = "${state.weather?.temperature?.kelvinToCelsius() ?: "" } ${context.getString(R.string.celsius)}"
                )
                WeatherDetailRow(
                    label = "Time",
                    value = "Today ${state.weather?.timeCreated?.epochToString("h:mm a")}"
                )
                WeatherDetailRow(
                    label = "Sunrise",
                    value = state.weather?.sunriseTime?.convertUtcToLocaleTime() ?: ""
                )
                WeatherDetailRow(
                    label = "Sunset",
                    value = state.weather?.sunsetTime?.convertUtcToLocaleTime() ?: ""
                )
            }
        }
    }
}

@Composable
fun WeatherDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.space_xxs)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
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
                description = "Mostly Cloudy",
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