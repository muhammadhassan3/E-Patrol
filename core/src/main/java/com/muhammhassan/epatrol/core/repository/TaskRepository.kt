package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>>

    suspend fun verifyPatrolTask(id: Long): Flow<ApiResponse<Nothing>>

    suspend fun getDetailPatrol(id: Long): Flow<ApiResponse<PatrolDetailResponse>>
}