package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Location


class LocationDataRepository(private val locationDao: LocationDao) {
    fun getLocation(estateId:Long): LiveData<List<Location>> {
        return this.locationDao.getItems(estateId)
    }
}