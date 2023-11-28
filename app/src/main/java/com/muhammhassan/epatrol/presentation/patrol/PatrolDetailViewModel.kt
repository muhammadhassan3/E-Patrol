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

    var orderId = 0L

    init {
        viewModelScope.launch {
            val email = useCase.getSavedEmail().first()
            _email.value = email ?: "Gagal memuat email"
            return@launch
        }
    }

    fun getDetail() {
        viewModelScope.launch {
            useCase.getDetailPatrol(orderId).collect {
                if (it is UiState.Success) {
                    it.data?.let { data -> getPatrolEvents(data.patrolId) }
                }
                _state.value = it
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

    private suspend fun getPatrolEvents(orderId: Long) {

        useCase.getEventList(orderId).collect {
            _eventState.value = it
        }
    }
}