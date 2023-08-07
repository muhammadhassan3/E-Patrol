package com.muhammhassan.epatrol.di

import com.muhammhassan.epatrol.presentation.auth.login.LoginViewModel
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardViewModel
import com.muhammhassan.epatrol.presentation.home.task.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Module {
    val viewModelModule = module {
        viewModel { LoginViewModel(get()) }
        viewModel { DashboardViewModel(get()) }
        viewModel { TaskListViewModel(get()) }
    }
}