package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.domain.usecase.DashboardUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardInteractor(private val user: UserRepository, private val task: TaskRepository) :
    DashboardUseCase {
    override fun getUser(): Flow<UserModel> {
        return user.getUser().map {
            UserModel(it.name!!, it.profile!!, it.email!!,it.jabatan!!, it.nrp!!)
        }
    }

    override suspend fun getTaskList(): Flow<UiState<List<PatrolModel>>> {
        return task.getTaskList().map { response ->
            response.mapToUiState { list ->
                list.map {
                    PatrolModel(
                        id = it.id,
                        status = it.status,
                        title = it.judul,
                        date = it.tanggal,
                        hour = it.jam,
                        lead = it.ketua,
                        verified = it.verified,
                        address = it.alamat,
                        plate =  it.plate
                    )
                }
            }
        }
    }

    override suspend fun verifyPatrolTask(id: Long): Flow<UiState<Nothing>> {
        return task.verifyPatrolTask(id).map {
            it.mapToUiState { body -> body }
        }
    }
}