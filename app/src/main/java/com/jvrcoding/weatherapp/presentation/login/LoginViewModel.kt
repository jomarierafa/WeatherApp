package com.jvrcoding.weatherapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.weatherapp.domain.use_case.user.UserUseCases
import com.jvrcoding.weatherapp.domain.util.ifError
import com.jvrcoding.weatherapp.domain.util.ifSuccess
import com.jvrcoding.weatherapp.presentation.util.UiText
import com.jvrcoding.weatherapp.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    var state by mutableStateOf(LoginState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.Login -> {
               login()
            }
            is LoginEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }
            is LoginEvent.OnPermissionResult -> {
                if(!event.isGranted && !state.visiblePermissionDialogQueue.contains(event.permission)) {
                    val updatedQueue = state.visiblePermissionDialogQueue.toMutableList()
                    updatedQueue.add(event.permission)
                    state = state.copy(visiblePermissionDialogQueue = updatedQueue)
                }
            }
            is LoginEvent.DismissDialog -> {
                if(state.visiblePermissionDialogQueue.isNotEmpty()) {
                    val updatedQueue = state.visiblePermissionDialogQueue.toMutableList()
                    updatedQueue.clear()
                    state = state.copy(visiblePermissionDialogQueue = updatedQueue)
                }
            }
            is LoginEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }

            is LoginEvent.TogglePasswordVisibility -> {
                state = state.copy(passwordVisible = event.isPasswordVisible)
            }
        }
    }


    fun login() {
        viewModelScope.launch {
            userUseCases.getUser(state.username, state.password)
                .ifSuccess {
                    _eventFlow.emit(UiEvent.SuccessfullyLogin)
                }
                .ifError { error ->
                    _eventFlow.emit(UiEvent.ShowToast(error.asUiText()))
                }
        }
    }


    sealed class UiEvent {
        data class ShowToast(val message: UiText): UiEvent()
        object SuccessfullyLogin: UiEvent()
    }
}