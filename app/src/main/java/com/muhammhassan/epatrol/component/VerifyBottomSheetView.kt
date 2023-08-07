package com.muhammhassan.epatrol.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Green

@Composable
fun VerifyBottomSheetView(plate: String, onSwipe: () -> Unit, modifier: Modifier = Modifier) {
    val plateState = remember {
        mutableStateOf(false)
    }
    val firearmsState = remember {
        mutableStateOf(false)
    }
    val handcuffsState = remember {
        mutableStateOf(false)
    }
    val privateEquipmentState = remember {
        mutableStateOf(false)
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        val (labelPlate, labelFirearms, labelEquipment, labelHandcuffs, swipeButton) = createRefs()
        SwitchLabel(title = "Kendaraan dengan nomor\n$plate",
            state = plateState.value,
            onStateChanged = {
                plateState.value = it
            },
            modifier = Modifier.constrainAs(labelPlate) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            })
        SwitchLabel(title = "Senjata Api", state = firearmsState.value, onStateChanged = {
            firearmsState.value = it
        }, modifier = Modifier.constrainAs(labelFirearms) {
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            top.linkTo(labelPlate.bottom, 8.dp)
            width = Dimension.fillToConstraints
        })
        SwitchLabel(title = "Borgol", state = handcuffsState.value, onStateChanged = {
            handcuffsState.value = it
        }, modifier = Modifier.constrainAs(labelHandcuffs) {
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            top.linkTo(labelFirearms.bottom, 8.dp)
            width = Dimension.fillToConstraints
        })
        SwitchLabel(title = "Perlengkapan Pribadi",
            state = privateEquipmentState.value,
            onStateChanged = {
                privateEquipmentState.value = it
            },
            modifier = Modifier.constrainAs(labelEquipment) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                top.linkTo(labelHandcuffs.bottom, 8.dp)
                width = Dimension.fillToConstraints
            })
        SwipeButton(
            enabled = (plateState.value && firearmsState.value && handcuffsState.value && privateEquipmentState.value),
            onSwipe = onSwipe,
            modifier = Modifier.constrainAs(swipeButton) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                top.linkTo(labelEquipment.bottom, 16.dp)
                width = Dimension.fillToConstraints
            })
    }
}

@Composable
fun SwitchLabel(
    title: String,
    state: Boolean,
    onStateChanged: (value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Switch(
                checked = state,
                onCheckedChange = onStateChanged,
                colors = SwitchDefaults.colors(checkedTrackColor = Green)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.align(Alignment.CenterVertically),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyBottomSheetPreview() {
    EPatrolTheme {
        VerifyBottomSheetView(plate = "R 1234 PNC", onSwipe = {})
    }
}

@Preview
@Composable
fun SwitchLabelPreview() {
    EPatrolTheme {
        SwitchLabel(title = "Senjata Api", state = false, onStateChanged = {})
    }
}

@Preview
@Composable
fun SwitchLabelPreview2() {
    EPatrolTheme {
        SwitchLabel(title = "Senjata Api", state = true, onStateChanged = {})
    }
}