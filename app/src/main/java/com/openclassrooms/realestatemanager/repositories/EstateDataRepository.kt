package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


class EstateDataRepository @Inject constructor(
    private val estateDao: EstateDao
    ) {
    fun getEstates(): LiveData<List<Estate>> {
        return this.estateDao.getEstates()
    }

    fun gesEstateByID(estateID:Long) : LiveData<Estate>{
        return this.estateDao.getEstate(estateID)
    }

    // --- CREATE ---
    fun createEstate(estate: Estate, pictureList: ArrayList<Picture>) {
        estateDao.createEstate(estate, pictureList)
    }

    // --- UPDATE ---
    fun updateEstate(estate: Estate) {
        estateDao.updateEstate(estate)
    }

    fun updateEstateAndPictures(estate: Estate, pictureList: ArrayList<Picture>){
        estateDao.updateEstateAndPictures(estate, pictureList)
    }



}

