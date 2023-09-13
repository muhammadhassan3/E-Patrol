package com.muhammhassan.epatrol.core.datasource.remote.api

import com.muhammhassan.epatrol.core.model.BaseResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventResponse
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
import retrofit2.http.Query

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): Response<BaseResponse<LoginResponse>>

    @GET("patroli")
    suspend fun getPatrolTask(): Response<BaseResponse<PatrolResponse>>

    @PUT("patroli/{id}")
    suspend fun verifyPatrol(
        @Path("id") id: Long, @Query("status") status: String = "sedang-dikerjakan"
    ): Response<BaseResponse<Nothing>>

    @GET("patroli/{id}")
    suspend fun getPatrolDetail(@Path("id") id: Long): Response<BaseResponse<PatrolDetailResponse>>

    //Unavaiable Endpoint
    @GET("kejadian-patroli/list-kejadian/{id}")
    suspend fun getPatrolEvent(@Path("id") id: Long): Response<BaseResponse<PatrolEventResponse>>

    @GET("kejadian-patroli/{id}")
    suspend fun getEventDetail(@Path("id") eventId: Long): Response<BaseResponse<EventDetailResponse>>

    //Uncheck endpoint
    @DELETE("patrol/{patrolId}/{eventId}")
    suspend fun deleteEvent(
        @Path("patrolId") patrolId: Long, @Path("eventId") eventId: Long
    ): Response<BaseResponse<Nothing>>

    @Multipart
    @POST("kejadian-patroli")
    suspend fun addEvent(
        @Part("id_patroli") patrolId: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("penemuan_kejadian") event: RequestBody,
        @Part("tindakan") action: RequestBody,
        @Part("uraian_kejadian") summary: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("writer") author: RequestBody
    ): Response<BaseResponse<Nothing>>

    @FormUrlEncoded
    @POST("kejadian-patroli")
    suspend fun addEventRequestBody(
        @Field("id_patroli") patrolId: Long,
        @Field("foto") image: String,
        @Field("penemuan_kejadian") event: String,
        @Field("tindakan") action: String,
        @Field("uraian_kejadian") summary: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("writer") author: String
    ): Response<BaseResponse<Nothing>>

    @PUT("patroli/{id}")
    suspend fun markAsDonePatrol(
        @Path("id") id: Long, @Query("status") status: String = "sudah-dikerjakan"
    ): Response<BaseResponse<Nothing>>
}