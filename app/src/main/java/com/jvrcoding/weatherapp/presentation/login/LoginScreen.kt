package com.jvrcoding.weatherapp.presentation.login

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.*
import com.jvrcoding.weatherapp.presentation.screen.Main
import com.jvrcoding.weatherapp.presentation.screen.Signup
import com.jvrcoding.weatherapp.presentation.util.LocationFinePermissionTextProvider
import com.jvrcoding.weatherapp.presentation.util.PermissionDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun LoginScreen(
    navController: NavController,
    state: LoginState,
    uiEvent: Flow<LoginViewModel.UiEvent>,
    onEvent: (LoginEvent) -> Unit
) {

    val context = LocalContext.current

    val permissionsToRequest = arrayListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                onEvent(LoginEvent.OnPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                ))
            }
        }
    )


    state.visiblePermissionDialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION  -> {
                        LocationFinePermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    context as Activity,
                    permission
                ),
                onDismiss = { onEvent(LoginEvent.DismissDialog) },
                onOkClick = {
                    onEvent(LoginEvent.DismissDialog)
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = {
                    onEvent(LoginEvent.DismissDialog)
                    context.openAppSettings()
                }
            )
        }

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is LoginViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is LoginViewModel.UiEvent.SuccessfullyLogin -> {
                    navController.navigate(Main) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.space_lg)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.space_lg))
                .wrapContentSize(align = Alignment.Center),
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = {
                onEvent(LoginEvent.UsernameChanged(it)) },
            label = {
                Text(text = stringResource(id = R.string.username))
                    },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text(text = stringResource(id = R.string.password)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (state.passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (state.passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {
                    onEvent(LoginEvent.TogglePasswordVisibility(!state.passwordVisible))
                }){
                    Icon(imageVector  = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.space_sm),
                    bottom = dimensionResource(id = R.dimen.space_lg)
                )
        )

        Button(
            onClick = {
                if(isAllPermissionGranted(context, permissionsToRequest)) {
                    onEvent(LoginEvent.Login)
                } else {
                    multiplePermissionResultLauncher.launch(permissionsToRequest.toTypedArray())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = stringResource(id = R.string.login), modifier = Modifier.padding(dimensionResource(id = R.dimen.space_sm)))
        }

        OutlinedButton(
            onClick = {
                navController.navigate(Signup)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.space_md)),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = Dp(0F), pressedElevation = Dp(0F)),
            border = BorderStroke(width = Dp(1F), color = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_up),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.space_sm))
            )
        }
    }

}

class UsernameParameterProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("jom", "jomarie")

}


@Preview(showSystemUi = true)
//@PreviewScreenSizes
@Composable
fun ScreenPreview(
    @PreviewParameter(UsernameParameterProvider::class) username: String
) {
    LoginScreen(
        rememberNavController(),
        state = LoginState(
            username = username,
            password = "password"
        ),
        uiEvent = flow {},
        onEvent = {}
    )
}
