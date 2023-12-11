package com.example.cleantheworld.ui.components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.cleantheworld.R
import com.example.cleantheworld.models.DirtyLevel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun CustomMapMaker(
    context: Context,
    position: LatLng,
    title: String,
    description: String,
    siteLevel: DirtyLevel
) {

    val imageRes = when (siteLevel) {
        DirtyLevel.HIGH -> R.drawable.trash_high
        DirtyLevel.MEDIUM -> R.drawable.trash_medium
        DirtyLevel.LOW -> R.drawable.trash_low
        else -> R.drawable.trash_cleaned
    }
    val icon = bitmapDescriptorFromVector(context, imageRes)
    Marker(
        state = MarkerState(position = position),
        title = title,
        snippet = description,
        icon = icon

    )
}


fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}