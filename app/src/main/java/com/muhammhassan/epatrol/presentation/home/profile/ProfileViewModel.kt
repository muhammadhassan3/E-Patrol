package com.muhammhassan.epatrol.presentation.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(private val useCase: ProfileUseCase) : ViewModel() {
    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch {
            val user = useCase.getUser().first()
            _user.value = user
            return@launch
        }
    }

    fun logout() {
        viewModelScope.launch {
            useCase.logout()
        }
    }
}