package com.muhammhassan.epatrol.presentation.home.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.muhammhassan.epatrol.component.SettingsItem
import com.muhammhassan.epatrol.domain.model.UserModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import com.muhammhassan.epatrol.ui.theme.Red
import com.muhammhassan.epatrol.ui.theme.Red20
import compose.icons.Octicons
import compose.icons.octicons.Globe24
import compose.icons.octicons.People24
import compose.icons.octicons.Person24
import compose.icons.octicons.Repo24
import compose.icons.octicons.Report24
import compose.icons.octicons.ShieldCheck24
import timber.log.Timber

@Composable
fun ProfileView(user: UserModel, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profileImage).diskCacheKey(user.profileImage)
                    .diskCachePolicy(CachePolicy.ENABLED).build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator()
                    },
                    error = {
                        Timber.e(it.result.throwable)
                        Icon(
                            painter = rememberVectorPainter(image = Octicons.Person24),
                            contentDescription = "Icon profile"
                        )
                    })
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = user.jabatan,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                    )
                    Text(
                        text = "NRP ${user.nrp}",
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pengaturan",
                    modifier = Modifier,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(text = "Bahasa", icon = Octicons.Globe24)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(text = "Tentang Aplikasi", icon = Octicons.Report24)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(text = "Pusat Bantuan", icon = Octicons.People24)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(text = "Syarat dan Ketentuan", icon = Octicons.Repo24)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(text = "Kebijakan Privasi", icon = Octicons.ShieldCheck24)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Red20, contentColor = Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Keluar")
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    EPatrolTheme {
        ProfileView(
            user = UserModel(
                "AIPDA Amin Prasetyo, S.H.",
                "",
                "",
                "Banit Turjawali Satsamapta Polresta Cilacap",
                "78070751"
            ),
            onLogout = {}
        )
    }
}

