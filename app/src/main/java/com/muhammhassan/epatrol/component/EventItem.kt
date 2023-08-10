package com.muhammhassan.epatrol.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ArrowUpRight24
import compose.icons.octicons.Clock24

@Composable
fun EventItem(
    data: PatrolEventModel,
    onItemClick: (data: PatrolEventModel) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.Clock24),
                    contentDescription = "Icon Waktu",
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = data.createdAt, style = TextStyle(fontSize = 10.sp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.title,
                modifier = Modifier,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.summary,
                modifier = Modifier,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    onItemClick(data)
                },
                modifier = Modifier,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary),
                border = BorderStroke(1.dp, Secondary),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp)
            ) {
                Text(text = "Lihat selengkapnya", style = TextStyle(fontSize = 12.sp))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ArrowUpRight24),
                    contentDescription = "Baca Selengkapnya",
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EventItemPreview() {
    EPatrolTheme {
        EventItem(
            data = PatrolEventModel(
                0L,
                "",
                "Patroli keamanan Malam Hari bertujuan untuk mencegah tindakan kriminal seperti perampokan, pencurian, vandalisme",
                "Kecelakaan lalu lintas",
                "Evakuasi korban",
                createdAt = "2 Juni 2023 13:24",
                lat = 0.0,
                long = 0.0
            ),
            onItemClick = {}
        )
    }
}