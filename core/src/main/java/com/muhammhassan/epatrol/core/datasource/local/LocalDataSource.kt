package com.muhammhassan.epatrol.core.datasource.local

import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun setUser(email: String, nrp: String, nama: String, jabatan: String, profileUrl: String)
    fun getSavedUser(): Flow<UserModel>
    suspend fun setToken(token: String)
    fun getToken(): Flow<String?>
    fun getEmail(): Flow<String?>

    suspend fun clearLocal()

    suspend fun setSubsToGlobalChannel(value: Boolean)
    fun isSubsToGlobalChannel(): Flow<Boolean>
}