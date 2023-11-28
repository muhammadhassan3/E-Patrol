package com.muhammhassan.epatrol.core.datasource.remote.api

import com.muhammhassan.epatrol.core.model.BaseResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventData
import com.muhammhassan.epatrol.core.model.PatrolItemResponse
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
import retrofit2.http.Query

interface ApiInterface {
    @FormUrlEncoded
    @POST("auth")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): Response<BaseResponse<LoginResponse>>

    @GET("patroli/list")
    suspend fun getPatrolTask(): Response<BaseResponse<List<PatrolItemResponse>>>

    @GET("patroli/list/all")
    suspend fun getCompletedPatrolTask(): Response<BaseResponse<List<PatrolItemResponse>>>

    @GET("patroli/{id}")
    suspend fun getPatrolDetail(@Path("id") id: Long): Response<BaseResponse<PatrolDetailResponse>>

    @PUT("patroli/verifikasi/{id}")
    suspend fun verifyPatrol(
        @Path("id") id: Long, @Query("status") status: String = "sedang-dikerjakan"
    ): Response<BaseResponse<Nothing>>

    @GET("patroli/kejadian/{id}")
    suspend fun getPatrolEvent(@Path("id") patrolId: Long): Response<BaseResponse<List<PatrolEventData>>>

    @GET("patroli/{id}/kejadian")
    suspend fun getEventDetail(@Path("id") eventId: Long): Response<BaseResponse<EventDetailResponse>>

    @Multipart
    @POST("patroli/kejadian/{id}")
    suspend fun addEvent(
        @Path("id") patrolId: Long,
        @Part image: MultipartBody.Part,
        @Part("penemuan_kejadian") event: RequestBody,
        @Part("tindakan") action: RequestBody,
        @Part("uraian_kejadian") summary: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("writer") author: RequestBody,
        @Part("tgl_kejadian") date: RequestBody,
    ): Response<BaseResponse<Nothing>>

    @DELETE("patroli/kejadian/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: Long
    ): Response<BaseResponse<Nothing>>

    @PUT("patroli/update/{id}")
    suspend fun markAsDonePatrol(
        @Path("id") id: Long, @Query("status") status: String = "sudah-dikerjakan"
    ): Response<BaseResponse<Nothing>>
}