package com.muhammhassan.epatrol.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.muhammhassan.epatrol.presentation.home.dashboard.DashboardView
import com.muhammhassan.epatrol.presentation.home.profile.ProfileView
import com.muhammhassan.epatrol.presentation.home.profile.ProfileViewModel
import com.muhammhassan.epatrol.presentation.home.task.TaskListView
import com.muhammhassan.epatrol.presentation.home.task.TaskListViewModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Tertiary10
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
    navigateToDetailPage: (id: Long) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigation = listOf(
        NavigationItem("Beranda", Octicons.Home24, screen = Screen.Dashboard),
        NavigationItem("Tugas Patroli", Octicons.Note24, screen = Screen.Task),
        NavigationItem("Profil", Octicons.Person24, screen = Screen.Profile)
    )

    val backStack = navController.currentBackStackEntryAsState()
    val route = backStack.value?.destination?.route
    rememberCoroutineScope()
    Scaffold(modifier = modifier, bottomBar = {
        val currentStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentStack?.destination?.route
        BottomAppBar(modifier = Modifier, containerColor = Color.White) {
            navigation.map { item ->
                NavigationBarItem(selected = currentRoute == item.screen.route, onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }
                        restoreState = false
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(imageVector = item.icon, contentDescription = "${item.title} page")
                }, label = {
                    Text(text = item.title)
                }, colors = NavigationBarItemDefaults.colors(indicatorColor = Tertiary10))
            }
        }
    }, topBar = {
        when (route) {
            Screen.Dashboard.route -> {

            }

            Screen.Task.route -> {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = "Tugas Patroli",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                })
            }

            Screen.Profile.route -> {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = "Profil",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                })
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

                DashboardView(
                    onProfileClicked = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                            restoreState = false
                            launchSingleTop = true
                        }
                    },
                    onNotificationClicked = { /*TODO*/ },
                    navigateToDetailPage = navigateToDetailPage,
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(
                route = Screen.Task.route
            ) {
                val viewModel = koinViewModel<TaskListViewModel>()
                val state by viewModel.taskList.collectAsState()
                val user by viewModel.user.collectAsState()
                val verifyState by viewModel.verifyState.collectAsState()

                TaskListView(
                    uiState = state,
                    onRefreshTriggered = viewModel::getTask,
                    verifyState = verifyState,
                    user = user,
                    navigateToDetailPage = navigateToDetailPage,
                    verifyPatrolTask = viewModel::verifyPatrol,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screen.Profile.route) {
                val viewModel = koinViewModel<ProfileViewModel>()
                val user by viewModel.user.collectAsStateWithLifecycle()
                user?.let { data ->
                    ProfileView(user = data, onLogout = {
                        viewModel.logout()
                        onReset.invoke()
                    })
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    EPatrolTheme {
        HomeView(navigateToDetailPage = {}, onReset = {})
    }
}