package com.muhammhassan.epatrol.presentation.patrol

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.component.EventItem
import com.muhammhassan.epatrol.component.ExpandableCard
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import com.muhammhassan.epatrol.utils.Utils
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.Calendar24
import compose.icons.octicons.Checklist24
import compose.icons.octicons.Clock24
import compose.icons.octicons.Plus24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatrolDetailView(
    onNavUp: () -> Unit,
    userEmail: String,
    navigateToAddEvent: (id: Long) -> Unit,
    navigateToDetailEvent: (id: PatrolEventModel) -> Unit,
    state: UiState<PatrolDetailModel>,
    modifier: Modifier = Modifier
) {
    val (data, setData) = remember {
        mutableStateOf(
            PatrolDetailModel(
                0L,
                "Patroli Keamanan Malam Hari",
                "sedang-dikerjakan",
                "Sprin/130/I/PAM.5.1.1/2023",
                "2 Juli 2023",
                "14.20",
                "Patroli keamanan Malam Hari bertujuan untuk mencegah tindakan kriminal seperti perampokan, pencurian, vandalisme",
                listOf(),
                lead = "budi@gmail.com"
            )
        )
    }

    val (isLoading, setLoading) = remember {
        mutableStateOf(false)
    }
    val (isDialogShow, setDialogShow) = remember {
        mutableStateOf(false)
    }
    val (dialogMessage, setDialogMessage) = remember {
        mutableStateOf("")
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

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = "Detail Patroli") }, modifier = Modifier, navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ArrowLeft24),
                    contentDescription = "Kembali"
                )
            }
        }, actions = {
            PlainTooltipBox(tooltip = { Text(text = "Tambah") }) {
                IconButton(
                    onClick = { navigateToAddEvent(data.id) },
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.Plus24),
                        contentDescription = "Tambah"
                    )
                }
            }
            if (userEmail == data.lead) {
                PlainTooltipBox(tooltip = { Text(text = "Selesaikan") }) {
                    IconButton(
                        onClick = { navigateToAddEvent(data.id) },
                        modifier = Modifier.tooltipAnchor()
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Octicons.Checklist24),
                            contentDescription = "Tambah"
                        )
                    }
                }
            }
        })
    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                Row(modifier = Modifier.padding(16.dp)) {
                    ExpandableCard(title = "Daftar Kejadian", content = {
                        data.events.forEach {
                            EventItem(data = it, onItemClick = navigateToDetailEvent)
                        }
                    }, defaultExpandedValue = true)
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
            Text(text = Utils.getStatus(status),
                modifier = Modifier
                    .constrainAs(statusConst) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 16.dp)
                    }
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                fontSize = 12.sp)
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

@Preview(showSystemUi = true)
@Composable
fun PatrolDetailPreview() {
    EPatrolTheme {
        PatrolDetailView(state = UiState.Loading,
            onNavUp = {},
            userEmail = "budi@gmail.com",
            navigateToAddEvent = {},
            navigateToDetailEvent = {})
    }
}