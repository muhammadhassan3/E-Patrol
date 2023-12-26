package com.muhammhassan.epatrol.core.datasource.remote

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventData
import com.muhammhassan.epatrol.core.model.PatrolItemResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface RemoteDataSource {
    suspend fun login(
        email: String,
        password: String,
        token: String?
    ): Flow<ApiResponse<LoginResponse>>

    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>>
    suspend fun getCompletedTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>>

    suspend fun getTaskEventList(patrolId: Long): Flow<ApiResponse<List<PatrolEventData>>>
    suspend fun getEventDetail(eventId: Long): Flow<ApiResponse<EventDetailResponse>>

    suspend fun verifyPatrol(id: Long): Flow<ApiResponse<Nothing>>

    suspend fun getPatrolDetail(id: Long): Flow<ApiResponse<PatrolDetailResponse>>

    suspend fun deletePatrolEvent(eventId: Long): Flow<ApiResponse<Nothing>>

    suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        lat: Double,
        long: Double,
        authorName: String,
        date: String,
    ): Flow<ApiResponse<Nothing>>

    suspend fun markAsDonePatrol(
        patrolId: Long
    ): Flow<ApiResponse<Nothing>>
}