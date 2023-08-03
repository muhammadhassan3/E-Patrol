package com.muhammhassan.epatrol.core.datasource.remote

import com.muhammhassan.epatrol.core.datasource.remote.api.ApiInterface
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import com.muhammhassan.epatrol.core.utils.Utils.parseError
import kotlinx.coroutines.delay
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
            error.printStackTrace()
            error.message?.let {
                emit(ApiResponse.Error(it))
            }
        }

    override suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>> =
        flow<ApiResponse<List<PatrolResponse>>> {
            Timber.e("Invoked")
            emit(ApiResponse.Loading)
            delay(800)
            emit(
                ApiResponse.Success(
                    listOf(
                        PatrolResponse(
                            status = "belum-dikerjakan",
                            alamat = "Jl Juanda",
                            jam = "20:00",
                            judul = "Patroli Rutin",
                            ketua = "ade@email.co",
                            tanggal = "20 Juli 2023",
                            verified = false,
                            id = 1
                        ), PatrolResponse(
                            status = "belum-dikerjakan",
                            alamat = "Jl Gatot Subroto",
                            jam = "15:00",
                            judul = "Patroli Rutin Harian",
                            ketua = "budi@email.co",
                            tanggal = "21 Juli 2023",
                            verified = false,
                            id = 2
                        ), PatrolResponse(
                            status = "sudah-dikerjakan",
                            alamat = "Jl Ahmad yani",
                            jam = "14:00",
                            judul = "Patroli Rutin Mingguan",
                            ketua = "budi@email.co",
                            tanggal = "19 Juli 2023",
                            verified = true,
                            id = 3
                        )
                    )
                )
            )
        }.catch { error ->
            error.printStackTrace()
            error.message?.let {
                emit(ApiResponse.Error(it))
            }
        }
}