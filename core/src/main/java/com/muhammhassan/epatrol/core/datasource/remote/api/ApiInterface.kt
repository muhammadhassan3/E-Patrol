package com.muhammhassan.epatrol.core.datasource.remote.api

import com.muhammhassan.epatrol.core.model.BaseResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<LoginResponse>>

//    suspend fun getPatrolTask(): Response<BaseResponse<>>
}