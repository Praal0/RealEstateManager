package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.*
import com.openclassrooms.realestatemanager.database.converters.PhotoDescriptionConverter
import com.openclassrooms.realestatemanager.database.converters.UriListConverter
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location


@Database(entities = [(Estate::class)], version = 1, exportSchema = false)
@TypeConverters(UriListConverter::class, PhotoDescriptionConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun estateDao(): EstateDAO

    companion object {
        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): RealEstateDatabase {
            if (INSTANCE == null) {
                synchronized(RealEstateDatabase::class.java) {

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateDatabase::class.java, "RealEstateDatabase.db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }
    }
}