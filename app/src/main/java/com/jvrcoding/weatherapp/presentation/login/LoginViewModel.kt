package com.jvrcoding.weatherapp.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.weatherapp.data.local.InvalidUserException
import com.jvrcoding.weatherapp.domain.use_case.user.UserUseCases
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
        }
    }


    fun login() {
        viewModelScope.launch {
            try {
                userUseCases.getUser(state.username, state.password)
                _eventFlow.emit(UiEvent.SuccessfullyLogin)
            } catch (e: InvalidUserException) {
                _eventFlow.emit(
                    UiEvent.ShowToast(
                        message = e.message ?: "Unknown Error"
                    )
                )
            }
        }
    }


    sealed class UiEvent {
        data class ShowToast(val message: String): UiEvent()
        object SuccessfullyLogin: UiEvent()
    }
}