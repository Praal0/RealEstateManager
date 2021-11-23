package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.os.Build
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class EstateContentProvderTest {

    //for data
    private lateinit var mContentResolver: ContentResolver

    //Data Set for test
    private val ESTATE_ID:Long = 1
    private val ESTATE_ID_2:Long = 9999


    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RealEstateDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted(){
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider().URI_ESTATE, ESTATE_ID_2),
            null,null,null,null)
        assertNotNull(cursor)
        cursor?.let { assertEquals(0, it.count) }
        cursor?.close()
    }


    @Test
    fun insertAndGetItem(){
        // ADDING DEMO ESTATE
        mContentResolver.insert(EstateContentProvider().URI_ESTATE, generateEstate())
        // TEST
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider().URI_ESTATE, ESTATE_ID),
            null,null,null,null)
        assertNotNull(cursor)
        if (cursor != null) {
            assertEquals(1,cursor.count)
            assertEquals(true,cursor.moveToFirst())
            assertEquals("House",cursor.getString(cursor.getColumnIndexOrThrow("estateType")))
        }

    }

    private fun generateEstate(): ContentValues {
        val values = ContentValues()
        values.put("estateType", "House")
        values.put("surface", 100)
        values.put("rooms", 3)
        values.put("bedrooms", 2)
        values.put("bathrooms", 1)
        values.put("ground", 700)
        values.put("price", 250000)
        values.put("description", "Very nice house in center")
        values.put("address", "8 quai vauban")
        values.put("zipCode", 66000)
        values.put("city", "perpignan")
        values.put("schools", false)
        values.put("stores", true)
        values.put("park", false)
        values.put("restaurants", true)
        values.put("upOfSaleDate", "01/10/2020")
        values.put("soldDate", "")
        values.put("agentName", "Karine Danjard")
        return values
    }
}