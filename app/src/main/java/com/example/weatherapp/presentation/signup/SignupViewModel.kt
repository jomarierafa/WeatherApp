package com.example.weatherapp.presentation.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.InvalidUserException
import com.example.weatherapp.domain.use_case.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    var state by mutableStateOf(SignupState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEVent(event: SignupEvent) {
        when(event) {
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
            is SignupEvent.Signup -> {
                Log.d("awit", "here")
                addUser()
            }
        }
    }

    private fun addUser() {
        viewModelScope.launch {
            try {
                userUseCases.insertUser(state.toUser())
                _eventFlow.emit(UiEvent.SaveUser)
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
        object SaveUser: UiEvent()
    }

}