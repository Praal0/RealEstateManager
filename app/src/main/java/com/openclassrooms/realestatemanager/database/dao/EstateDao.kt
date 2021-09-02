package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture

@Dao
abstract class EstateDao(private val database: RealEstateDatabase) {
    @Query("SELECT * FROM Estate")
    abstract fun getEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM Estate WHERE id = :mandateNumberID")
    abstract fun getEstate(mandateNumberID: Long): LiveData<Estate>

    @Insert
    abstract fun insertEstate(estate: Estate): Long

    @Update
    abstract fun updateEstate(estate: Estate): Int


    @Transaction
    open fun createEstate(estate: Estate, pictureList: ArrayList<Picture>): Long{
        val id = this.insertEstate(estate)
        for (picture in pictureList){
            database.pictureDao().insertPicture(picture.apply { estateId = id })
        }
        return id
    }

    @Transaction
    open fun updateEstateAndPictures(estate: Estate, pictureList: ArrayList<Picture>): Int{
        val count = this.updateEstate(estate)
        val id = estate.id
        database.pictureDao().deleteAllPictureFromEstate(id)
        for (picture in pictureList){
            database.pictureDao().insertPicture(picture.apply { estateId = id })
        }
        return count
    }

    @Query("DELETE FROM Estate WHERE id = :mandateNumberID")
    abstract fun deleteItem(mandateNumberID: Long): Int

    @RawQuery(observedEntities = [Estate::class])
    abstract fun getSearchEstate(query: SupportSQLiteQuery): LiveData<List<Estate>>

    //For ContentProvider
    @Query("SELECT * FROM Estate WHERE id = :mandateNumberID")
    abstract fun getEstateWithCursor(mandateNumberID: Long): Cursor

}