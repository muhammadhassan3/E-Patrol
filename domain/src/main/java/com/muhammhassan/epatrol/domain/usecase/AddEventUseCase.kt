package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AddEventUseCase {
    suspend fun addEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        lat: Double,
        long: Double,
        authorName:String,
        date: String
    ): Flow<UiState<Nothing>>

    fun getUser(): Flow<UserModel>
}