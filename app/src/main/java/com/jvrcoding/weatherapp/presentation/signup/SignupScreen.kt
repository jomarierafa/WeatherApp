package com.jvrcoding.weatherapp.presentation.signup


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.toast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(
    navController: NavController,
    state: SignupState,
    uiEvent: Flow<SignupViewModel.UiEvent>,
    onEvent: (SignupEvent) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when (event) {
                is SignupViewModel.UiEvent.SaveUser -> {
                    context.toast(context.getString(R.string.successfully_registered))
                    navController.navigateUp()
                }

                is SignupViewModel.UiEvent.ShowToast -> {
                    context.toast(event.message.asString(context))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.space_lg)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.register),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.space_lg))
                .wrapContentSize(align = Alignment.Center)
        )

        OutlinedTextField(
            value = state.firstname,
            onValueChange = { onEvent(SignupEvent.FirstnameChanged(it)) },
            label = { Text(text = stringResource(id = R.string.first_name)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.lastname,
            onValueChange = { onEvent(SignupEvent.LastnameChanged(it)) },
            label = { Text(text = stringResource(id = R.string.last_name)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = { onEvent(SignupEvent.UsernameChanged(it)) },
            label = { Text(text = stringResource(id = R.string.username)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(SignupEvent.PasswordChanged(it)) },
            label = { Text(text = stringResource(id = R.string.password)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (state.passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (state.passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {
                    onEvent(SignupEvent.TogglePasswordVisibility(!state.passwordVisible))
                }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { onEvent(SignupEvent.ConfirmPasswordChanged(it)) },
            label = { Text(text = stringResource(id = R.string.confirm_password)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (state.passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (state.confirmPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = {
                    onEvent(SignupEvent.ToggleConfirmPasswordVisibility(!state.confirmPasswordVisible))
                }) {
                    Icon(imageVector = image, description)
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
            onClick = { onEvent(SignupEvent.Signup) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.space_sm))
            )
        }

        OutlinedButton(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.space_md)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
            border = BorderStroke(width = Dp(1F), color = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.space_sm))
            )
        }
    }
}