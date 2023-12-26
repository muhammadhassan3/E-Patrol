package com.muhammhassan.epatrol.presentation.home.task

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.component.PatrolTaskList
import com.muhammhassan.epatrol.component.VerifyBottomSheetView
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.utils.PatrolStatus
import com.muhammhassan.epatrol.utils.doReloginEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskListView(
    uiState: UiState<List<PatrolModel>>,
    verifyState: UiState<Nothing>?,
    user: UserModel,
    navigateToDetailPage: (id: Long) -> Unit,
    verifyPatrolTask: (id: Long) -> Unit,
    onRefreshTriggered: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Expanded })
    val scope = rememberCoroutineScope()
    val isSheetShow = remember {
        mutableStateOf(false)
    }

    val listData = remember { mutableStateListOf<PatrolModel>() }
    val swipeLoading = remember { mutableStateOf(false) }
    val isLoading = remember {
        mutableStateOf(false)
    }

    val isDialogShow = remember { mutableStateOf(false) }
    val dialogMessage = remember {
        mutableStateOf("")
    }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = swipeLoading.value, onRefresh = onRefreshTriggered)
    val (selectedId, setSelectedOrderId) = remember {
        mutableLongStateOf(0L)
    }
    val selectedPatrolId = remember{
        mutableLongStateOf(0L)
    }
    val (selectedPlate, setSelectedPlate) = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        sheetState.hide()
    })

    LaunchedEffect(key1 = uiState, block = {
        when (uiState) {
            is UiState.Error -> {
                swipeLoading.value = false
                dialogMessage.value = uiState.message
                isDialogShow.value = true
            }

            UiState.Loading -> {
                swipeLoading.value = true
            }

            is UiState.Success -> {
                swipeLoading.value = false
                listData.clear()
                uiState.data?.let {
                    listData.addAll(
                        it
                    )
                }
            }

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    LaunchedEffect(key1 = verifyState, block = {
        when (verifyState) {
            is UiState.Error -> {
                isLoading.value = false
                dialogMessage.value = verifyState.message
                isDialogShow.value = true
            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                navigateToDetailPage.invoke(selectedId)
                Toast.makeText(context, "Tugas terverifikasi", Toast.LENGTH_SHORT).show()
            }

            null -> {}

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    if (isSheetShow.value) {
        ModalBottomSheet(
            onDismissRequest = { isSheetShow.value = false },
            sheetState = sheetState,
        ) {
            VerifyBottomSheetView(plate = selectedPlate, onSwipe = {
                scope.launch { sheetState.hide() }
                isSheetShow.value = false
                verifyPatrolTask.invoke(selectedId)
            })
        }
    }

    if (isDialogShow.value) {
        AlertDialog(onDismissRequest = { isDialogShow.value = false }, confirmButton = {
            TextButton(onClick = { isDialogShow.value = false }) {
                Text(text = "Oke")
            }
        }, title = { Text(text = "Pemberitahuan") }, text = { Text(text = dialogMessage.value) })
    }

    if (isLoading.value) {
        LoadingDialog(onDismiss = { isLoading.value = false })
    }

    Box(modifier = modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp), contentAlignment = Alignment.TopCenter) {
        PatrolTaskList(data = listData, onItemClick = {
            if (it.verified || it.status != PatrolStatus.BELUM_DIJALANKAN) {
                navigateToDetailPage.invoke(it.id)
                return@PatrolTaskList
            }
            if (it.lead == user.email) {
                scope.launch { sheetState.show() }
                isSheetShow.value = true
                setSelectedOrderId(it.id)
                selectedPatrolId.longValue = it.patrolId
                setSelectedPlate(it.plate)
            } else {
                dialogMessage.value =
                    "Perlengkapan patroli ini belum dicek, silahkan tunggu ketua regu untuk melakukan verifikasi kemudian lakukan refresh pada halaman ini."
                isDialogShow.value = true
            }
        }, pullRefreshState = pullRefreshState, modifier = Modifier)

        PullRefreshIndicator(
            refreshing = swipeLoading.value, state = pullRefreshState, modifier = Modifier
        )
    }
}