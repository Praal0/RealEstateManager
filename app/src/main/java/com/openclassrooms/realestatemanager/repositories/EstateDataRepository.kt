package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import javax.inject.Inject


class EstateDataRepository @Inject constructor(private val estateDao: EstateDao)  {
    fun getEstates(): LiveData<List<Estate>> {
        return this.estateDao.getEstates()
    }

    fun gesEstateByID(estateID:Long) : LiveData<Estate>{
        return this.estateDao.getEstate(estateID)
    }


}

