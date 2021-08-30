package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
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
        @Volatile
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
                                val contentValues = ContentValues()
                                contentValues.put("id",1)
                                contentValues.put("estateType","House")
                                contentValues.put("surface",3000)
                                contentValues.put("roomNumber",3)
                                contentValues.put("bathroomNumber",3)
                                contentValues.put("bedroomNumber",4)
                                contentValues.put("desc","Affichage du détails")
                                contentValues.put("address","11 avenu du sent")
                                contentValues.put("postalCode",69000)
                                contentValues.put("city","Lyon")
                                contentValues.put("parks",true)
                                contentValues.put("shops",true)
                                contentValues.put("schools",true)
                                contentValues.put("highway",false)
                                contentValues.put("estateStatute","Ace")
                                contentValues.put("estateAgent","Luc Dewit")
                                db.insert("estates", OnConflictStrategy.IGNORE,contentValues)                          }
                        })
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }

    }
}