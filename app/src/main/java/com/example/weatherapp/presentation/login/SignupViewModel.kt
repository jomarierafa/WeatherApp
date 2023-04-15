package com.example.weatherapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.InvalidUserException
import com.example.weatherapp.data.local.User
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

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                userUseCases.insertUser(user)
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