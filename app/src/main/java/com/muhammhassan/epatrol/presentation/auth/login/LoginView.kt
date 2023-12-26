package com.muhammhassan.epatrol.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammhassan.epatrol.R
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Primary
import com.muhammhassan.epatrol.utils.doReloginEvent
import compose.icons.Octicons
import compose.icons.octicons.Eye24
import compose.icons.octicons.EyeClosed24
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginView(
    onResponseSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val isPasswordShow = remember { mutableStateOf(false) }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val dialogData = remember {
        mutableStateOf("")
    }
    val isDialogShow = remember {
        mutableStateOf(false)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state, block = {
        when (state) {
            is UiState.Error -> {
                isLoading.value = false
                dialogData.value = (state as UiState.Error).message
                isDialogShow.value = true
            }

            UiState.Loading -> {
                isLoading.value = true
            }

            is UiState.Success -> {
                isLoading.value = false
                onResponseSuccess.invoke()
            }

            is UiState.NeedLogin -> context.doReloginEvent()

            null -> {}
        }
    })


    if (isDialogShow.value) {
        AlertDialog(onDismissRequest = { isDialogShow.value = false }, confirmButton = {
            TextButton(onClick = { isDialogShow.value = false }) {
                Text(text = "Oke")
            }
        }, title = {
            Text(text = "Pemberitahuan")
        }, text = {
            Text(text = dialogData.value)
        })
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (icon, title, subtitle, edtEmail, edtPassword, btnMasuk, loading) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "Application Icon",
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, 32.dp)
                width = Dimension.value(180.dp)
                height = Dimension.preferredWrapContent
            },
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "Selamat Datang!",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(icon.bottom, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontSize = 24.sp
        )
        Text(text = "Masuk untuk melanjutkan", modifier = Modifier.constrainAs(subtitle) {
            top.linkTo(title.bottom, (-10).dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.constrainAs(loading) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(icon.bottom)
                bottom.linkTo(parent.bottom)
            })
        } else {
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::setEmail,
                modifier = Modifier.constrainAs(edtEmail) {
                    top.linkTo(subtitle.bottom, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
                placeholder = { Text(text = "Masukkan Email") },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                ),
                singleLine = true
            )
            OutlinedTextField(value = password,
                onValueChange = viewModel::setPassword,
                modifier = Modifier.constrainAs(edtPassword) {
                    top.linkTo(edtEmail.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
                placeholder = { Text(text = "Masukkan Password") },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                ),
                visualTransformation = if (isPasswordShow.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordShow.value = !isPasswordShow.value }) {
                        Icon(
                            imageVector = if (isPasswordShow.value) Octicons.EyeClosed24 else Octicons.Eye24,
                            contentDescription = if (isPasswordShow.value) "Hide Password" else "Show Password"
                        )
                    }
                }, keyboardActions = KeyboardActions(onSend = {
                    viewModel.login()
                }),
                singleLine = true
            )
//            Text(text = "Lupa Password?", modifier = Modifier.constrainAs(forgetPassword) {
//                end.linkTo(parent.end, 16.dp)
//                top.linkTo(edtPassword.bottom, 8.dp)
//            }, fontWeight = FontWeight.Medium, color = Primary)
            Button(
                onClick = viewModel::login,
                modifier = Modifier.constrainAs(btnMasuk) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(45.dp)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    text = "Masuk",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

//            Row(modifier = Modifier.constrainAs(layoutRegister) {
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//                bottom.linkTo(parent.bottom, 16.dp)
//            }) {
//                Text(
//                    text = "Belum punya akun? ",
//                    modifier = Modifier,
//                    color = Color.DarkGray,
//                    fontSize = 14.sp
//                )
//                Text(text = "Daftar", modifier = Modifier, color = Secondary, fontSize = 14.sp)
//            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    EPatrolTheme {
        LoginView(
            onResponseSuccess = {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview2() {
    EPatrolTheme {
        LoginView(
            onResponseSuccess = {})
    }
}