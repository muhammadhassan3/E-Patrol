package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.muhammhassan.epatrol.component.LoadingDialog
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.Image24

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddEventView(
    onNavUp: () -> Unit,
    onCaptureImage: () -> Unit,
    image: Uri?,
    event: String,
    eventChanged: (value: String) -> Unit,
    summary: String,
    summaryChanged: (value: String) -> Unit,
    action: String,
    actionChanged: (value: String) -> Unit,
    onSubmit: () -> Unit,
    addState: UiState<Nothing>?,
    onResponseSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = remember {
        mutableStateOf(false)
    }

    val isDialogShow = remember {
        mutableStateOf(false)
    }

    val dialogMessage = remember {
        mutableStateOf("")
    }

    val summaryFocus = remember {
        FocusRequester()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    if (isLoading.value) {
        LoadingDialog(onDismiss = { isLoading.value = false })
    }

    if (isDialogShow.value) {
        AlertDialog(onDismissRequest = { isDialogShow.value = false }, confirmButton = {
            TextButton(onClick = { isDialogShow.value = false }) {
                Text(text = "Oke")
            }
        }, title = {
            Text(text = "Pemberitahuan")
        }, text = {
            Text(text = dialogMessage.value)
        })
    }

    LaunchedEffect(key1 = addState, block = {
        when (addState) {
            is UiState.Error -> {
                isLoading.value = false
                dialogMessage.value = addState.message
                isDialogShow.value = true
            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                onResponseSuccess.invoke()
            }

            null -> {}
        }
    })


    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = "Tambah Kejadian",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
        }, navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ArrowLeft24),
                    contentDescription = "Kembali"
                )
            }
        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White))
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Kejadian",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = event,
                        onValueChange = eventChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Tuliskan kejadian yang kamu temukan",
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        },
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray, focusedBorderColor = Secondary
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            summaryFocus.requestFocus()
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Deskripsi Singkat",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = summary,
                        onValueChange = summaryChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(summaryFocus)
                            .onFocusChanged { if (it.isFocused) keyboardController?.show() },
                        placeholder = {
                            Text(
                                text = "Berikan deskripsi singkat kejadian",
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        },
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray, focusedBorderColor = Secondary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tindakan yang dilakukan",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = action,
                        onValueChange = actionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Tuliskan tindakan yang dilakukan",
                                modifier = Modifier,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        },
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray, focusedBorderColor = Secondary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gambar",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable { onCaptureImage.invoke() }) {
                        if (image == null || image.toString() == "") {

                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.Image24),
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(24.dp),
                                    tint = Secondary
                                )
                                Text(text = "Pilih Gambar", style = TextStyle(color = Secondary))
                            }
                        } else {
                            AsyncImage(
                                model = image,
                                contentDescription = "Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .fillMaxWidth(), shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 16.dp
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(text = "Tambah Kejadian", style = TextStyle(fontSize = 16.sp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddEventPreview() {
    EPatrolTheme {
        AddEventView(onNavUp = { /*TODO*/ },
            onCaptureImage = { /*TODO*/ },
            image = "".toUri(),
            action = "",
            event = "",
            eventChanged = {},
            actionChanged = {},
            summary = "",
            summaryChanged = {},
            onSubmit = {},
            addState = null,
            onResponseSuccess = {})
    }
}