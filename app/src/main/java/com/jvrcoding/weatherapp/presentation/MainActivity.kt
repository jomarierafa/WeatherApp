package com.jvrcoding.weatherapp.presentation

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.jvrcoding.to_do_app.ui.theme.WeatherAppTheme
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.presentation.util.Screen
import com.jvrcoding.weatherapp.common.buildAlertDialog
import com.jvrcoding.weatherapp.common.isAutomaticTimeEnabled
import com.jvrcoding.weatherapp.presentation.main.MainScreen
import com.jvrcoding.weatherapp.presentation.login.LoginViewModel
import com.jvrcoding.weatherapp.presentation.signup.SignupViewModel
import com.jvrcoding.weatherapp.presentation.login.LoginScreen
import com.jvrcoding.weatherapp.presentation.main.current_weather.CurrentWeatherViewModel
import com.jvrcoding.weatherapp.presentation.main.weather_list.WeatherListViewModel
import com.jvrcoding.weatherapp.presentation.signup.SignupScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addDynamicShortcut()

        if (!isAutomaticTimeEnabled(this)) {
            buildAlertDialog(
                this,
                getString(R.string.please_set_your_time_automatically),
                getString(R.string.go_to_settings),
            ) {
                startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
            }
        }

        installSplashScreen()
        setContent {
            WeatherAppTheme(dynamicColor = false) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.LoginScreen.route
                ) {
                    composable(route = Screen.LoginScreen.route) {
                        val viewModel = hiltViewModel<LoginViewModel>()
                        LoginScreen(
                            navController = navController,
                            state = viewModel.state,
                            uiEvent = viewModel.eventFlow,
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(route = Screen.SignUpScreen.route) {
                        val viewModel = hiltViewModel<SignupViewModel>()
                        SignupScreen(
                            navController = navController,
                            state = viewModel.state,
                            uiEvent = viewModel.eventFlow,
                            onEvent = viewModel::onEVent
                        )
                    }

                    composable(route = Screen.MainScreen.route) {
                        val currentWeatherViewModel = hiltViewModel<CurrentWeatherViewModel>()
                        val weatherListViewModel = hiltViewModel<WeatherListViewModel>()
                        MainScreen(
                            navController = navController,
                            currentWeatherViewModel = currentWeatherViewModel,
                            weatherListViewModel = weatherListViewModel
                        )
                    }

                    composable(
                        route = "deeplink",
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "weather://${getString(R.string.app_scheme_host)}/{id}"
                                action = Intent.ACTION_VIEW
                            },
                            navDeepLink {
                                uriPattern = "https://${getString(R.string.app_scheme_host)}/{id}"
                                action = Intent.ACTION_VIEW
                            }
                        ),
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { navBackStackEntry ->
                        when (navBackStackEntry.arguments?.getInt("id")) {
                            Screen.SignUpScreen.deeplinkId -> navController.navigate(Screen.SignUpScreen.route) {
                                popUpTo(Screen.LoginScreen.route)
                            }
                            else -> navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }

                    }
                }
            }
        }

        //handle intent came from notification that received when app is in background/Killed
        //handleIntent(intent)

    }

    override fun onResume() {
        checkGPS()
        super.onResume()
    }

    private fun checkGPS() {
        if (!isGPSEnabled()) buildAlertMessageNoGps()
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun buildAlertMessageNoGps() {
        buildAlertDialog(
            this,
            getString(R.string.your_gps_seems_to_be_disabled),
            getString(R.string.go_to_settings),
        ) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        handleIntent(intent)
    }

    private fun addDynamicShortcut() {
        val shortcut = ShortcutInfoCompat.Builder(applicationContext, "dynamic")
            .setShortLabel(getString(R.string.register))
            .setLongLabel("Create new account")
            .setIcon(
                IconCompat.createWithResource(
                    applicationContext, R.drawable.baseline_app_shortcut_24
                )
            )
            .setIntent(
                Intent(applicationContext, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    data =
                        Uri.parse("weather://${getString(R.string.app_scheme_host)}/${Screen.SignUpScreen.deeplinkId}")
                    putExtra("shortcut_id", "dynamic")
                }
            )
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(applicationContext, shortcut)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {}

    }
}


