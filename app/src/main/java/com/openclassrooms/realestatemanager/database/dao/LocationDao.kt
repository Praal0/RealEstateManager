package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    fun getLocations(): LiveData<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationTest(location: Location)

    @Update
    suspend fun updateLocation(location: Location)

}