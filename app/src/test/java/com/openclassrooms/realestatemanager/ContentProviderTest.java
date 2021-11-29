package com.openclassrooms.realestatemanager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;


@RunWith(AndroidJUnit4.class)

public class ContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static final long USER_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }


    @Test
    public void insertAndGetItem() {
        Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        // BEFORE : Adding demo item
        mContentResolver.insert(EstateContentProvider.URI_ESTATE, generateItem());
        // TEST
        cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("estateType")), is("House"));
    }



    private ContentValues generateItem(){
            final ContentValues values = new ContentValues();
        values.put("estateType", "House");
        values.put("surface", 100);
        values.put("rooms", 3);
        values.put("bedrooms", 2);
        values.put("bathrooms",1);
        values.put("ground",700);
        values.put("price", 250000);
        values.put("description", "Very nice house in center");
        values.put("address", "8 quai vauban");
        values.put("zipCode", 66000);
        values.put("city", "perpignan");
        values.put("schools", false);
        values.put("stores", true);
        values.put("park", false);
        values.put("restaurants", true);
        values.put("upOfSaleDate", "01/12/2021");
        values.put("soldDate", "");
        values.put("agentName", "Karine Danjard");
        return values;
    }

}