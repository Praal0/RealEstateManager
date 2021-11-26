package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.Estate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ContentProviderInstrumentedTest {

    private ContentResolver mContentResolver;
    private static long AGENT_ID = 1;
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = "estate";
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);


    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }

    private void logAgent(Cursor cursor) {
        if (cursor != null) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            Log.d("logAgent", "getAgent.id: " + id + " name = " + name);
        }
    }


    private void logCursor(Cursor cursor){
        // for debug
        if (cursor == null) {
            Log.d("Cursor", "logCursor() called with: cursor = [" + cursor + "]");
        } else {
            switch (cursor.getCount()) {
                case 0:
                    Log.d("Cursor", "logCursor() called with: cursor count = 0");
                    return;
                case 1:
                    Log.d("Cursor", "logCursor() called with: cursor count = 1");
                    cursor.moveToFirst();
                    logAgent(cursor);
                    return;
                default:
                    Log.d("Cursor", "logCursor() called with: cursor count = " + cursor.getCount());
                    cursor.moveToFirst();
                    logAgent(cursor);
                    while (cursor.moveToNext()) {
                        logAgent(cursor);
                    }
                    return;
            }
        }
    }



    @Test
    public void getAgentById(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(URI_ITEM, AGENT_ID),
                null, null, null, null);
        assertNotNull(cursor);
    }

}
