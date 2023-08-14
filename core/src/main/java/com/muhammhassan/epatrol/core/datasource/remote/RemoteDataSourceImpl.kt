package com.muhammhassan.epatrol.core.datasource.remote

import android.net.Uri
import com.muhammhassan.epatrol.core.datasource.remote.api.ApiInterface
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import com.muhammhassan.epatrol.core.utils.Utils.parseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RemoteDataSourceImpl(private val api: ApiInterface) : RemoteDataSource {
    override suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> =
        flow {
            emit(ApiResponse.Loading)
            val response = api.login(email, password)
            val body = response.body()
            if (response.isSuccessful) {
                body?.data?.let {
                    emit(ApiResponse.Success(it))
                }
            } else {
                emit(ApiResponse.Error(response.parseError()))
            }
        }.catch { error ->
            Timber.e(error)
            error.message?.let {
                emit(ApiResponse.Error(it))
            }
        }

    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>> =
        flow {
            emit(ApiResponse.Loading)
            val response = api.getPatrolTask()
            if(response.isSuccessful){
                response.body()?.data?.let {
                    emit(ApiResponse.Success(it))
                }
            }else{
                emit(ApiResponse.Error(response.parseError()))
            }
        }.catch { error ->
            Timber.e(error)
            error.message?.let {
                emit(ApiResponse.Error(it))
            }
        }

    override suspend fun verifyPatrol(id: Long): Flow<ApiResponse<Nothing>> = flow{
        emit(ApiResponse.Loading)
        val response = api.verifyPatrol(id)
        if(response.isSuccessful){
            emit(ApiResponse.Success(null))
        }else{
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)

        it.message?.let{message ->
            emit(ApiResponse.Error(message))
        }
    }

    override suspend fun getPatrolDetail(id: Long): Flow<ApiResponse<PatrolDetailResponse>> = flow{
        emit(ApiResponse.Loading)
        val response = api.getDetail(id)
        if(response.isSuccessful){
            emit(ApiResponse.Success(response.body()?.data))
        }else{
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        it.message?.let {message ->
            emit(ApiResponse.Error(message))
        }
    }

    override suspend fun deletePatrolEvent(
        patrolId: Long,
        eventId: Long
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.deleteEvent(patrolId, eventId)
        if (response.isSuccessful){
            emit(ApiResponse.Success(null))
        }else{
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        it.message?.let { message ->
            emit(ApiResponse.Error(message))
        }
    }

    override suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: Uri
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.addEvent(patrolId)
        if(response.isSuccessful){
            emit(ApiResponse.Success(null))
        }else{
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        it.message?.let{message ->
            emit(ApiResponse.Error(message = message))
        }
    }
}