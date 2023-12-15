package com.example.cleantheworld.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.DirtyLevel
import com.example.cleantheworld.utils.ThemeViewModel

@Composable
fun SiteCard(
    site: CleanUpSite,
    themeViewModel: ThemeViewModel,
    navController: NavController,
    userId: String,
    onJoinSite: (String) -> Unit
) {

    val isDarkTheme = themeViewModel.isDarkTheme.value
    val imageRes = when {
        site.level == DirtyLevel.HIGH && isDarkTheme -> R.drawable.high_dark
        site.level == DirtyLevel.HIGH && !isDarkTheme -> R.drawable.high_light
        site.level == DirtyLevel.MEDIUM && isDarkTheme -> R.drawable.medium_dark
        site.level == DirtyLevel.MEDIUM && !isDarkTheme -> R.drawable.medium_light
        site.level == DirtyLevel.LOW && isDarkTheme -> R.drawable.low_dark
        site.level == DirtyLevel.LOW && !isDarkTheme -> R.drawable.low_light
        site.level == DirtyLevel.CLEANED && isDarkTheme -> R.drawable.cleaned_dark
        else -> R.drawable.cleaned_light
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                navController.navigate("site_detail/${site.id}")
            },
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

                .padding(16.dp)
        ) {
            // Image - Replace with actual image resource or URL
            Image(
                painter = painterResource(id = imageRes), // Replace with actual image resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Site Name
            Text(
                text = site.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Site Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = site.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (userId !in site.participantIds) {
                    Button(
                        onClick = { onJoinSite(site.id) },
                    ) {
                        Text("Join")
                    }
                }
            }

        }
    }
}