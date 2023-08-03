package com.muhammhassan.epatrol.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.R
import com.muhammhassan.epatrol.component.DialogContent
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Primary
import com.muhammhassan.epatrol.ui.theme.Secondary
import com.muhammhassan.epatrol.utils.ButtonType
import com.muhammhassan.epatrol.utils.DialogActions
import com.muhammhassan.epatrol.utils.DialogData
import compose.icons.Octicons
import compose.icons.octicons.Eye24
import compose.icons.octicons.EyeClosed24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    state: UiState<UserModel>?,
    email: String,
    onEmailChanged: (value: String) -> Unit,
    password: String,
    onPasswordChanged: (value: String) -> Unit,
    onSaveButton: () -> Unit,
    onResponseSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPasswordShow = remember { mutableStateOf(false) }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val dialogData = remember {
        mutableStateOf(DialogData.init())
    }
    val isDialogShow = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state, block = {
        when (state) {
            is UiState.Error -> {
                isLoading.value = false
                dialogData.value = DialogData("Pemberitahuan",
                    state.message,
                    ButtonType.NEUTRAL,
                    action = object : DialogActions {
                        override fun onConfirmAction() {

                        }

                        override fun onCancelAction() {

                        }

                        override fun onNeutralAction() {
                            isDialogShow.value = false
                        }

                    })
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

    if (isDialogShow.value) {
        Dialog(onDismissRequest = { isDialogShow.value = false }) {
            DialogContent(
                message = dialogData.value.message,
                title = dialogData.value.title,
                buttonType = dialogData.value.buttonType,
                action = dialogData.value.action
            )
        }
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (icon, title, subtitle, edtEmail, edtPassword, forgetPassword, btnMasuk, layoutRegister, loading) = createRefs()
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
                onValueChange = onEmailChanged,
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Primary
                )
            )
            OutlinedTextField(value = password,
                onValueChange = onPasswordChanged,
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
                    imeAction = ImeAction.Go
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Primary
                ),
                visualTransformation = if (isPasswordShow.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordShow.value = !isPasswordShow.value }) {
                        Icon(
                            imageVector = if (isPasswordShow.value) Octicons.EyeClosed24 else Octicons.Eye24,
                            contentDescription = if (isPasswordShow.value) "Hide Password" else "Show Password"
                        )
                    }
                })
            Text(text = "Lupa Password?", modifier = Modifier.constrainAs(forgetPassword) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(edtPassword.bottom, 8.dp)
            }, fontWeight = FontWeight.Medium, color = Primary)
            Button(
                onClick = onSaveButton,
                modifier = Modifier.constrainAs(btnMasuk) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(layoutRegister.top, 8.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(45.dp)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(text = "Masuk", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.White)
            }

            Row(modifier = Modifier.constrainAs(layoutRegister) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 16.dp)
            }) {
                Text(
                    text = "Belum punya akun? ",
                    modifier = Modifier,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
                Text(text = "Daftar", modifier = Modifier, color = Secondary, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    EPatrolTheme {
        LoginView(email = "",
            onEmailChanged = {},
            onSaveButton = {},
            password = "",
            onPasswordChanged = {},
            state = null,
            onResponseSuccess = {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview2() {
    EPatrolTheme {
        LoginView(email = "",
            onEmailChanged = {},
            onSaveButton = {},
            password = "",
            onPasswordChanged = {},
            state = UiState.Loading,
            onResponseSuccess = {})
    }
}