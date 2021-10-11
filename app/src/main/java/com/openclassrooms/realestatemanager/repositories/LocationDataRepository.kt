package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location
import javax.inject.Inject


class LocationDataRepository@Inject constructor(
    private val locationDao: LocationDao
    ){

    fun getLocations(): LiveData<List<Location>> {
        return this.locationDao.getLocations()
    }

    fun getLocationByID(locationId:Long) : LiveData<Location>{
        return this.locationDao.getLocationById(locationId)
    }

    /**
     * Create
     *
     * @param location
     */
    // --- CREATE ---
     suspend fun createLocation(location: Location) {
        try {
            locationDao.insertLocation(location)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("LocationDataRepository","Cannot Insert")
        }
    }

    /**
     * Update
     *
     * @param estate
     */
    // --- UPDATE ---
    suspend fun updateLocation(location: Location) {
        try {
            locationDao.updateLocation(location)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("LocationDataRepository","Cannot Update")
        }
    }


    /**
     * Delete
     *
     * @param estate
     */
    // --- UPDATE ---
    fun deleteLocation(estateId : Long) {
        locationDao.deleteLocation(estateId)
    }


}