package com.example.cleantheworld.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class RouteResponse(val routes: List<Route>)
data class Route(val overview_polyline: Polyline)
data class Polyline(val points: String)

val TAG: String = "GOOGLE_DIRECTION"

interface RouteService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Response<RouteResponse>
}

object GoogleRouteAPI {
    private val api = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RouteService::class.java)

    suspend fun getRoute(curUserLoca: LatLng, destination: LatLng): List<LatLng> {
        var polylinePoints = emptyList<LatLng>()
        val res = api.getDirections(
            origin = "${curUserLoca.latitude},${curUserLoca.longitude}",
            destination = "${destination.latitude},${destination.longitude}",
            apiKey = "AIzaSyDLq8zrmEZsKrmVqUHtvQ0mNZPt3fgISoA"
        )
        if (res.isSuccessful) {
            Log.i(TAG, "onSuccess: called google direction success")
            val routes = res.body()?.routes ?: emptyList()
            if (routes.isNotEmpty()) {
                polylinePoints = PolyUtil.decode(routes[0].overview_polyline.points)
            }
        }

        return polylinePoints
    }
}