package com.jvrcoding.weatherapp.presentation.signup

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
class SignupViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    var state by mutableStateOf(SignupState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEVent(event: SignupEvent) {
        when (event) {
            is SignupEvent.FirstnameChanged -> {
                state = state.copy(firstname = event.value)
            }

            is SignupEvent.LastnameChanged -> {
                state = state.copy(lastname = event.value)
            }

            is SignupEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }

            is SignupEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }

            is SignupEvent.ConfirmPasswordChanged -> {
                state = state.copy(confirmPassword = event.value)
            }

            is SignupEvent.TogglePasswordVisibility -> {
                state = state.copy(passwordVisible = event.isPasswordVisible)
            }

            is SignupEvent.ToggleConfirmPasswordVisibility -> {
                state = state.copy(confirmPasswordVisible = event.isConfirmPasswordVisible)
            }

            is SignupEvent.Signup -> {
                addUser()
            }
        }
    }

    private fun addUser() {
        viewModelScope.launch {
            userUseCases.insertUser(state.toUser())
                .ifSuccess { _eventFlow.emit(UiEvent.SaveUser) }
                .ifError { error ->
                    _eventFlow.emit(
                        UiEvent.ShowToast(
                            message = error.asUiText()
                        )
                    )
                }
        }
    }


    sealed class UiEvent {
        data class ShowToast(val message: UiText) : UiEvent()
        data object SaveUser : UiEvent()
    }

}