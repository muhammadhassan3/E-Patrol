package com.muhammhassan.epatrol.presentation.auth.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginViewModel(private val useCase: LoginUseCase) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()


    init {
        viewModelScope.launch {
            val email = useCase.getSavedEmail().firstOrNull()
            _email.value = email ?: ""
        }
    }

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun login(): StateFlow<UiState<UserModel>?> {
        val state = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email.value).matches()) {
            state.value = UiState.Error("Silahkan masukkan email yang valid")
            return state
        }

        viewModelScope.launch {
            val token = useCase.getToken().firstOrNull()
            useCase.login(email.value, password.value, token).collect {
                state.value = it
            }
        }

        return state
    }
}