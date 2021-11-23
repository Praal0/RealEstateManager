package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import androidx.room.Room
import androidx.room.RoomMasterTable.TABLE_NAME
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.IsNull
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class EstateContentProvderTest {

    //for data
    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    private var mContentResolver: ContentResolver? = null
    var URI_ESTATE: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    //Data Set for test
    private val mandateNumberID: Long = 1

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RealEstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getEstateWhenNoEstateInserted() {
        val cursor = mContentResolver?.query(ContentUris.withAppendedId(URI_ESTATE, mandateNumberID),
            null, null, null, null);
        Assert.assertEquals(cursor, null)
        cursor?.count?.let { Assert.assertEquals(it.toLong(), 0) }
        cursor?.close()
    }

    @Test
    fun insertAndGetEstate() {
        //Before : Adding demo estate
        val estateUri = mContentResolver?.insert(URI_ESTATE, generateEstate())
        //test
        val cursor = mContentResolver?.query(
            ContentUris.withAppendedId(
                URI_ESTATE, mandateNumberID
            ),
            null, null, null, null
        )
        Assert.assertEquals(cursor, IsNull.notNullValue())
        Assert.assertEquals(cursor!!.count.toLong(), 1)
        Assert.assertEquals(cursor.moveToFirst(), true)
        Assert.assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("estateType")), "House")
        estateUri?.let { mContentResolver!!.delete(it, null, null) }
    }

    private fun generateEstate(): ContentValues? {
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