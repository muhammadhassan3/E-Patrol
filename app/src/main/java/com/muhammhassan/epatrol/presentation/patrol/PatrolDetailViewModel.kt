package com.muhammhassan.epatrol.presentation.patrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.PatrolDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PatrolDetailViewModel(private val useCase: PatrolDetailUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UiState<PatrolDetailModel>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _eventState = MutableStateFlow<UiState<List<PatrolEventModel>>>(UiState.Loading)
    val eventState = _eventState.asStateFlow()

    private val _confirmState = MutableStateFlow<UiState<Nothing>?>(null)
    val confirmState = _confirmState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    var patrolId = 0L

    init {
        viewModelScope.launch {
            val email = useCase.getSavedEmail().first()
            _email.value = email ?: "Gagal memuat email"
            return@launch
        }
    }

    fun getDetail() {
        viewModelScope.launch {
            useCase.getDetailPatrol(patrolId).collect {
                _state.value = it
            }

            useCase.getEventList(patrolId).collect {
                _eventState.value = it
            }
        }
    }

    fun verify(id: Long) {
        viewModelScope.launch {
            useCase.markAsDone(id).collect {
                _confirmState.value = it
            }
        }
    }
}