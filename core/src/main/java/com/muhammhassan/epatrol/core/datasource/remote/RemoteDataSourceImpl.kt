package com.muhammhassan.epatrol.core.datasource.remote

import com.muhammhassan.epatrol.core.datasource.remote.api.ApiInterface
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventData
import com.muhammhassan.epatrol.core.model.PatrolItemResponse
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
    override suspend fun login(
        email: String, password: String, token: String?
    ): Flow<ApiResponse<LoginResponse>> = flow {
        emit(ApiResponse.Loading)
        val response = api.login(email, password, token)
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

    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getPatrolTask()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                emit(ApiResponse.Success(it))
            }
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch { error ->
        Timber.e(error)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun getCompletedTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>> =
        flow {
            emit(ApiResponse.Loading)
            val response = api.getCompletedPatrolTask()
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    emit(ApiResponse.Success(it))
                }
            } else if (response.code() == 401) {
                emit(ApiResponse.NeedLogin)
            } else {
                emit(ApiResponse.Error(response.parseError()))
            }
        }.catch {
            Timber.e(it)
            emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
        }

    override suspend fun getTaskEventList(patrolId: Long): Flow<ApiResponse<List<PatrolEventData>>> =
        flow {
            emit(ApiResponse.Loading)
            val response = api.getPatrolEvent(patrolId)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    emit(ApiResponse.Success(it))
                }
            } else if (response.code() == 401) {
                emit(ApiResponse.NeedLogin)
            } else {
                emit(
                    if (response.code() == 404) ApiResponse.Success(
                        emptyList<PatrolEventData>()
                    )
                    else ApiResponse.Error(
                        response.parseError()
                    )
                )
            }
        }.catch {
            Timber.e(it)
            emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
        }

    override suspend fun getEventDetail(eventId: Long): Flow<ApiResponse<EventDetailResponse>> =
        flow {
            emit(ApiResponse.Loading)
            val response = api.getEventDetail(eventId)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    emit(ApiResponse.Success(it))
                }
            } else if (response.code() == 401) {
                emit(ApiResponse.NeedLogin)
            } else {
                emit(ApiResponse.Error(response.parseError()))
            }
        }.catch {
            Timber.e(it)
            emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
        }

    override suspend fun verifyPatrol(id: Long): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.verifyPatrol(id)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun getPatrolDetail(id: Long): Flow<ApiResponse<PatrolDetailResponse>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getPatrolDetail(id)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(response.body()?.data))
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
        } else {
            emit(ApiResponse.Error(response.parseError()))
        }
    }.catch {
        Timber.e(it)
        emit(ApiResponse.Error(NETWORK_FAILURE_MESSAGE))
    }

    override suspend fun deletePatrolEvent(
        eventId: Long
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val response = api.deleteEvent(eventId)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
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
        long: Double,
        authorName: String,
        date: String
    ): Flow<ApiResponse<Nothing>> = flow {
        emit(ApiResponse.Loading)
        val actionPart = action.toRequestBody("text/plain".toMediaType())
        val eventPart = event.toRequestBody("text/plain".toMediaType())
        val summaryPart = summary.toRequestBody("text/plain".toMediaType())
        val latitudePart = lat.toString().toRequestBody("text/plain".toMediaType())
        val longitudePart = long.toString().toRequestBody("text/plain".toMediaType())
        val authorPart = authorName.toRequestBody("text/plain".toMediaType())
        val imageFile = image.asRequestBody("image/jpeg".toMediaType())
        Timber.e((image.length()/ 1024L).toString())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto", image.name, imageFile
        )
        val datePart = date.toRequestBody("text/plain".toMediaType())

        //Use this when request type is Multipart
        val response = api.addEvent(
            patrolId = patrolId,
            event = eventPart,
            image = imageMultipart,
            action = actionPart,
            summary = summaryPart,
            latitude = latitudePart,
            longitude = longitudePart,
            author = authorPart,
            date = datePart
        )

        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
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
        if (response.isSuccessful) {
            emit(ApiResponse.Success(null))
        } else if (response.code() == 401) {
            emit(ApiResponse.NeedLogin)
        } else {
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