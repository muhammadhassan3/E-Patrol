package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.NotificationRepository
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.LoginUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginInteractor(private val user: UserRepository, private val notif: NotificationRepository) : LoginUseCase {
    override suspend fun login(
        email: String,
        password: String,
        token: String?
    ): Flow<UiState<UserModel>> {
        return user.login(email, password, token).map {
            it.mapToUiState { message ->
                UserModel(
                    message.user.name ?: "",
                    message.user.profile ?: "",
                    message.user.email ?: "",
                    message.user.jabatan ?: "",
                    message.user.nrp ?: ""
                )
            }
        }
    }

    override suspend fun getSavedEmail(): Flow<String?> {
        return user.getEmail()
    }

    override fun getToken(): Flow<String?> {
        return notif.getToken()
    }
}