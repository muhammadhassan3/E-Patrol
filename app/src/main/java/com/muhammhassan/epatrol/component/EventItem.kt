package com.muhammhassan.epatrol.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ChevronRight24

@Composable
fun EventItem(
    data: PatrolEventModel,
    onItemClick: (data: PatrolEventModel) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            val (indicator, titleConst, dateCost, expandButton) = createRefs()
            Text(
                text = data.title, modifier = Modifier.constrainAs(titleConst) {
                    start.linkTo(indicator.end, 8.dp)
                    top.linkTo(expandButton.top)
                    bottom.linkTo(expandButton.bottom)
                    end.linkTo(expandButton.start, 8.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }, maxLines = 1, overflow = TextOverflow.Ellipsis, style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            )
            Canvas(modifier = Modifier.constrainAs(indicator) {
                start.linkTo(parent.start)
                top.linkTo(titleConst.top)
                bottom.linkTo(dateCost.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.value(4.dp)
            }, onDraw = {
                drawRect(Secondary)
            })
            IconButton(onClick = {
                onItemClick(data)
            }, modifier = Modifier.constrainAs(expandButton) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ChevronRight24),
                    contentDescription = "Perluas",
                    modifier = Modifier
                )
            }

            Text(text = data.createdAt, modifier = Modifier.constrainAs(dateCost) {
                start.linkTo(indicator.end, 8.dp)
                top.linkTo(titleConst.bottom, 4.dp)
            }, style = TextStyle(fontSize = 12.sp))
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
                "Ini ringkasan kejadian",
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