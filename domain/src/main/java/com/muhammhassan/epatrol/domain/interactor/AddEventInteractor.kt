package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.AddEventUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class AddEventInteractor(private val task: TaskRepository, private val user: UserRepository) : AddEventUseCase {
    override suspend fun addEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        lat: Double,
        long: Double,
        authorName: String,
        date: String
    ): Flow<UiState<Nothing>> {
        return task.addPatrolEvent(patrolId, event, summary, action, image, lat, long, authorName, date).map {
            it.mapToUiState { body ->
                body
            }
        }
    }

    override fun getUser(): Flow<UserModel> {
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