package com.muhammhassan.epatrol.presentation.home.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.DashboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardViewModel(private val useCase: DashboardUseCase): ViewModel()
{
    private val _taskList = MutableStateFlow<UiState<List<PatrolModel>>>(UiState.Loading)
    val taskList = _taskList.asStateFlow()

    private val _verifyState = MutableStateFlow<UiState<Nothing>?>(null)
    val verifyState = _verifyState.asStateFlow()

    private val _user = MutableStateFlow(UserModel("Gagal memuat data user", "","", "", ""))
    val user = _user.asStateFlow()

    init{
        viewModelScope.launch {
            val user = useCase.getUser().first()
            _user.value = user
            return@launch
        }

        getTask()
    }

    fun getTask(){
        viewModelScope.launch {
            useCase.getTaskList().collect{
                _taskList.value = it
            }
        }
    }

    fun verifyPatrol(id: Long){
        viewModelScope.launch {
            useCase.verifyPatrolTask(id).collect{
                _verifyState.value = it
            }
        }
    }
}