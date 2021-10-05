package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.models.geocodingAPI.Location
import com.openclassrooms.realestatemanager.repositories.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.observers.DisposableObserver
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (private val locationRepository: LocationRepository): ViewModel() {

    private val API_KEY: String = BuildConfig.API_KEY
    private val perms = "Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION"

    val currentUserPosition = MutableLiveData<LatLng>()

    fun getCurrentUserPosition(): LatLng? {
        return currentUserPosition.value
    }

    fun updateCurrentUserPosition(latLng: LatLng) {
        currentUserPosition.value = latLng
    }

    fun getCurrentUserPositionFormatted(): String {
        val location = currentUserPosition.value.toString().replace("lat/lng: (", "")
        return location.replace(")", "")
    }

    fun getCurrentUserPositionF(): String? {
        return if (getCurrentUserPosition() != null) {
            val location = getCurrentUserPositionFormatted()
            location.replace(")", "")
        } else null
    }

    @SuppressLint("MissingPermission")
    fun startLocationRequest(context: Context?) {
        if (EasyPermissions.hasPermissions(context!!, perms)) {
            locationRepository.startLocationRequest()
        }
    }

    fun getLocation(): LiveData<Location?>? {
        return locationRepository.getLocationLiveData()
    }

    fun stopLocationRequest() {
        locationRepository.stopLocationRequest()
    }

}