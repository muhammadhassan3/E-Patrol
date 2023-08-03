package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.LoginUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class LoginInteractor(private val user: UserRepository): LoginUseCase {
    override suspend fun login(email: String, password: String): Flow<UiState<UserModel>> {
        return user.login(email, password).map {
            it.mapToUiState { message ->
                UserModel(message.user.name!!, "", message.user.email!!)
            }
        }
    }

    override suspend fun getSavedEmail(): Flow<String?> {
        return user.getEmail()
    }
}