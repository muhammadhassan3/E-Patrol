package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>>
}