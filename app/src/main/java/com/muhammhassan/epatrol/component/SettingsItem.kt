package com.muhammhassan.epatrol.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Orange20
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ChevronRight24
import compose.icons.octicons.Globe24

@Composable
fun SettingsItem(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Orange20)
                .padding(8.dp)
        ) {
            Icon(
                painter = rememberVectorPainter(image = icon),
                contentDescription = "Icon $text",
                modifier = Modifier,
                tint = Secondary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 12.sp))
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = rememberVectorPainter(image = Octicons.ChevronRight24),
            contentDescription = "Tampilkan lebih lanjut",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
fun SettingsItemPreview() {
    EPatrolTheme {
        SettingsItem(text = "Bahasa", Octicons.Globe24)
    }
}