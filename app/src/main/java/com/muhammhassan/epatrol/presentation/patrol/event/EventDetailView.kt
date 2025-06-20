package com.muhammhassan.epatrol.presentation.patrol.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.muhammhassan.epatrol.component.AsyncImage
import com.muhammhassan.epatrol.component.ConfirmDialogView
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.domain.model.EventDetailModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Red
import com.muhammhassan.epatrol.ui.theme.Red20
import com.muhammhassan.epatrol.utils.doReloginEvent
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.Clock24
import compose.icons.octicons.Location24
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailView(
    onNavUp: () -> Unit,
    uiState: UiState<EventDetailModel>,
    deleteState: UiState<Nothing>?,
    onResponseSuccess: () -> Unit,
    onDeleteAction: (eventId: Long) -> Unit,
    removable: Boolean,
    showLocationOnMap: (lat: Double, long: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val data = remember {
        mutableStateOf<EventDetailModel?>(null)
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val isConfirmDialogShow = remember {
        mutableStateOf(false)
    }

    val isErrorDialogShow = remember {
        mutableStateOf(false)
    }

    val errorMessage = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = uiState, block = {
        when (uiState) {
            is UiState.Error -> {
                isLoading.value = false
                isErrorDialogShow.value = true
                errorMessage.value = uiState.message

            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                data.value = uiState.data
            }

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    LaunchedEffect(key1 = deleteState, block = {
        when (deleteState) {
            is UiState.Error -> {
                isLoading.value = false
                isErrorDialogShow.value = true
                errorMessage.value = deleteState.message

            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                onResponseSuccess.invoke()
            }

            null -> {}

            is UiState.NeedLogin -> context.doReloginEvent()

        }
    })

    if (isConfirmDialogShow.value) {
        if (data.value != null) {
            Dialog(onDismissRequest = { isConfirmDialogShow.value = false }) {
                ConfirmDialogView(message = "Apakah kamu yakin ingin menghapus kejadian ini dari laporan?",
                    onDismiss = { isConfirmDialogShow.value = false },
                    onConfirm = {
                        isConfirmDialogShow.value = false
                        onDeleteAction.invoke(data.value!!.id)
                    })
            }
        } else {
            AlertDialog(onDismissRequest = { isConfirmDialogShow.value = false }, confirmButton = {
                TextButton(onClick = { isConfirmDialogShow.value = false }) {
                    Text(text = "Oke")
                }
            }, text = {
                Text(text = "Gagal menghapus data, data tidak tersedia.\nSilahkan muat ulang halaman ini")
            }, title = {
                Text(text = "Pemberitahuan")
            })
        }
    }

    if (isErrorDialogShow.value) {
        AlertDialog(onDismissRequest = { isErrorDialogShow.value = false }, confirmButton = {
            TextButton(onClick = { isErrorDialogShow.value = false }) {
                Text(text = "Oke")
            }
        }, text = {
            Text(text = errorMessage.value)
        }, title = {
            Text(text = "Pemberitahuan")
        })
    }

    if (isLoading.value) {
        LoadingDialog(onDismiss = { isLoading.value = false })
    }
    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(modifier = modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Detail Kejadian",
                    style = TextStyle(fontWeight = FontWeight.Black, fontSize = 18.sp)
                )
            }, modifier = Modifier, navigationIcon = {
                IconButton(onClick = onNavUp) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.ArrowLeft24),
                        contentDescription = "Kembali"
                    )
                }
            }, actions = {
                data.value?.let { data ->
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { Text(text = "Lihat lokasi") },
                        state = rememberTooltipState()) {
                        IconButton(onClick = {
                            showLocationOnMap.invoke(data.lat, data.long)
                        }, modifier = Modifier) {
                            Icon(
                                painter = rememberVectorPainter(image = Octicons.Location24),
                                contentDescription = "Lihat Location"
                            )
                        }
                    }
                }
            })
        }) { padding ->
            data.value?.let { data ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(state = scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Timber.e(data.image)
                    AsyncImage("", data.image, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.Clock24),
                                    contentDescription = "Icon waktu",
                                    modifier = Modifier
                                        .size(12.dp)
                                        .align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = data.createdAt,
                                    modifier = Modifier,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = data.title,
                                modifier = Modifier,
                                style = TextStyle(fontWeight = FontWeight.Black, fontSize = 16.sp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Deskripsi Kejadian",
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data.summary,
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tindakan yang dilakukan",
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data.action,
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                    if (removable) {
                        Spacer(modifier = Modifier.weight(1f))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(
                                topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Button(
                                    onClick = { isConfirmDialogShow.value = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Red20),
                                    contentPadding = PaddingValues(12.dp)
                                ) {
                                    Text(
                                        text = "Hapus Kejadian",
                                        style = TextStyle(fontSize = 16.sp, color = Red)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EventDetailPreview() {
    EPatrolTheme {
        EventDetailView(uiState = UiState.Loading,
            onNavUp = {},
            onDeleteAction = {},
            deleteState = UiState.Loading,
            onResponseSuccess = {},
            removable = true,
            showLocationOnMap = { _, _ -> })
    }
}