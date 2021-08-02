package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.FullEstate

@Dao
interface EstateDao {
    @Query("SELECT Estate.*,Location.* FROM Estate INNER JOIN Location ON Estate.id = Location.estateId ")
    fun getItems(): LiveData<List<FullEstate>>

    @Query("SELECT Estate.*,Location.* FROM Estate INNER JOIN Location ON Estate.id = Location.estateId  WHERE Estate.id = :index")
    fun getItemsWithCursor(index:Long): Cursor

    @RawQuery
    fun getItemsBySearch(query: SupportSQLiteQuery) : LiveData<List<FullEstate>>

    @Query("SELECT Estate.*,Location.* FROM Estate INNER JOIN Location ON Estate.id = Location.estateId  WHERE Estate.id = :index")
    fun getItemsByID(index:Long) : LiveData<FullEstate>

    @Insert
    fun insertItem(estate: Estate) : Long

    @Update
    fun updateItem(estate: Estate) :Int

}