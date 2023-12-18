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

    fun isSubscribedToGlobalChannel() {
        viewModelScope.launch {
            val value = useCase.isSubsToGlobalChannel().first()
            _isSubsGlobalChannel.postValue(value)
        }
    }

    fun subsToGlobalChannel(){
        messages.subscribeToTopic("/general").addOnSuccessListener {
            setSubscribedToGlobalChannel(true)
        }
    }

    private fun setSubscribedToGlobalChannel(value: Boolean) {
        viewModelScope.launch {
            useCase.setSubsToGlobalChannel(value)
        }
    }
}