package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventData
import com.muhammhassan.epatrol.core.model.PatrolItemResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

class TaskRepositoryImpl(private val remoteDataSource: RemoteDataSource) : TaskRepository {
    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>> {
        return remoteDataSource.getTaskList()
    }

    override suspend fun getCompletedTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>> {
        return remoteDataSource.getCompletedTaskList()
    }

    override suspend fun verifyPatrolTask(id: Long): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.verifyPatrol(id)
    }

    override suspend fun getDetailPatrol(id: Long): Flow<ApiResponse<PatrolDetailResponse>> {
        return remoteDataSource.getPatrolDetail(id)
    }

    override suspend fun getEventList(patrolId: Long): Flow<ApiResponse<List<PatrolEventData>>> {
        return remoteDataSource.getTaskEventList(patrolId)
    }

    override suspend fun getEventDetail(eventId: Long): Flow<ApiResponse<EventDetailResponse>> {
        return remoteDataSource.getEventDetail(eventId)
    }

    override suspend fun deletePatrolEvent(
        eventId: Long
    ): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.deletePatrolEvent(eventId)
    }

    override suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        latitude: Double,
        longitude: Double,
        authorName: String,
        date: String
    ): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.addPatrolEvent(
            patrolId,
            event,
            summary,
            action,
            image,
            latitude,
            longitude,
            authorName,
            date
        )
    }

    override suspend fun markAsDone(patrolId: Long): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.markAsDonePatrol(patrolId)
    }
}