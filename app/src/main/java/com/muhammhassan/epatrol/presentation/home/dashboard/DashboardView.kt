package com.muhammhassan.epatrol.presentation.home.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.component.PatrolTaskList
import com.muhammhassan.epatrol.component.VerifyBottomSheetView
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.utils.PatrolStatus
import com.muhammhassan.epatrol.utils.doReloginEvent
import compose.icons.Octicons
import compose.icons.octicons.Bell24
import compose.icons.octicons.Person24
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun DashboardView(
    onProfileClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    navigateToDetailPage: (patrolId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {

    val uiState by viewModel.taskList.collectAsState()
    val user by viewModel.user.collectAsState()


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Expanded })
    val scope = rememberCoroutineScope()
    val isSheetShow = remember {
        mutableStateOf(false)
    }

    val username = remember { mutableStateOf("Memuat Data") }
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
        rememberPullRefreshState(refreshing = swipeLoading.value, onRefresh = {
            viewModel.getTask()
        })
    val (selectedOrderId, setSelectedOrderId) = remember {
        mutableLongStateOf(0L)
    }
    val selectedPatrolId = remember {
        mutableLongStateOf(0L)
    }
    val (selectedPlate, setSelectedPlate) = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        sheetState.hide()
    })

    LaunchedEffect(key1 = user, block = {
        val tmpName = StringBuilder(user.name)
        username.value = tmpName.toString()
    })

    LaunchedEffect(key1 = uiState, block = {
        when (uiState) {
            is UiState.Error -> {
                swipeLoading.value = false
                dialogMessage.value = (uiState as UiState.Error).message
                isDialogShow.value = true
            }

            UiState.Loading -> {
                swipeLoading.value = true
            }

            is UiState.Success -> {
                swipeLoading.value = false
                listData.clear()
                (uiState as UiState.Success<List<PatrolModel>>).data?.let {
                    listData.addAll(
                        it
                    )
                }
            }

            is UiState.NeedLogin -> context.doReloginEvent()
        }
    })

    if (isSheetShow.value) {
        ModalBottomSheet(
            onDismissRequest = { isSheetShow.value = false },
            sheetState = sheetState,
            dragHandle = {},
            containerColor = Color.White,
        ) {
            VerifyBottomSheetView(plate = selectedPlate, onSwipe = {
                scope.launch {
                    sheetState.hide()
                    isSheetShow.value = false
                    viewModel.verifyPatrol(selectedPatrolId.longValue).collect{
                        when (it) {
                            is UiState.Error -> {
                                isLoading.value = false
                                dialogMessage.value = it.message
                                isDialogShow.value = true
                            }

                            UiState.Loading -> {
                                isLoading.value = true
                            }

                            is UiState.Success -> {
                                isLoading.value = false
                                navigateToDetailPage.invoke(selectedOrderId)
                                Toast.makeText(context, "Tugas terverifikasi", Toast.LENGTH_SHORT).show()
                            }

                            is UiState.NeedLogin -> context.doReloginEvent()
                        }
                    }
                }
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Halo,", modifier = Modifier.weight(1f), style = TextStyle(fontSize = 22.sp)
            )
            IconButton(onClick = onNotificationClicked, modifier = Modifier.size(35.dp)) {
                Icon(imageVector = Octicons.Bell24, contentDescription = "Show Notification")
            }
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.White),
            ) {
                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profileImage).diskCacheKey(user.profileImage)
                    .diskCachePolicy(CachePolicy.ENABLED).build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        IconButton(onClick = onProfileClicked, modifier = Modifier) {
                            Icon(
                                painter = rememberVectorPainter(image = Octicons.Person24),
                                contentDescription = "Icon profile"
                            )
                        }
                    })
            }
        }

        Text(
            text = username.value.plus(" \uD83D\uDC4B"),
            modifier = Modifier.width(275.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                platformStyle = PlatformTextStyle(emojiSupportMatch = EmojiSupportMatch.None)
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            Text(
                text = "Tugas Patroli", modifier = Modifier.weight(1f), style = TextStyle(
                    fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color(0xFF2E3A59)
                )
            )
            Text(
                text = "Lihat Semua", modifier = Modifier, style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
            )
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {

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
}

@Preview(showSystemUi = true)
@Composable
fun DashboardPreview() {
    EPatrolTheme {
        DashboardView(
            onProfileClicked = {},
            navigateToDetailPage = {},
            onNotificationClicked = {},
        )
    }
}