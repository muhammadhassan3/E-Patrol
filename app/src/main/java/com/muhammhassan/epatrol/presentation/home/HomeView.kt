package com.muhammhassan.epatrol.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.muhammhassan.epatrol.component.DialogContent
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardView
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardViewModel
import com.muhammhassan.epatrol.presentation.home.task.TaskListView
import com.muhammhassan.epatrol.presentation.home.task.TaskListViewModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Tertiary
import com.muhammhassan.epatrol.utils.DialogData
import com.muhammhassan.epatrol.utils.NavigationItem
import com.muhammhassan.epatrol.utils.Screen
import compose.icons.Octicons
import compose.icons.octicons.Home24
import compose.icons.octicons.Note24
import compose.icons.octicons.Person24
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {

    val isDialogShow = remember { mutableStateOf(false) }
    val dialogData = remember { mutableStateOf(DialogData.init()) }

    val navigation = listOf(
        NavigationItem("Beranda", Octicons.Home24, screen = Screen.Dashboard),
        NavigationItem("Tugas Patroli", Octicons.Note24, screen = Screen.Task),
        NavigationItem("Profil", Octicons.Person24, screen = Screen.Profile)
    )

    if (isDialogShow.value) {
        Dialog(onDismissRequest = { isDialogShow.value = false }) {
            DialogContent(
                message = dialogData.value.message,
                title = dialogData.value.title,
                buttonType = dialogData.value.buttonType,
                action = dialogData.value.action
            )
        }
    }
    Scaffold(modifier = modifier, bottomBar = {
        val currentStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentStack?.destination?.route
        BottomAppBar(modifier = Modifier, containerColor = Color.White) {
            navigation.map { item ->
                NavigationBarItem(selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = "${item.title} page")
                    },
                    label = {
                        Text(text = item.title)
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Tertiary)
                )
            }
        }
    }) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(
                route = Screen.Dashboard.route
            ) {
                val viewModel = koinViewModel<DashboardViewModel>()
                val state by viewModel.taskList.collectAsState()
                val user by viewModel.user.collectAsState()
                DashboardView(uiState = state,
                    user = user,
                    onProfileClicked = { /*TODO*/ },
                    onNotificationClicked = { /*TODO*/ },
                    onItemClicked = {},
                    onRefreshTriggered = viewModel::getTask,
                    setIsDialogShow = {
                        isDialogShow.value = it
                    },
                    setDialogData = {
                        dialogData.value = it
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            composable(
                route = Screen.Task.route
            ){
                val viewModel = koinViewModel<TaskListViewModel>()
                val state by viewModel.taskList.collectAsState()
                
                TaskListView(
                    uiState = state,
                    onRefreshTriggered = viewModel::getTask,
                    setDialogData = {
                                    dialogData.value = it
                    },
                    setIsDialogShow = {
                        isDialogShow.value = it
                    }
                )
            }
            composable(route = Screen.Profile.route){
                val context = LocalContext.current
                Toast.makeText(context, "Halaman ini sedang dalam pembaruan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    EPatrolTheme {
        HomeView()
    }
}