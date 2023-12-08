package com.example.cleantheworld.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R

@Composable
fun BottomNavigationBar(navController: NavController, admin: Boolean) {
    val items = mutableListOf(

        BottomNavItem("My Sites", R.drawable.my_sites), // Replace with your actual icon resource
        BottomNavItem("Profile", R.drawable.profile)
    )

    if (admin) {
        items.add(
            0,
            BottomNavItem("All Sites", R.drawable.sites)
        ) // Add "All Sites" at the beginning if isAdmin is true
    } else {
        items.add(0, BottomNavItem("Nearby", R.drawable.sites))
    }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { /* Icon for the item */
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.icon),
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                selected = false, // Implement logic to determine if the item is selected
                onClick = {
                    navController.navigate(
                        when (item.title) {
                            "All Sites" -> "list_of_sites"
                            "My Sites" -> "my_list_of_sites"
                            "Nearby" -> "nearby_list_of_sites"
                            else -> "profile"
                        }
                    )
                }
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: Int)


