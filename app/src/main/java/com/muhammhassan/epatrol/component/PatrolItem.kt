package com.muhammhassan.epatrol.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Green
import com.muhammhassan.epatrol.ui.theme.Green20
import com.muhammhassan.epatrol.ui.theme.Red
import com.muhammhassan.epatrol.ui.theme.Red20
import compose.icons.Octicons
import compose.icons.octicons.Calendar24
import compose.icons.octicons.Clock24
import compose.icons.octicons.KebabHorizontal24

@Composable
fun PatrolItem(model: PatrolModel, onItemClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (status, iconDate, date, iconHour, hour, title, targetAddress, menu) = createRefs()
            if (model.status == "belum-dikerjakan") {
                Text("Belum Dikerjakan",
                    modifier = Modifier
                        .constrainAs(status) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(Red20)
                        .padding(12.dp, 2.dp),
                    color = Red)
            } else {
                Text("Sedang Dikerjakan",
                    modifier = Modifier
                        .constrainAs(status) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(Green20)
                        .padding(12.dp, 2.dp),
                    color = Green)
            }

            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(menu) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
            }) {
                Icon(imageVector = Octicons.KebabHorizontal24, contentDescription = "More", modifier = Modifier
                    .rotate(90f)
                    .size(24.dp))
            }

            Text(
                text = model.title,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(status.bottom, 8.dp)
                    start.linkTo(parent.start)
                },
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Octicons.Calendar24,
                contentDescription = "Date",
                modifier = Modifier.constrainAs(iconDate) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom, 8.dp)
                }, tint = Color.Gray)
            Text(text = model.date, modifier = Modifier.constrainAs(date) {
                top.linkTo(title.bottom, 8.dp)
                start.linkTo(iconDate.end, 8.dp)
            }, color = Color.Gray)

            Icon(imageVector = Octicons.Clock24,
                contentDescription = "Date",
                modifier = Modifier.constrainAs(
                    iconHour
                ) {
                    start.linkTo(date.end, 16.dp)
                    top.linkTo(title.bottom, 8.dp)
                }, tint = Color.Gray)
            Text(text = model.hour, modifier = Modifier.constrainAs(hour) {
                top.linkTo(title.bottom, 8.dp)
                start.linkTo(iconHour.end, 8.dp)
            }, color = Color.Gray)

            Text(text = model.address, modifier = Modifier.constrainAs(targetAddress) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(iconDate.bottom, 8.dp)
                width = Dimension.fillToConstraints
            }, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun PatrolItemPreview() {
    EPatrolTheme {
        PatrolItem(model = PatrolModel(
            id = 0,
            title = "Patroli Keamanan Malam Hari",
            date = "22 Juni 2023",
            status = "belum-dikerjakan",
            hour = "20.00",
            address = "Jl. Dr. Soetomo",
            verified = false,
            lead = "ade@email.co"
        ), onItemClick = { /*TODO*/ })
    }
}


@Preview
@Composable
fun PatrolItemPreview2() {
    EPatrolTheme {
        PatrolItem(model = PatrolModel(
            id = 1,
            title = "Patroli Keamanan Malam Hari",
            date = "22 Juni 2023",
            status = "sudah-dikerjakan",
            hour = "20.00",
            address = "Jl. Dr. Soetomo",
            verified = false,
            lead = "ade@email.co"
        ), onItemClick = { /*TODO*/ })
    }
}