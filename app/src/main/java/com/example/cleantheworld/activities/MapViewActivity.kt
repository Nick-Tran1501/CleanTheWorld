package com.example.cleantheworld.activities

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cleantheworld.models.parseDirtyLevel
import com.example.cleantheworld.ui.components.CustomMapMaker
import com.example.cleantheworld.utils.GoogleRouteAPI
import com.example.cleantheworld.utils.getUserCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapViewActivity(
    navController: NavController,
    latitude: Double,
    longitude: Double,
    dirtyLevel: String
) {
    val context = LocalContext.current
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    var currentLocation by remember {
        mutableStateOf<Location?>(null)
    }

    val startingLocation by remember {
        mutableStateOf(LatLng(latitude, longitude))
    }

    if (locationPermissionState.status.isGranted) {
        LaunchedEffect(Unit) {
            currentLocation = getUserCurrentLocation(context)
        }
    }

    var route by remember {
        mutableStateOf<List<LatLng>>(emptyList())
    }

    if (currentLocation != null) {
        LaunchedEffect(Unit) {
            route = GoogleRouteAPI.getRoute(
                LatLng(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                ), LatLng(latitude, longitude)
            )
        }
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingLocation, 10f)
        if (currentLocation != null) {
            position = CameraPosition.fromLatLngZoom(
                LatLng(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                ), 20f
            )
        }
    }
    val dirtyLevelParse = parseDirtyLevel(dirtyLevel)
    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted),
        uiSettings = MapUiSettings(myLocationButtonEnabled = locationPermissionState.status.isGranted),
    ) {
        CustomMapMaker(
            context = context,
            position = LatLng(latitude, longitude),
            title = "Route",
            description = "To the Site",
            siteLevel = dirtyLevelParse
        )
        if (route.isNotEmpty()) {
            Polyline(points = route, color = MaterialTheme.colors.primary, width = 8f)
        }
    }


}