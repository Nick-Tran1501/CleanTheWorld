package com.example.cleantheworld.ui.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationPicker(onLocationSelected: (LatLng) -> Unit) {
    var selectedPosition by remember { mutableStateOf<LatLng?>(LatLng(10.8231, 106.62977)) }
    val cameraPositionState = rememberCameraPositionState {
        this.position =
            selectedPosition?.let { CameraPosition.fromLatLngZoom(it, 15f) }!! // Default position
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        selectedPosition?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Selected Location",
                draggable = true,
                onInfoWindowClick = { marker ->
                    Log.d(TAG, " This is new loca: ${selectedPosition.toString()}")
                    Log.d(TAG, " This is new loca: ${marker.position}")
                    onLocationSelected(marker.position)
                }
            )
        }
    }

    // Detect map taps to update the marker position
    LaunchedEffect(key1 = cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedPosition = cameraPositionState.position.target
            selectedPosition?.let { onLocationSelected(it) }

        }
    }
}