package com.muhammhassan.epatrol.core.datasource.remote

import com.muhammhassan.epatrol.core.datasource.remote.api.ApiInterface
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import com.muhammhassan.epatrol.core.utils.Utils.parseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File

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
            emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
        }

    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getPatrolTask()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                emit(ApiResponse.Success(it))
            }
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch { error ->
        Timber.e(error)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun verifyPatrol(id: Long): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.verifyPatrol(id)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun getPatrolDetail(id: Long): Flow<ApiResponse<PatrolDetailResponse>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getDetail(id)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(response.body()?.data))
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun deletePatrolEvent(
        patrolId: Long, eventId: Long
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.deleteEvent(patrolId, eventId)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        lat: Double,
        long: Double
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val actionPart = action.toRequestBody("text/plain".toMediaType())
        val eventPart = event.toRequestBody("text/plain".toMediaType())
        val summaryPart = summary.toRequestBody("text/plain".toMediaType())
        val latitudePart = lat.toString().toRequestBody("text/plain".toMediaType())
        val longitudePart = long.toString().toRequestBody("text/plain".toMediaType())
        val imageFile = image.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", image.name, imageFile
        )
        val response = api.addEvent(
            patrolId = patrolId,
            event = eventPart,
            image = imageMultipart,
            action = actionPart,
            summary = summaryPart,
            latitude = latitudePart,
            longitude = longitudePart
        )
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(message = NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun markAsDonePatrol(patrolId: Long): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.markAsDonePatrol(patrolId)
        if(response.isSuccessful){
            emit(ApiResponse.Success(null))
        }else{
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }


    companion object {
        private const val NETWORK_FAILURE_MESSAGE =
            "Gagal terhubung ke jaringan, silahkan periksa jaringan yang kamu gunakan"
    }
}