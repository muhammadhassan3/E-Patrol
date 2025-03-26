package com.muhammhassan.epatrol.presentation.patrol

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.component.ConfirmDialogView
import com.muhammhassan.epatrol.component.EventItem
import com.muhammhassan.epatrol.component.ExpandableCard
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.DarkGreen
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import com.muhammhassan.epatrol.utils.PatrolStatus
import com.muhammhassan.epatrol.utils.doReloginEvent
import com.muhammhassan.epatrol.utils.getDisplayStatus
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.Calendar24
import compose.icons.octicons.Check24
import compose.icons.octicons.Clock24
import compose.icons.octicons.Plus24
import compose.icons.octicons.Sync24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatrolDetailView(
    onNavUp: () -> Unit,
    userEmail: String,
    navigateToAddEvent: (id: Long) -> Unit,
    navigateToDetailEvent: (id: PatrolEventModel, removable: Boolean) -> Unit,
    state: UiState<PatrolDetailModel>,
    eventState: UiState<List<PatrolEventModel>>,
    confirmState: UiState<Nothing>?,
    markAsDonePatrol: (patrolId: Long) -> Unit,
    onCompleteSuccess: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (data, setData) = remember {
        mutableStateOf(
            PatrolDetailModel(
                0L, 2, "Memuat data", "-", "Sprin/---/-/---.-.-.-/----", "-", "-", "-", lead = "-"
            )
        )
    }

    val context = LocalContext.current

    val events = remember {
        mutableStateListOf<PatrolEventModel>()
    }


    val (isLoading, setLoading) = remember {
        mutableStateOf(false)
    }

    val (isEventLoading, setEventLoading) = remember {
        mutableStateOf(false)
    }

    val (isDialogShow, setDialogShow) = remember {
        mutableStateOf(false)
    }
    val (dialogMessage, setDialogMessage) = remember {
        mutableStateOf("")
    }

    val isConfirmDialogShow = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state, block = {
        when (state) {
            is UiState.Error -> {
                setLoading(false)
                setDialogMessage(state.message)
                setDialogShow(true)
            }

            UiState.Loading -> {
                setLoading(true)
            }

            is UiState.Success -> {
                setLoading(false)
                state.data?.let { setData(it) }
            }

            is UiState.NeedLogin -> context.doReloginEvent()
        }
    })

    LaunchedEffect(key1 = eventState, block = {
        when (eventState) {
            is UiState.Error -> {
                setEventLoading(false)
                setDialogMessage(eventState.message)
                setDialogShow(true)
            }

            UiState.Loading -> {
                setEventLoading(true)
            }

            is UiState.Success -> {
                setEventLoading(false)
                events.clear()
                eventState.data?.let { events.addAll(it) }
            }

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    LaunchedEffect(key1 = confirmState, block = {
        when (confirmState) {
            is UiState.Error -> {
                setLoading(false)
                setDialogMessage(confirmState.message)
                setDialogShow(true)
            }

            UiState.Loading -> {
                setLoading(true)
            }

            is UiState.Success -> {
                setLoading(false)
                onCompleteSuccess.invoke()
            }

            null -> {}

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    if (isDialogShow) {
        AlertDialog(onDismissRequest = { setDialogShow(false) }, confirmButton = {
            TextButton(onClick = { setDialogShow(false) }) {
                Text(text = "Oke")
            }
        }, title = { Text(text = "Pemberitahuan") }, text = { Text(text = dialogMessage) })
    }

    if (isLoading) {
        LoadingDialog(onDismiss = { setLoading(false) })
    }

    if (isConfirmDialogShow.value) {
        Dialog(onDismissRequest = { isConfirmDialogShow.value }) {
            ConfirmDialogView(message = "Apakah kamu ingin menyelesaikan tugas patroli ini?",
                onDismiss = { isConfirmDialogShow.value = false },
                onConfirm = {
                    isConfirmDialogShow.value = false
                    markAsDonePatrol.invoke(data.patrolId)
                })
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = "Detail Patroli") }, modifier = Modifier, navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ArrowLeft24),
                    contentDescription = "Kembali"
                )
            }
        }, actions = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { Text(text = "Muat ulang") },
                state = rememberTooltipState()
            ) {
                IconButton(
                    onClick = onRefresh, modifier = Modifier
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.Sync24),
                        contentDescription = "Muat ulang"
                    )
                }
            }
        })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    HeaderCard(
                        title = data.title,
                        status = data.status,
                        sprin = data.sprin,
                        date = data.tanggal,
                        hour = data.jam
                    )
                }
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        ExpandableCard(title = "Tujuan", content = {
                            Text(text = data.tujuan)
                        }, modifier = Modifier, containerColor = Color.White)
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        ExpandableCard(title = "Daftar Kejadian", content = {
                            if (isEventLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(45.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            } else if (events.isNotEmpty()) {
                                events.forEach {
                                    EventItem(data = it, onItemClick = { item ->
                                        navigateToDetailEvent.invoke(
                                            item,
                                            userEmail == data.lead && data.status == PatrolStatus.SEDANG_DIJALANKAN
                                        )
                                    })
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            } else {
                                Text(
                                    text = "Data tidak tersedia",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }, defaultExpandedValue = true)
                    }
                }
            }
            if (data.status == PatrolStatus.SEDANG_DIJALANKAN) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { navigateToAddEvent(data.patrolId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tambah Kejadian",
                                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Plus24),
                                        contentDescription = "Tambah Kejadian",
                                        modifier = Modifier.size(16.dp),
                                        tint = Secondary
                                    )
                                }
                            }
                        }
                        if (data.lead == userEmail) {
                            IconButton(
                                onClick = { isConfirmDialogShow.value = true },
                                modifier = Modifier.clip(CircleShape),
                                colors = IconButtonDefaults.iconButtonColors(containerColor = DarkGreen),

                                ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.Check24),
                                    contentDescription = "Selesaikan Patroli",
                                    modifier = Modifier,
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CircleGradient(modifier: Modifier) {
    val gradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val maxSize = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(Color.Transparent, Color.White),
                radius = maxSize / 0.5f,
                center = size.center
            )
        }
    }
    Canvas(modifier = modifier.clip(CircleShape), onDraw = {
        drawCircle(gradient)
    })
}

@Composable
fun HeaderCard(
    title: String,
    status: String,
    sprin: String,
    date: String,
    hour: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (statusConst, titleConst, sprinConst, footerConst, iconCalendar, calendarConst, iconTime, timeConst) = createRefs()
            val verticalGuideline = createGuidelineFromStart(0.8f)
            Text(text = getDisplayStatus(status),
                modifier = Modifier
                    .constrainAs(statusConst) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 16.dp)
                    }
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                fontSize = 12.sp,
                color = Secondary)
            Text(
                text = title, modifier = Modifier.constrainAs(titleConst) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(verticalGuideline)
                    top.linkTo(statusConst.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                }, style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 27.sp,
                    fontSize = 22.sp,

                    ), maxLines = 2, overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sprin, modifier = Modifier.constrainAs(sprinConst) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(titleConst.bottom)
                    end.linkTo(verticalGuideline)
                    width = Dimension.fillToConstraints
                }, style = TextStyle(
                    color = Color.White, lineHeight = 17.sp
                )
            )
            Canvas(modifier = Modifier.constrainAs(footerConst) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(sprinConst.bottom, 4.dp)
                width = Dimension.fillToConstraints
                height = Dimension.value(28.dp)
            }) {
                drawRect(Color.Black, alpha = 0.3f)
            }
            CircleGradient(
                modifier = Modifier
                    .size(118.dp)
                    .offset((-78).dp, 72.dp)
                    .alpha(0.5f)
            )
            CircleGradient(
                modifier = Modifier
                    .size(118.dp)
                    .offset((-32).dp, 130.dp)
                    .alpha(0.5f)
            )
            CircleGradient(
                modifier = Modifier
                    .size(118.dp)
                    .offset((300).dp, (-40).dp)
                    .alpha(0.5f)
            )
            CircleGradient(
                modifier = Modifier
                    .size(118.dp)
                    .offset((325).dp, (-40).dp)
                    .alpha(0.5f)
            )

            Icon(
                painter = rememberVectorPainter(image = Octicons.Calendar24),
                contentDescription = "Tanggal",
                modifier = Modifier.constrainAs(iconCalendar) {
                    end.linkTo(calendarConst.start, 4.dp)
                    top.linkTo(footerConst.top)
                    bottom.linkTo(footerConst.bottom)
                    width = Dimension.value(14.dp)
                    height = Dimension.value(14.dp)
                },
                tint = Color.White
            )
            Text(text = date, modifier = Modifier.constrainAs(calendarConst) {
                end.linkTo(iconTime.start, 8.dp)
                top.linkTo(footerConst.top)
                bottom.linkTo(footerConst.bottom)
            }, style = TextStyle(fontSize = 12.sp, color = Color.White))
            Icon(
                painter = rememberVectorPainter(image = Octicons.Clock24),
                contentDescription = "Jam",
                modifier = Modifier.constrainAs(iconTime) {
                    end.linkTo(timeConst.start, 4.dp)
                    top.linkTo(footerConst.top)
                    bottom.linkTo(footerConst.bottom)
                    width = Dimension.value(14.dp)
                    height = Dimension.value(14.dp)
                },
                tint = Color.White
            )
            Text(text = hour, modifier = Modifier.constrainAs(timeConst) {
                end.linkTo(footerConst.end, 16.dp)
                top.linkTo(footerConst.top)
                bottom.linkTo(footerConst.bottom)
            }, style = TextStyle(fontSize = 12.sp, color = Color.White))
        }
    }
}

@Preview(showSystemUi = true, name = "Detail patrol with lead")
@Composable
fun PatrolDetailPreviewWithLead() {
    EPatrolTheme {
        PatrolDetailView(state = UiState.Success(
            PatrolDetailModel(
                0,
                2,
                "Patroli pengamanan tindak kriminal",
                "sedang-dikerjakan",
                "Sprin/131/I/PAM.6.2.2/2020",
                "1 November 2023",
                "23:00",
                "Gunung simping",
                "budi@gmail.com"
            )
        ),
            onNavUp = {},
            userEmail = "budi@gmail.com",
            navigateToAddEvent = {},
            navigateToDetailEvent = { _, _ -> },
            confirmState = null,
            markAsDonePatrol = {},
            onCompleteSuccess = {},
            onRefresh = {},
            eventState = UiState.Success(
                listOf(
                    PatrolEventModel(
                        0,
                        "Pencegahan tindak kriminal",
                        "Patroli rutin",
                        "Pengamanan pemuda",
                        "3 Juli 2020",
                        "budi@gmail.com"
                    )
                )
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PatrolDetailPreview() {
    EPatrolTheme {
        PatrolDetailView(state = UiState.Loading,
            onNavUp = {},
            userEmail = "budi2@gmail.com",
            navigateToAddEvent = {},
            navigateToDetailEvent = { _, _ -> },
            confirmState = null,
            markAsDonePatrol = {},
            onCompleteSuccess = {},
            onRefresh = {},
            eventState = UiState.Loading
        )
    }
}