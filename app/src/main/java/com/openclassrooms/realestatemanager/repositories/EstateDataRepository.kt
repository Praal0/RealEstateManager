package com.openclassrooms.realestatemanager.repositories

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.FullEstate
import javax.inject.Inject


class EstateDataRepository @Inject constructor(private val estateDao: EstateDao)  {
    fun getEstates(): LiveData<List<FullEstate>> {
        return this.estateDao.getItems()
    }

    fun gesEstateByID(estateID:Long) : LiveData<FullEstate>{
        return this.estateDao.getItemsByID(estateID)
    }


}

