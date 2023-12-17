package com.example.cleantheworld.activities

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.myFirebaseManager.CleanUpSiteManager
import com.example.cleantheworld.ui.components.CustomMapMaker
import com.example.cleantheworld.utils.getUserCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AllMapView(navController: NavController) {
    var sites by remember {
        mutableStateOf<List<CleanUpSite>>(mutableListOf())
    }
    var currentUserLocation by remember { mutableStateOf<Location?>(null) }
    val startingLocation by remember {
        mutableStateOf(LatLng(10.775659, 106.7247))
    }

    val context = LocalContext.current
    val locationPermission =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    if (locationPermission.status.isGranted) {
        LaunchedEffect(Unit) {
            currentUserLocation = getUserCurrentLocation(context)
        }
    }

    LaunchedEffect(Unit) {
        sites = CleanUpSiteManager.getAllSites()
    }
    val cameraPositionState = rememberCameraPositionState {
        position = if (currentUserLocation != null) {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    currentUserLocation!!.latitude,
                    currentUserLocation!!.longitude
                ), 5f
            )
        } else {
            CameraPosition.fromLatLngZoom(startingLocation, 13f)
        }
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = locationPermission.status.isGranted),
        uiSettings = MapUiSettings(myLocationButtonEnabled = locationPermission.status.isGranted),

        ) {

        sites.forEach { site ->
            CustomMapMaker(
                context = context,
                position = LatLng(site.latitude, site.longitude),
                title = "${site.name} location",
                description = "To the Site",
                siteLevel = site.level,
                onClick = {
                    navController.navigate("site_detail/${site.id}")
                    true
                }
            )
        }


    }
}