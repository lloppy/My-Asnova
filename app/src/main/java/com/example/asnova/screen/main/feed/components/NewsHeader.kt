package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.asnova.R
import com.example.asnova.data.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NewsHeader(
    userData: UserData?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = stringResource(id = R.string.feed),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = stringResource(R.string.profile_picture),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CurrentDateText()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Добро пожаловать${if (userData?.username.isNullOrEmpty()) "!" else ",\n${userData!!.username}"}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }

}


@Composable
fun CurrentDateText() {
    val locale = Locale("ru", "RU")
    val currentDate = remember { Date() }

    val dateFormat = SimpleDateFormat("EEEE, d MMMM", locale)
    val dateString = dateFormat.format(currentDate)

    Text(
        text = dateString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() },
        color = Color.Gray,
        style = MaterialTheme.typography.bodyLarge,
    )
}