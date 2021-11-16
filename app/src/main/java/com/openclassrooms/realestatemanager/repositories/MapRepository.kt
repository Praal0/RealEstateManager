package com.openclassrooms.realestatemanager.repositories

import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.openclassrooms.realestatemanager.models.geocodingAPI.Location
import javax.inject.Inject

class MapRepository @Inject constructor(fusedLocationProviderClient: FusedLocationProviderClient){
    private val LOCATION_REQUEST_INTERVAL_MS = 10000
    private val SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25f

    private var fusedLocationProviderClient: FusedLocationProviderClient = fusedLocationProviderClient

    private val locationMutableLiveData: MutableLiveData<Location?> = MutableLiveData(null)

    private var callback: LocationCallback? = null

    fun getLocationLiveData(): MutableLiveData<Location?> {
        return locationMutableLiveData
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startLocationRequest() {
        if (callback == null) {
            callback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = Location(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )
                    locationMutableLiveData.value = location
                }
            }
        }
        fusedLocationProviderClient.removeLocationUpdates(callback)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                .setInterval(LOCATION_REQUEST_INTERVAL_MS.toLong()),
            callback,
            Looper.getMainLooper()
        )
    }
}