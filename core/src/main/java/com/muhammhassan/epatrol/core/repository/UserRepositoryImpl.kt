package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val remote: RemoteDataSource, private val local: LocalDataSource) :
    UserRepository {
    override suspend fun login(
        email: String,
        password: String,
        token: String?
    ): Flow<ApiResponse<LoginResponse>> {
        return remote.login(email, password, token).map {
            if (it is ApiResponse.Success && it.data != null) {
                local.setUser(
                    email = it.data.user.email ?: "",
                    nrp = it.data.user.nrp ?: "",
                    nama = it.data.user.name ?: "",
                    jabatan = it.data.user.jabatan ?: "",
                    profileUrl = it.data.user.profile ?: ""
                )
                local.setToken(it.data.token)
            }
            it
        }
    }

    override fun getUser(): Flow<UserModel> {
        return local.getSavedUser()
    }

    override fun getEmail(): Flow<String?> {
        return local.getEmail()
    }

    override fun getToken(): Flow<String?> {
        return local.getToken()
    }

    override suspend fun clear() {
        local.clearLocal()
    }
}
