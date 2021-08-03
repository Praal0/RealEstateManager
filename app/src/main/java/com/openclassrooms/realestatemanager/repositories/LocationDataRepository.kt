package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.model.Image
import com.openclassrooms.realestatemanager.model.Location

class LocationDataRepository (private val database: RealEstateDatabase) {
    fun getLocation(estateId:Long): LiveData<List<Location>> {
        return this.database.locationDao().getItems(estateId)
    }
}