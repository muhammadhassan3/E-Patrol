package com.muhammhassan.epatrol.di

import com.muhammhassan.epatrol.presentation.auth.login.LoginViewModel
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardViewModel
import com.muhammhassan.epatrol.presentation.home.task.TaskListViewModel
import org.koin.dsl.module

object Module {
    val viewModelModule = module {
        single { LoginViewModel(get()) }
        single { DashboardViewModel(get()) }
        single { TaskListViewModel(get()) }
    }
}