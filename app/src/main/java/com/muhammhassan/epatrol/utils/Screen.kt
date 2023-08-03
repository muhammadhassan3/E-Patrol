package com.muhammhassan.epatrol.utils

sealed class Screen(val route: String){
    object Dashboard: Screen("dashboard")
    object Task: Screen("task")
    object Profile: Screen("profile")
}
