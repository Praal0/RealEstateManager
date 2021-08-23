package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.database.dao.ImageDao
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Image
import com.openclassrooms.realestatemanager.model.Location

@Database(entities = [(Estate::class), (Image::class), (Location::class)],version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract fun estateDao(): EstateDao
    abstract fun imageDao(): ImageDao
    abstract fun locationDao(): LocationDao

    companion object {
        private var INSTANCE: RealEstateDatabase? = null

        fun getInstance(context: Context):RealEstateDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateDatabase::class.java,
                        "RealEstateDatabase.db")
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                            }
                        })
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }


        fun destroyInstance(){
            INSTANCE = null
        }



        val PREPOPULATE_DATA = listOf(Estate(1, "house", 100000.00, 50000, 2, 1, 2, "true",
            true, true, false, false, "false", null, null, "John Doe"))
    }
}