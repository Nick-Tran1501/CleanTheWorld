package com.example.cleantheworld.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.DirtyLevel
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager
import com.example.cleantheworld.ui.components.CustomSearchBar
import com.example.cleantheworld.ui.components.SiteCard
import com.example.cleantheworld.ui.components.SortDropdown
import com.example.cleantheworld.utils.ThemeViewModel

@Composable
fun MySites(curUserId: String, themeViewModel: ThemeViewModel, navController: NavController) {
    var sites by remember {
        mutableStateOf<List<CleanUpSite>>(mutableListOf())
    }
    var sortOption by remember { mutableStateOf("None") }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sites = CleanUpSiteManager.getSiteById(curUserId)
    }
    val filteredAndSortedSites = sites
        .filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(
                searchQuery,
                ignoreCase = true
            )
        }
        .sortedWith { site1, site2 ->
            when (sortOption) {
                "High" -> compareValuesBy(site1, site2) { it.level != DirtyLevel.HIGH }
                "Medium" -> compareValuesBy(site1, site2) { it.level != DirtyLevel.MEDIUM }
                "Low" -> compareValuesBy(site1, site2) { it.level != DirtyLevel.LOW }
                "Cleaned" -> compareValuesBy(site1, site2) { it.level != DirtyLevel.CLEANED }
                else -> 0 // No sorting
            }
        }



    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Text(
            "My Sites",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        CustomSearchBar(onQueryChanged = { query ->
            searchQuery = query
        })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SortDropdown(onSortChanged = { option ->
                sortOption = option
            })

            Button(onClick = { /* TODO: Handle click */ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add New",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("New")
            }
        }


        LazyColumn {
            items(filteredAndSortedSites) { site ->
                SiteCard(site, themeViewModel, navController, curUserId, onJoinSite = { siteId ->
                    CleanUpSiteManager.joinSite(siteId, curUserId)
                })
            }
        }
    }
}