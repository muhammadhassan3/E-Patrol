package com.muhammhassan.epatrol.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary

@Composable
fun ConfirmDialogView(
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    subMessage: String? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Konfirmasi",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )
            subMessage?.let {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = it, style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 12.sp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Secondary)
                ) {
                    Text(text = "Kembali", style = TextStyle(color = Secondary
                    ))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                ) {
                    Text(text = "Lanjutkan")
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    EPatrolTheme {
        ConfirmDialogView(
            message = "Apakah anda yakin akan menyelesaikan patroli ini?",
            subMessage = "Pastikan semua area telah diperiksa dengan cermat sebelum menyelesaikannya",
            onDismiss = {  },
            onConfirm = {  })
    }
}