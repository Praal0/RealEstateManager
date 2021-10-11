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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEstate(estate: Estate)

    @Update
    suspend fun updateEstate(estate: Estate): Int

    @Query("DELETE FROM Estate WHERE numMandat = :mandateNumberID")
    suspend fun deleteItem(mandateNumberID: Long): Int

    @RawQuery(observedEntities = [Estate::class])
    fun getSearchEstate(query: SupportSQLiteQuery): LiveData<List<Estate>>

    //For ContentProvider
    @Query("SELECT * FROM Estate WHERE numMandat = :mandateNumberID")
    fun getEstateWithCursor(mandateNumberID: Long): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstateProvider(estate: Estate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstateTest(estateHouse: Estate)

    @Update
    fun updateEstateProvider(estate: Estate): Int

    @Query("DELETE FROM Estate WHERE numMandat = :mandateNumberID")
    fun deleteItemProvider(mandateNumberID: Long): Int




}