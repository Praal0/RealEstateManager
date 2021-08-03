package com.openclassrooms.realestatemanager.repositories

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.FullEstate

class EstateDataRepository (private val database: RealEstateDatabase) {
    fun getEstates(): LiveData<List<FullEstate>> {
        return this.database.estateDao().getItems()
    }

    fun gesEstateByID(estateID:Long) : LiveData<FullEstate>{
        return this.database.estateDao().getItemsByID(estateID)
    }

    // --- CREATE ---

}

