package com.muhammhassan.epatrol.core.datasource.local

import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun setUser(email: String, nrp: String, nama: String)
    fun getSavedUser(): Flow<UserModel>
    suspend fun setToken(token: String)
    fun getToken(): Flow<String?>
    fun getEmail(): Flow<String?>
}