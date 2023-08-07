package com.muhammhassan.epatrol.core.datasource.remote.api

import com.muhammhassan.epatrol.core.model.BaseResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<LoginResponse>>

    @GET("patrol/")
    suspend fun getPatrolTask(): Response<BaseResponse<List<PatrolResponse>>>

    @PUT("patrol/{id}/verify")
    suspend fun verifyPatrol(@Path("id") id: Long): Response<BaseResponse<Nothing>>

    @GET("patrol/{id}")
    suspend fun getDetail(@Path("id")id: Long): Response<BaseResponse<PatrolDetailResponse>>
}