package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.models.geocodingAPI.Location
import com.openclassrooms.realestatemanager.repositories.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class MapViewModel@Inject constructor (private val mapRepository: MapRepository): ViewModel() {

    private val perms = "Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION"

    val currentUserPosition = MutableLiveData<LatLng>()


    @SuppressLint("MissingPermission")
    fun startLocationRequest(context: Context) {
        if (EasyPermissions.hasPermissions(context, perms)) {
            mapRepository.startLocationRequest()
        }
    }

    fun getLocation(): LiveData<Location?>? {
        return mapRepository.getLocationLiveData()
    }

    
    fun updateCurrentUserPosition(latLng: LatLng) {
        currentUserPosition.value = latLng
    }
}