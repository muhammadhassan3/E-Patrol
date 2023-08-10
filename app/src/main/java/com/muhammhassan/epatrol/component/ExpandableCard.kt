package com.muhammhassan.epatrol.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ChevronRight24

@Composable
fun ExpandableCard(
    title: String,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
) {
    val isContentExpand = remember {
        mutableStateOf(false)
    }
    val angleAnimation = animateFloatAsState(
        targetValue = if (isContentExpand.value) 270f else 90f, label = "Rotate button"
    )
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .animateContentSize()
        ) {
            val (titleConst, indicator, expandButton, contentConst) = createRefs()
            Text(
                text = title, modifier = Modifier.constrainAs(titleConst) {
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
                bottom.linkTo(titleConst.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.value(4.dp)
            }, onDraw = {
                drawRect(Secondary)
            })
            IconButton(onClick = {
                isContentExpand.value = !isContentExpand.value
            }, modifier = Modifier.constrainAs(expandButton) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }) {
                Icon(
                    painter = rememberVectorPainter(image = Octicons.ChevronRight24),
                    contentDescription = "Perluas",
                    modifier = Modifier.rotate(angleAnimation.value)
                )
            }
            if (isContentExpand.value) {
                Column(modifier = Modifier.constrainAs(contentConst) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(expandButton.bottom)
                }) {
                    AnimatedVisibility(
                        visible = isContentExpand.value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(modifier = Modifier)
                            Spacer(modifier = Modifier.height(8.dp))
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ExpandableCardPreview() {
    EPatrolTheme {
        ExpandableCard(title = "Ini Judul", content = {
            Text(text = "Ini isi kontennya")
        })
    }
}