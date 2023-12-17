package com.example.cleantheworld.ui.layout

import android.Manifest
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.DirtyLevel
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager.getNearbySites
import com.example.cleantheworld.ui.components.CustomSearchBar
import com.example.cleantheworld.ui.components.SiteCard
import com.example.cleantheworld.ui.components.SortDropdown
import com.example.cleantheworld.utils.ThemeViewModel
import com.example.cleantheworld.utils.getUserCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NearbySites(themeViewModel: ThemeViewModel, curUserId: String, navController: NavController) {
    val context = LocalContext.current
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    var currentUserLocation by remember { mutableStateOf<Location?>(null) }
    var sites by remember {
        mutableStateOf<List<CleanUpSite>>(mutableListOf())
    }
    var sortOption by remember { mutableStateOf("None") }
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        sites = CleanUpSiteManager.getAllSites()
    }
    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            currentUserLocation = getUserCurrentLocation(context)
        }
    }
    val nearbySites = currentUserLocation?.let { location ->
        getNearbySites(location, sites, maxDistance = 10000f)
    } ?: listOf()
    val filteredAndSortedSites = nearbySites
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
            "Nearby Sites",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        CustomSearchBar(onQueryChanged = { query ->
            searchQuery = query
        })

        SortDropdown(onSortChanged = { option ->
            sortOption = option
        })

        LazyColumn {
            items(filteredAndSortedSites) { site ->
                SiteCard(site, themeViewModel, navController, curUserId, onJoinSite = { siteId ->
                    CleanUpSiteManager.joinSite(siteId, curUserId)
                })
            }
        }
    }
}