package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location

@Database(entities = [(Estate::class), (Location::class)],version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun estateDao(): EstateDAO
    abstract fun locationDao(): LocationDao

    companion object {
        // --- SINGLETON ---
        @Volatile
        var INSTANCE: RealEstateDatabase? = null

        // --- INSTANCE ---
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
                                contentValues.put("id",ESTATE_HOUSE.id)
                                contentValues.put("estateType",ESTATE_HOUSE.estateType)
                                contentValues.put("surface",ESTATE_HOUSE.surface)
                                contentValues.put("rooms",ESTATE_HOUSE.rooms)
                                contentValues.put("bedrooms",ESTATE_HOUSE.bedrooms)
                                contentValues.put("bathrooms",ESTATE_HOUSE.bathrooms)
                                contentValues.put("ground",ESTATE_HOUSE.ground)
                                contentValues.put("price",ESTATE_HOUSE.price)
                                contentValues.put("description", ESTATE_HOUSE.description)
                                contentValues.put("address", ESTATE_HOUSE.address)
                                contentValues.put("postalCode",ESTATE_HOUSE.postalCode)
                                contentValues.put("city", ESTATE_HOUSE.city)
                                contentValues.put("schools", ESTATE_HOUSE.schools)
                                contentValues.put("stores",ESTATE_HOUSE.stores)
                                contentValues.put("park",ESTATE_HOUSE.park)
                                contentValues.put("restaurants",ESTATE_HOUSE.restaurants)
                                contentValues.put("sold",ESTATE_HOUSE.sold)
                                contentValues.put("upOfSaleDate",ESTATE_HOUSE.upOfSaleDate)
                                contentValues.put("soldDate",ESTATE_HOUSE.soldDate)
                                contentValues.put("agentName",ESTATE_HOUSE.agentName)
                                db.insert("estate", OnConflictStrategy.IGNORE,contentValues) }
                        })
                        .build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }

    }
}

val ESTATE_HOUSE = Estate(
    100,
    "house",
    200,
    4,
    2,
    1,
    200,
    100000.00,
    "Très belle maison",
    "2 rue du Pont",
    66000,
    "Perpignan",
    true,
    false,
    false,
    true,
    true,
    1601510400000L,
    "",
    "Karine Danjard"
)

private val ESTATE_FLAT = Estate(
    2,
    "flat",
    80,
    2,
    1,
    1,
    0,
    50000.00,
    "Very nice flat",
    "5 rue longue",
    66000,
    "Perpignan",
    false,
    true,
    true,
    true,
    true,
    1601510400000L,
    "",
    "John Doe"
)

val estateList : List<Estate> = listOf(ESTATE_HOUSE,ESTATE_FLAT)