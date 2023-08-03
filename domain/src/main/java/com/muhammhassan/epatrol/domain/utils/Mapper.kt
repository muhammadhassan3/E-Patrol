package com.muhammhassan.epatrol.domain.utils

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.domain.model.UiState

object Mapper {
    fun <T, R> ApiResponse<T>.mapToUiState(mutation: (sourceClass: T) -> R): UiState<R> {
        return when(this){
            is ApiResponse.Error -> UiState.Error(this.message)
            ApiResponse.Loading -> UiState.Loading
            is ApiResponse.Success -> UiState.Success(mutation.invoke(data))
        }
    }
}
