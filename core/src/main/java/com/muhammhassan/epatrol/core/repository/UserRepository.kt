package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>>

    fun getUser(): Flow<UserModel>
    fun getEmail(): Flow<String?>
}