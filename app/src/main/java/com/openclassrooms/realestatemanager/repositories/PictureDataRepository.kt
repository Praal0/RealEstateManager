package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.model.Picture
import javax.inject.Inject


class PictureDataRepository (private val pictureDao: PictureDao) {

    fun getImages(estateId:Long): LiveData<List<Picture>> {
        return this.pictureDao.getPicture(estateId)
    }


}