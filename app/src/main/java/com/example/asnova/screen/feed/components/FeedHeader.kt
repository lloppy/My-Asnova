package com.example.asnova.screen.feed.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.ui.theme.FeedItemHeight
import com.example.asnova.ui.theme.blackShadesLinearMini
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HeaderSection(
    userData: User?,
    segments: List<String>,
    selectedSegment: String,
    pictureBackgroundId: Int,
    onSegmentSelected: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painterResource(id = pictureBackgroundId),
                contentScale = ContentScale.Crop
            )
            .background(blackShadesLinearMini)
            .height(
                screenHeight
                    .minus(BottomBarHeight)
                    .minus(FeedItemHeight)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
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
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
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

                if (userData != null) {
                    Text(
                        text = "С возвращением${
                            if (!userData.surname.isNullOrEmpty() && !userData.name.isNullOrEmpty()) {
                                ",\n${userData.name} ${userData.surname}" 
                            } else if (!userData?.name.isNullOrEmpty()) {
                                ",\n${userData.name}"
                            } else if (!userData?.email.isNullOrEmpty()) {
                                ",\n${userData.email}"
                            } else if (userData?.username.isNullOrEmpty()) {
                                "!"
                            } else {
                                ",\n${userData!!.username}"
                            }
                        }",
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            SegmentedControl(
                segments = segments,
                selectedSegment = selectedSegment,
                onSegmentSelected = onSegmentSelected,
                modifier = Modifier.height(52.dp)
            ) {
                SegmentText(it, selectedSegment == it)
            }
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
        color = Color.LightGray,
        style = MaterialTheme.typography.bodyLarge,
    )
}