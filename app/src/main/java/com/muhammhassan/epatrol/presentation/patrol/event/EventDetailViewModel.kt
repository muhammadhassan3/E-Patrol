package com.muhammhassan.epatrol.presentation.patrol.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.EventDetailModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.DetailPatrolEventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EventDetailViewModel(private val useCase: DetailPatrolEventUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<EventDetailModel>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Nothing>?>(null)
    val deleteState = _deleteState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    var patrolId: Long = 0L
    var eventId: Long = 0L

    init {
        getEmail()
    }

    private fun getEmail() {
        viewModelScope.launch {
            val email = useCase.getEmail().first()
            _email.value = email
            return@launch
        }
    }

    fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            useCase.deleteEvent(patrolId, eventId).collect {
                _deleteState.value = it
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            useCase.getEventDetail(eventId).collect {
                _uiState.value = it
            }
        }
    }
}