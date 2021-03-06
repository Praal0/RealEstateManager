package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.Estate

@Dao
interface EstateDAO {


    @Query("SELECT * FROM Estate")
    fun getEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM Estate WHERE numMandat = :mandateNumberID")
    fun getEstate(mandateNumberID: Long): LiveData<Estate>

    @RawQuery(observedEntities = [Estate::class])
    fun getSearchEstate(query: SupportSQLiteQuery): LiveData<List<Estate>>


    //For ContentProvider
    @Query("SELECT * FROM Estate  WHERE numMandat = :index")
    fun getEstateWithCursor(index:Long): Cursor

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEstate(estate: Estate) : Long

    @Update
    suspend fun updateEstate(estate: Estate): Int

    @Query("DELETE FROM Estate WHERE numMandat = :mandateNumberID")
    suspend fun deleteItem(mandateNumberID: Long): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertEstateTest(estateHouse: Estate) : Long
}