package com.muhammhassan.epatrol.domain.usecase

import kotlinx.coroutines.flow.Flow


interface MainUseCase {
    fun isSubsToGlobalChannel(): Flow<Boolean>
    suspend fun setSubsToGlobalChannel(value: Boolean)
}