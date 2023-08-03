package com.muhammhassan.epatrol.presentation.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muhammhassan.epatrol.component.PatrolItem
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.utils.ButtonType
import com.muhammhassan.epatrol.utils.DialogActions
import com.muhammhassan.epatrol.utils.DialogData
import compose.icons.Octicons
import compose.icons.octicons.Bell24
import compose.icons.octicons.Person24

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun DashboardView(
    uiState: UiState<List<PatrolModel>>,
    user: UserModel,
    onProfileClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onItemClicked: (patrolId: Long) -> Unit,
    onRefreshTriggered: () -> Unit,
    setDialogData: (value: DialogData) -> Unit,
    setIsDialogShow: (value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val username = remember { mutableStateOf("Budi Santoso") }
    val listData = remember { mutableStateListOf<PatrolModel>() }
    val isLoading = remember { mutableStateOf(false) }

    val scrollState = rememberLazyListState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isLoading.value, onRefresh = onRefreshTriggered)

    LaunchedEffect(key1 = user, block = {
        val tmpName = StringBuilder(user.name).append("!")
        username.value = tmpName.toString()
    })

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


    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (greetings, name, notification, profile, title, details, content) = createRefs()
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
                GlideImage(
                    model = user.profileImage,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop
                ) {
                    it.error(Octicons.Person24).diskCacheStrategy(DiskCacheStrategy.DATA)
                }
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

        Box(modifier = Modifier
            .constrainAs(content) {
                top.linkTo(title.bottom, 8.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .pullRefresh(pullRefreshState)) {
            LazyColumn(content = {
                items(listData, key = { it.id }) {
                    PatrolItem(model = it, onItemClick = { /*TODO*/ }, modifier = Modifier)
                }
            }, state = scrollState, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize())
            PullRefreshIndicator(
                refreshing = isLoading.value, state = pullRefreshState, modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
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
                    "Jl juanda"
                )
            )
        ),
            user = UserModel("Doni Salamander", "", ""),
            onProfileClicked = {},
            onItemClicked = {},
            onNotificationClicked = {},
            onRefreshTriggered = {}, setDialogData = {}, setIsDialogShow = {}
        )
    }
}