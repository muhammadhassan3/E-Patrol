package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.LoginResponse
import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UserRepositoryImpl(private val remote: RemoteDataSource, private val local: LocalDataSource): UserRepository {
    override suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return remote.login(email, password).map {
            if(it is ApiResponse.Success){
                local.setUser(email = it.data.user.email!!, nrp = it.data.user.nrp!!, nama = it.data.user.name!!)
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
}
