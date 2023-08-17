package com.muhammhassan.epatrol.domain.di

import com.muhammhassan.epatrol.core.di.Module.dataSourceModule
import com.muhammhassan.epatrol.core.di.Module.datastoreModule
import com.muhammhassan.epatrol.core.di.Module.provideApi
import com.muhammhassan.epatrol.core.di.Module.repositoryModule
import com.muhammhassan.epatrol.domain.interactor.AddEventInteractor
import com.muhammhassan.epatrol.domain.interactor.DashboardInteractor
import com.muhammhassan.epatrol.domain.interactor.DetailPatrolEventInteractor
import com.muhammhassan.epatrol.domain.interactor.LoginInteractor
import com.muhammhassan.epatrol.domain.interactor.PatrolDetailInteractor
import com.muhammhassan.epatrol.domain.interactor.ProfileInteractor
import com.muhammhassan.epatrol.domain.interactor.TaskListInteractor
import com.muhammhassan.epatrol.domain.usecase.AddEventUseCase
import com.muhammhassan.epatrol.domain.usecase.DashboardUseCase
import com.muhammhassan.epatrol.domain.usecase.DetailPatrolEventUseCase
import com.muhammhassan.epatrol.domain.usecase.LoginUseCase
import com.muhammhassan.epatrol.domain.usecase.PatrolDetailUseCase
import com.muhammhassan.epatrol.domain.usecase.ProfileUseCase
import com.muhammhassan.epatrol.domain.usecase.TaskListUseCase
import org.koin.dsl.module

object Module {
    val useCaseModule = module {
        single<LoginUseCase> { LoginInteractor(get()) }
        single<DashboardUseCase> { DashboardInteractor(get(), get()) }
        single<TaskListUseCase> { TaskListInteractor(get(), get()) }
        single<PatrolDetailUseCase> { PatrolDetailInteractor(get(), get()) }
        single<DetailPatrolEventUseCase> { DetailPatrolEventInteractor(get(), get()) }
        single<AddEventUseCase> { AddEventInteractor(get()) }
        single<ProfileUseCase> { ProfileInteractor(get()) }
    }

    fun retrofit(version: Int) = provideApi(version)
    val datasourceModule = dataSourceModule
    val repModule = repositoryModule
    val dataStoreModule = datastoreModule
}