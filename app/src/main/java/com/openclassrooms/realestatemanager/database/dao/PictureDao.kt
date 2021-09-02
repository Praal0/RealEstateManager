package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.model.Picture

@Dao
interface PictureDao {

    @Query("SELECT * FROM PICTURE WHERE estateId = :estateId")
    fun getPicture(estateId:Long): LiveData<List<Picture>>

    @Insert
    fun insertPicture(picture: Picture) : Long

    @Query("DELETE FROM PICTURE WHERE estateId = :estateId")
    fun deleteAllPictureFromEstate(estateId:Long): Int
}