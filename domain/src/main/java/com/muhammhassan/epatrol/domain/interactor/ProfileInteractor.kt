package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileInteractor(private val user: UserRepository) : ProfileUseCase {
    override suspend fun getUser(): Flow<UserModel> {
        return user.getUser().map {
            UserModel(
                it.name ?: "",
                it.profile ?: "",
                it.email ?: "",
                it.jabatan ?: "",
                it.nrp ?: ""
            )
        }
    }
}