package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Location
import javax.inject.Inject


class LocationDataRepository@Inject constructor(
    private val locationDao: LocationDao
    ){
    private var  id : Long = 0

    fun getLocations(): LiveData<List<Location>> {
        return this.locationDao.getLocations()
    }

    /**
     * Create
     * @param location
     */
    // --- CREATE ---
     suspend fun createLocation(location: Location)  : Long {
        try {
            id = locationDao.insertLocation(location)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("LocationDataRepository","Cannot Insert : $cause")
        }
        return id
    }

    /**
     * Update
     * @param estate
     */
    // --- UPDATE ---
    suspend fun updateLocation(location: Location) {
        try {
            locationDao.updateLocation(location)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("LocationDataRepository","Cannot Update : $cause")
        }
    }





}