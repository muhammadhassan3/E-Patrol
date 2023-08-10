package com.muhammhassan.epatrol.presentation.patrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.PatrolDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PatrolDetailViewModel(private val useCase: PatrolDetailUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UiState<PatrolDetailModel>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    init {
        viewModelScope.launch {
            val email = useCase.getSavedEmail().first()
            _email.value = email ?: "Gagal memuat email"
            return@launch
        }
    }

    fun getDetail(id: Long) {
        viewModelScope.launch {
            useCase.getDetailPatrol(id).collect {
                _state.value = it
            }
        }
    }
}