package com.muhammhassan.epatrol.presentation.home.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.component.PatrolItem
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.utils.ButtonType
import com.muhammhassan.epatrol.utils.DialogActions
import com.muhammhassan.epatrol.utils.DialogData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskListView(
    uiState: UiState<List<PatrolModel>>,
    onRefreshTriggered: () -> Unit,
    setDialogData: (value: DialogData) -> Unit,
    setIsDialogShow: (value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val listData = remember { mutableStateListOf<PatrolModel>() }
    val isLoading = remember { mutableStateOf(false) }

    val scrollState = rememberLazyListState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isLoading.value, onRefresh = onRefreshTriggered)

    LaunchedEffect(key1 = uiState, block = {
        when (uiState) {
            is UiState.Error -> {
                isLoading.value = false
                setDialogData.invoke(
                    DialogData(title = "Pemberitahuan",
                        message = uiState.message,
                        buttonType = ButtonType.NEUTRAL,
                        action = object : DialogActions {
                            override fun onConfirmAction() {
                                TODO("Not yet implemented")
                            }

                            override fun onCancelAction() {
                                TODO("Not yet implemented")
                            }

                            override fun onNeutralAction() {
                                setIsDialogShow.invoke(false)
                            }

                        })
                )
                setIsDialogShow.invoke(true)
            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                listData.clear()
                listData.addAll(
                    uiState.data
                )

            }
        }
    })
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (title, content) = createRefs()
        Box(modifier = Modifier
            .constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            .background(Color.White).padding(12.dp)){
            Text(text = "Tugas Patroli", modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }
        Box(modifier = Modifier
            .constrainAs(content) {
                top.linkTo(title.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
            .pullRefresh(pullRefreshState)) {
            LazyColumn(content = {
                items(listData, key = { it.id }) {
                    PatrolItem(model = it, onItemClick = { /*TODO*/ }, modifier = Modifier)
                }
            }, state = scrollState, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize().padding(16.dp, 16.dp))
            PullRefreshIndicator(
                refreshing = isLoading.value, state = pullRefreshState, modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}