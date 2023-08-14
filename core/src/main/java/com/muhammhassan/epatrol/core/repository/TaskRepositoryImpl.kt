package com.muhammhassan.epatrol.core.repository

import android.net.Uri
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val remoteDataSource: RemoteDataSource) : TaskRepository {
    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>> {
        return remoteDataSource.getTaskList()
    }

    override suspend fun verifyPatrolTask(id: Long): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.verifyPatrol(id)
    }

    override suspend fun getDetailPatrol(id: Long): Flow<ApiResponse<PatrolDetailResponse>> {
        return remoteDataSource.getPatrolDetail(id)
    }

    override suspend fun deletePatrolEvent(
        patrolId: Long, eventId: Long
    ): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.deletePatrolEvent(patrolId, eventId)
    }

    override suspend fun addPatrolEvent(
        patrolId: Long, event: String, summary: String, action: String, image: Uri
    ): Flow<ApiResponse<Nothing>> {
        return remoteDataSource.addPatrolEvent(patrolId, event, summary, action, image)
    }
}