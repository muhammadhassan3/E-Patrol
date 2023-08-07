package com.muhammhassan.epatrol.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Secondary
import compose.icons.Octicons
import compose.icons.octicons.ChevronRight24
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeButton(enabled: Boolean, onSwipe: () -> Unit, modifier: Modifier = Modifier) {
    val swipeState = rememberSwipeableState(initialValue = 0)
    val width = 308.dp
    val widthInPx =
        with(LocalDensity.current) {
            width.toPx()
        }

    val anchor = mapOf(0f to 0, widthInPx to 1)
    val (swipeComplete, setSwipeComplete) = remember{
        mutableStateOf(false)
    }
    val alpha: Float by animateFloatAsState(
        targetValue = if (swipeComplete) {
            0f
        }else{
            1f
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        ), label = "Alpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = (1f-(swipeState.offset.value/1078)),
        visibilityThreshold = 0.3f,
        label = "Text Alpha"
    )

    LaunchedEffect(key1 = swipeState.currentValue, block = {
        if(swipeState.currentValue == 1){
            setSwipeComplete(true)
            onSwipe.invoke()
        }
    })
    if (enabled){
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(100))
                .background(color = Secondary)
                .animateContentSize()
                .fillMaxWidth()
        ) {
            SwipeIndicator(modifier = Modifier
                .alpha(alpha)
                .padding(8.dp)
                .swipeable(
                    state = swipeState,
                    anchors = anchor,
                    orientation = Orientation.Horizontal,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) })
                .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
            )
            Text(
                text = "Geser untuk melakukan verfikasi",
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(textAlpha),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
            )
        }
    }else{
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(100))
                .background(color = Color.Gray)
                .fillMaxWidth()
        ) {
            SwipeIndicator(modifier = Modifier
                .alpha(0f)
                .padding(8.dp))
            Text(
                text = "Silahkan pastikan semua sudah diperiksa",
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(textAlpha),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
            )
        }
    }
}

@Composable
fun SwipeIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .clip(CircleShape)
        .background(color = Color.White)
        .size(42.dp)
    ) {
        Icon(
            painter = rememberVectorPainter(image = Octicons.ChevronRight24),
            contentDescription = "Geser untuk melanjutkan",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showSystemUi = false)
@Composable
fun SwipeButtonPreview() {
    EPatrolTheme {
        SwipeButton(enabled = true, onSwipe = {})
    }
}
@Preview(showSystemUi = false)
@Composable
fun DisabledSwipeButtonPreview() {
    EPatrolTheme {
        SwipeButton(enabled = false, onSwipe = {})
    }
}