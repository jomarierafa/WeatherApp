package com.example.weatherapp.presentation.login

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
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                userUseCases.getUser(username, password)
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