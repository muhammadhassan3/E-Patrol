package com.muhammhassan.epatrol.core.datasource.remote

import android.net.Uri
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>>

    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>>

    suspend fun verifyPatrol(id: Long): Flow<ApiResponse<Nothing>>

    suspend fun getPatrolDetail(id: Long): Flow<ApiResponse<PatrolDetailResponse>>

    suspend fun deletePatrolEvent(patrolId: Long, eventId: Long): Flow<ApiResponse<Nothing>>

    suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: Uri
    ): Flow<ApiResponse<Nothing>>
}