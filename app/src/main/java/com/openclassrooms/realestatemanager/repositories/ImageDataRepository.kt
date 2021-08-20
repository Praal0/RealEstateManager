package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.dao.ImageDao
import com.openclassrooms.realestatemanager.model.Image
import java.util.*
import javax.inject.Inject

class ImageDataRepository @Inject constructor(private val imageDao: ImageDao) {

    fun getImages(estateId:Long): LiveData<List<Image>> {
        return this.imageDao.getItems(estateId)
    }


}