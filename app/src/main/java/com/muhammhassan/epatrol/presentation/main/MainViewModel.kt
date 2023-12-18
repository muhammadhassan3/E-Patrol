package com.muhammhassan.epatrol.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.muhammhassan.epatrol.domain.usecase.MainUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(private val useCase: MainUseCase) : ViewModel() {
    private val _isSubsGlobalChannel = MutableLiveData<Boolean>()
    val isSubsGlobalChannel: LiveData<Boolean> = _isSubsGlobalChannel

    private val messages = FirebaseMessaging.getInstance()

    init {
        isSubscribedToGlobalChannel()
    }

    private fun isSubscribedToGlobalChannel() {
        viewModelScope.launch {
            val value = useCase.isSubsToGlobalChannel().first()
            _isSubsGlobalChannel.postValue(value)
        }
    }

    fun subsToGlobalChannel(){
        messages.subscribeToTopic("/general").addOnSuccessListener {
            setSubscribedToGlobalChannel()
        }
    }

    private fun setSubscribedToGlobalChannel() {
        viewModelScope.launch {
            useCase.setSubsToGlobalChannel(true)
        }
    }
}