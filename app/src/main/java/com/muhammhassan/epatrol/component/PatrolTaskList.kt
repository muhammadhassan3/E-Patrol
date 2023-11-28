package com.muhammhassan.epatrol.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammhassan.epatrol.domain.model.PatrolModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PatrolTaskList(
    data: List<PatrolModel>,
    onItemClick: (data: PatrolModel) -> Unit,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        content = {
            items(data, key = { it.id }) {
                PatrolItem(model = it, onItemClick = onItemClick, modifier = Modifier)
            }
        },
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 8.dp, bottom = 16.dp)
    )
    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Belum ada tugas patroli",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 18.sp)
            )
        }
    }
}