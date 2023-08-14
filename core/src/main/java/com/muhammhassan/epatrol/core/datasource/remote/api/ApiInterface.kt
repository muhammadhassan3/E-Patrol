package com.muhammhassan.epatrol.core.datasource.remote.api

import com.muhammhassan.epatrol.core.model.BaseResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): Response<BaseResponse<LoginResponse>>

    @GET("patrol/")
    suspend fun getPatrolTask(): Response<BaseResponse<List<PatrolResponse>>>

    @PUT("patrol/{id}/verify")
    suspend fun verifyPatrol(@Path("id") id: Long): Response<BaseResponse<Nothing>>

    @GET("patrol/{id}")
    suspend fun getDetail(@Path("id") id: Long): Response<BaseResponse<PatrolDetailResponse>>

    @DELETE("patrol/{patrolId}/{eventId}")
    suspend fun deleteEvent(
        @Path("patrolId") patrolId: Long,
        @Path("eventId") eventId: Long
    ): Response<BaseResponse<Nothing>>

    @Multipart
    @POST("patrol/{patrolId}/")
    suspend fun addEvent(
        @Path("patrolId") patrolId: Long,
        @Part image: MultipartBody.Part,
        @Part("event") event: RequestBody,
        @Part("action") action: RequestBody,
        @Part("summary") summary: RequestBody,
        @Part("lat") latitude: RequestBody,
        @Part("long") longitude: RequestBody
    ): Response<BaseResponse<Nothing>>
}