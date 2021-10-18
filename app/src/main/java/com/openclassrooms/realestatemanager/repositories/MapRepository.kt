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

class MapRepository @Inject constructor(){
    private val LOCATION_REQUEST_INTERVAL_MS = 10000
    private val SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25f

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationMutableLiveData: MutableLiveData<Location?> = MutableLiveData(null)

    private lateinit var callback: LocationCallback

    fun getLocationLiveData(): MutableLiveData<Location?> {
        return locationMutableLiveData
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startLocationRequest() {
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