package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    fun getLocations(): LiveData<List<Location>>

    @Query("SELECT * FROM Location WHERE estateId = :index")
    fun getLocationById(index:Long): LiveData<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location) : Long

    @Update
    suspend fun updateLocation(location: Location)

    @Query("DELETE FROM Location WHERE estateId = :mandateNumberID")
    fun deleteLocation(mandateNumberID: Long): Int
}