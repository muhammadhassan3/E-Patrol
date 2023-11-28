package com.muhammhassan.epatrol.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.muhammhassan.epatrol.ui.theme.Primary
import compose.icons.Octicons
import compose.icons.octicons.Image24

@Composable
fun AsyncImage(token: String, url: String, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url)
            .addHeader("Authorization", "Bearer $token").crossfade(true).diskCacheKey(url)
            .diskCachePolicy(CachePolicy.ENABLED).build(),
        contentDescription = "Gambar kejadian",
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp)),
        error = {
            Box(modifier = Modifier, contentAlignment = Alignment.Center){
                Icon(
                    painter = rememberVectorPainter(image = Octicons.Image24),
                    contentDescription = "Gagal memuat gambar"
                )
            }
        },
        loading = {
            Box(modifier = Modifier, contentAlignment = Alignment.Center){
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(55.dp), color = Primary
                )
            }
        },
        contentScale = ContentScale.Inside
    )
}