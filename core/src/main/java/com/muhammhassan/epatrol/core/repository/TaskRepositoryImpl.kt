package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val remoteDataSource: RemoteDataSource): TaskRepository {
    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>> {
        return remoteDataSource.getTaskList()
    }
}