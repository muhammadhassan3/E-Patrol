package com.muhammhassan.epatrol.core.datasource.local

import com.muhammhassan.epatrol.core.datasource.local.datastore.DataStorePreferences
import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val datastore: DataStorePreferences) : LocalDataSource {
    override suspend fun setUser(
        email: String, nrp: String, nama: String, jabatan: String, profileUrl: String
    ) {
        datastore.setUser(email, nrp, nama, jabatan, profileUrl)
    }

    override fun getSavedUser(): Flow<UserModel> {
        return datastore.getUser()
    }

    override suspend fun setToken(token: String) {
        datastore.setToken(token)
    }

    override fun getToken(): Flow<String?> {
        return datastore.getToken()
    }

    override fun getEmail(): Flow<String?> {
        return datastore.getEmail()
    }

    override suspend fun clearLocal() {
        datastore.clear()
    }

    override suspend fun setSubsToGlobalChannel(value: Boolean) {
        datastore.setSubscribedToGlobalChannel(value)
    }

    override fun isSubsToGlobalChannel(): Flow<Boolean> {
        return datastore.getSubscribedToGlobalChannel()
    }
}