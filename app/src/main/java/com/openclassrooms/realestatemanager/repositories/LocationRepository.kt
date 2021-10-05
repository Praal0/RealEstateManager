package com.openclassrooms.realestatemanager.repositories

import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.geocodingAPI.Location
import javax.inject.Inject

class LocationRepository@Inject constructor(){
    private val LOCATION_REQUEST_INTERVAL_MS = 10000
    private val SMALLEST_DISPLACEMENT_THRESHOLD_METER : Float = 25F

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationMutableLiveData: MutableLiveData<Location?> = MutableLiveData(null)

    private var callback: LocationCallback? = null

    fun getLocationLiveData(): LiveData<Location?>? {
        return locationMutableLiveData
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startLocationRequest() {
        if (callback == null) {
            callback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = Location()
                    locationMutableLiveData.setValue(location)
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

    fun stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }
}