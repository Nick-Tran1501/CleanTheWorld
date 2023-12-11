package com.example.cleantheworld.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleantheworld.R
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager
import com.example.cleantheworld.ui.components.CustomMapMaker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SiteDetailActivity(siteId: String, navController: NavController) {
    var site by remember { mutableStateOf<CleanUpSite?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    var startingLocation by remember {
        mutableStateOf(LatLng(10.762622, 106.660172))
    }

    LaunchedEffect(siteId) {
        coroutineScope.launch {
            site = CleanUpSiteManager.getSiteDetailById(siteId)
            site?.let { Log.d(TAG, it.name) }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingLocation, 10f)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { site?.let { Text(it.name, fontWeight = FontWeight.Bold) } },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            site?.let {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = locationPermissionState.status.isGranted)
                ) {
                    CustomMapMaker(
                        context = context,
                        position = LatLng(it.latitude, it.longitude),
                        title = "Clean up site: ${it.name}",
                        description = it.description,
                        siteLevel = it.level
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Dirty Level: ${site?.level}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Site Details
            Text("Description: ${site?.description}", style = MaterialTheme.typography.bodyMedium)
            // ... other details ...

            Spacer(modifier = Modifier.height(16.dp))

            // Direction button
            Button(
                onClick = { /* TODO: Navigate to MapView */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.direction),
                    contentDescription = "Directions"
                )
                Text("Directions", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}