package com.openclassrooms.realestatemanager;

import static com.openclassrooms.realestatemanager.ContentProviderInstrumentedTest.URI_ITEM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static final long USER_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ITEM, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }
}