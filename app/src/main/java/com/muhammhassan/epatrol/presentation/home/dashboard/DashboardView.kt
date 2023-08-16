package com.muhammhassan.epatrol.presentation.home.dashboard

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.component.PatrolItem
import com.muhammhassan.epatrol.component.VerifyBottomSheetView
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import compose.icons.Octicons
import compose.icons.octicons.Bell24
import compose.icons.octicons.Person24
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun DashboardView(
    uiState: UiState<List<PatrolModel>>,
    verifyState: UiState<Nothing>?,
    user: UserModel,
    onProfileClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    navigateToDetailPage: (patrolId: Long) -> Unit,
    verifyUser: (id: Long) -> Unit,
    onRefreshTriggered: () -> Unit,
    modifier: Modifier = Modifier
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Expanded })
    val scope = rememberCoroutineScope()
    val isSheetShow = remember {
        mutableStateOf(false)
    }

    val username = remember { mutableStateOf("Budi Santoso") }
    val listData = remember { mutableStateListOf<PatrolModel>() }
    val swipeLoading = remember { mutableStateOf(false) }
    val isLoading = remember {
        mutableStateOf(false)
    }

    val isDialogShow = remember { mutableStateOf(false) }
    val dialogMessage = remember {
        mutableStateOf("")
    }

    val scrollState = rememberLazyListState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = swipeLoading.value, onRefresh = onRefreshTriggered)
    val (selectedId, setSelectedId) = remember {
        mutableStateOf(0L)
    }
    val (selectedPlate, setSelectedPlate) = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        sheetState.hide()
    })

    LaunchedEffect(key1 = user, block = {
        val tmpName = StringBuilder(user.name).append("!")
        username.value = tmpName.toString()
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
                verifyUser.invoke(selectedId)
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

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (greetings, name, notification, profile, title, details, content, indicator) = createRefs()
        Text(text = "Halo,", modifier = Modifier.constrainAs(greetings) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(parent.top, 16.dp)
        }, style = TextStyle(fontSize = 22.sp))
        Text(
            text = username.value,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(parent.start, 16.dp)
                top.linkTo(greetings.bottom)
                end.linkTo(notification.start, 8.dp)
                width = Dimension.fillToConstraints
            },
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onNotificationClicked, modifier = Modifier.constrainAs(notification) {
            top.linkTo(parent.top, 16.dp)
            end.linkTo(profile.start, 8.dp)
            bottom.linkTo(profile.bottom)
            width = Dimension.value(35.dp)
            height = Dimension.value(35.dp)
        }) {
            Icon(imageVector = Octicons.Bell24, contentDescription = "Show Notification")
        }

        if (user.profileImage != "") {
            Button(
                onClick = onProfileClicked,
                modifier = Modifier.constrainAs(profile) {
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.value(35.dp)
                    height = Dimension.value(35.dp)
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(user.profileImage)
                        .crossfade(true).diskCacheKey(user.profileImage)
                        .diskCachePolicy(CachePolicy.ENABLED).build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            IconButton(onClick = onProfileClicked, modifier = Modifier.constrainAs(profile) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.value(35.dp)
                height = Dimension.value(35.dp)
            }) {
                Icon(imageVector = Octicons.Person24, contentDescription = "Profile")
            }
        }
        Text(
            text = "Tugas Patroli", modifier = Modifier.constrainAs(title) {
                top.linkTo(name.bottom, 8.dp)
                start.linkTo(parent.start, 16.dp)
            }, style = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color(0xFF2E3A59)
            )
        )
        Text(
            text = "Lihat Semua", modifier = Modifier.constrainAs(details) {
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(title.bottom)
            }, style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            )
        )

        LazyColumn(content = {
            items(listData, key = { it.id }) {
                PatrolItem(model = it, onItemClick = { id, plate ->
                    if (it.verified) {
                        navigateToDetailPage.invoke(id)
                        return@PatrolItem
                    }
                    if (it.lead == user.email) {
                        scope.launch { sheetState.show() }
                        isSheetShow.value = true
                        setSelectedId(id)
                        setSelectedPlate(plate)
                    } else {
                        dialogMessage.value =
                            "Perlengkapan patroli ini belum dicek, silahkan tunggu ketua regu untuk melakukan verifikasi kemudian lakukan refresh pada halaman ini."
                        isDialogShow.value = true
                    }
                }, modifier = Modifier)
            }
        },
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(title.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .pullRefresh(pullRefreshState),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp))
        PullRefreshIndicator(refreshing = swipeLoading.value,
            state = pullRefreshState,
            modifier = Modifier.constrainAs(indicator) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(content.top)
            })
    }
}

@Preview(showSystemUi = true)
@Composable
fun DashboardPreview() {
    EPatrolTheme {
        DashboardView(uiState = UiState.Success(
            listOf(
                PatrolModel(
                    1,
                    "belum-dikerjakan",
                    "jalan jalan",
                    "17 agustus 2020",
                    "07:00",
                    "ade@email.co",
                    false,
                    "Jl juanda",
                    "R 0000 PH"
                )
            )
        ),
            user = UserModel("Doni Salamander", "", "","", ""),
            onProfileClicked = {},
            navigateToDetailPage = {},
            onNotificationClicked = {},
            onRefreshTriggered = {},
            verifyUser = {},
            verifyState = null
        )
    }
}