package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.models.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location WHERE estateId = :index")
    fun getItems(index:Long): LiveData<List<Location>>

    @Query("SELECT * FROM Location WHERE estateId = :index")
    fun getItemId(index:Long): Long

    @Insert
    fun insertItem(location: Location) : Long

    @Update
    fun updateItem(location: Location)
}