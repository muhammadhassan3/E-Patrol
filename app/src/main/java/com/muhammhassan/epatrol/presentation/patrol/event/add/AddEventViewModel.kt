package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.AddEventUseCase
import com.muhammhassan.epatrol.utils.reduceFileImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEventViewModel(private val useCase: AddEventUseCase) : ViewModel() {
    var patrolId = 0L
    var latitude = 0.0
    var longitude = 0.0
    private var name = ""
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

    init {
        getUser()
    }

    fun save() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            if (latitude == 0.0 || longitude == 0.0) {
                _state.value =
                    UiState.Error("Data lokasi tidak ditemukan, silahkan coba beberapa saat lagi")
                return@launch
            }

            if(image.value == null){
                _state.value = UiState.Error("Silahkan upload gambar terlebih dahulu")
                return@launch
            }

            if (!arrayOf(
                    action.value.trim(),
                    event.value.trim(),
                    summary.value.trim()
                ).all { it.isNotEmpty() }
            ) {
                _state.value = UiState.Error("Pastikan semua kolom sudah terisi ya")
                return@launch
            }
            val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val calendar = Calendar.getInstance()
            val date = dateFormater.format(calendar.time)
            if (image.value != null) {
                useCase.addEvent(
                    patrolId = patrolId,
                    event = event.value,
                    summary = summary.value,
                    action = action.value,
                    image = reduceFileImage(File(image.value!!.path!!)),
                    lat = latitude,
                    long = longitude,
                    authorName = name,
                    date = date
                ).collect {
                    _state.value = it
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            val user = useCase.getUser().first()
            name = user.name
        }
    }
}