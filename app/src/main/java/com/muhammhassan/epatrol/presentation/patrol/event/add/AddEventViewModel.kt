package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.AddEventUseCase
import com.muhammhassan.epatrol.utils.uriToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEventViewModel(private val useCase: AddEventUseCase) : ViewModel() {
    var patrolId = 0L
    var latitude = 0.0
    var longitude = 0.0
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()

    private val _state: MutableStateFlow<UiState<Nothing>?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow("")
    val event = _event.asStateFlow()

    private val _summary = MutableStateFlow("")
    val summary = _summary.asStateFlow()

    private val _action = MutableStateFlow("")
    val action = _action.asStateFlow()


    fun setEvent(value: String) {
        _event.value = value
    }

    fun setSummary(value: String) {
        _summary.value = value
    }

    fun setAction(value: String) {
        _action.value = value
    }

    fun setImage(value: Uri) {
        _image.value = value
    }

    fun save(context: Context) {
        viewModelScope.launch {
            if(latitude == 0.0 || longitude == 0.0){
                _state.value = UiState.Error("Data lokasi tidak ditemukan, silahkan coba beberapa saat lagi")
                return@launch
            }
            if (image.value != null) {
                useCase.addEvent(
                    patrolId = patrolId,
                    event = event.value,
                    summary = summary.value,
                    action = action.value,
                    image = uriToFile(image.value!!, context),
                    lat = latitude,
                    long = longitude
                ).collect {
                    _state.value = it
                }
            }
        }
    }
}