package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.converters.PhotoDescriptionConverter
import com.openclassrooms.realestatemanager.database.converters.UriListConverter
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location


private var INSTANCE: RealEstateDatabase? = null

@Database(entities = [(Estate::class), (Location::class)],version = 1, exportSchema = false)
@TypeConverters(UriListConverter::class, PhotoDescriptionConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract fun estateDao(): EstateDAO
    abstract fun locationDao(): LocationDao

    companion object {
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
                                db.insert("estates", OnConflictStrategy.IGNORE,contentValues) }
                        })
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }

    }
}