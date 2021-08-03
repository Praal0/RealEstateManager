package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.model.Image
import java.util.*

class ImageDataRepository (private val database: RealEstateDatabase) {

    fun getImages(estateId:Long): LiveData<List<Image>> {
        return this.database.imageDao().getItems(estateId)
    }


}