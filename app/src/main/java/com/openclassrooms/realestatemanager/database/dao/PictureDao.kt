package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.PhotoDescription

@Dao
interface PictureDao {

    @Query("SELECT * FROM PICTURE WHERE estateId = :estateId")
    fun getPicture(estateId:Long): LiveData<List<PhotoDescription>>

    @Insert
    fun insertPicture(photoDescription: PhotoDescription) : Long

    @Query("DELETE FROM PICTURE WHERE estateId = :estateId")
    fun deleteAllPictureFromEstate(estateId:Long): Int
}