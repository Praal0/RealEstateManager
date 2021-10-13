package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location
import com.openclassrooms.realestatemanager.repositories.LocationDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel@Inject constructor (private val locationDataSource: LocationDataRepository): ViewModel() {

    // --------------------
    // LOCATION
    // --------------------

    fun getLocations() : LiveData<List<Location>> {
        return locationDataSource.getLocations()
    }

    fun insertLocation(location: Location) {
        viewModelScope.launch {
            locationDataSource.createLocation(location)
            delay(1000)
        }
    }

    fun updateLocation(location: Location){
        viewModelScope.launch {
            locationDataSource.updateLocation(location)
            delay(1000)
        }
    }

     fun deleteLocation(locationId : Long){
         viewModelScope.launch {
             locationDataSource.deleteLocation(locationId)
             delay(1000)
         }

    }

    fun getLocationById(locationId: Long) : LiveData<Location>{
        return locationDataSource.getLocationByID(locationId)
    }

}