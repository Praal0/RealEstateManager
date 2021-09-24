package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.openclassrooms.realestatemanager.database.converters.PhotoDescriptionConverter
import com.openclassrooms.realestatemanager.database.converters.UriListConverter
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location


@Database(entities = [Estate::class, Location::class], version = 1, exportSchema = false)
@TypeConverters(UriListConverter::class, PhotoDescriptionConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {
    // --- DAO ---
    abstract fun estateDao(): EstateDAO
    abstract fun locationDao() : LocationDao

    companion object {
        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): RealEstateDatabase {
            if (INSTANCE == null) {
                synchronized(RealEstateDatabase::class.java) {

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateDatabase::class.java, "Estate.db")
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }
    }


}