package com.muhammhassan.epatrol.di

import com.muhammhassan.epatrol.presentation.auth.login.LoginViewModel
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardViewModel
import com.muhammhassan.epatrol.presentation.home.profile.ProfileViewModel
import com.muhammhassan.epatrol.presentation.home.task.TaskListViewModel
import com.muhammhassan.epatrol.presentation.main.MainViewModel
import com.muhammhassan.epatrol.presentation.patrol.PatrolDetailViewModel
import com.muhammhassan.epatrol.presentation.patrol.event.EventDetailViewModel
import com.muhammhassan.epatrol.presentation.patrol.event.add.AddEventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object Module {
    val viewModelModule = module {
        viewModelOf(::LoginViewModel)
        viewModel { DashboardViewModel(get()) }
        viewModel { TaskListViewModel(get()) }
        viewModel { PatrolDetailViewModel(get()) }
        viewModel { EventDetailViewModel(get()) }
        viewModel { AddEventViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { MainViewModel(get()) }
    }
}