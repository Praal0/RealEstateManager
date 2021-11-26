package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.openclassrooms.realestatemanager.database.RealEstateDatabase.Companion.getInstance
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Estate.Companion.fromContentValues
import kotlinx.coroutines.runBlocking


class EstateContentProvider : ContentProvider() {

    // FOR DATA
    var AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    var TABLE_NAME: String = Estate::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun onCreate(): Boolean { return true }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val cursor : Cursor
        runBlocking{
            val index:Long = ContentUris.parseId(uri)
            cursor = getInstance(context!!).estateDao().getEstateWithCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,uri)
            Log.e("Cursor","Cursor")
        }
        return cursor
    }

    override fun getType(uri: Uri): String { return "vnd.android.cursor.estate/$AUTHORITY.$TABLE_NAME"}

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (context != null && values != null){
            Log.e("EstateContentProvider","ContentValues : $values")
            val index = getInstance(context!!).estateDao().insertEstateTest((fromContentValues(values)))
            if (index != 0L){
                context!!.contentResolver.notifyChange(uri,null)
                return ContentUris.withAppendedId(uri,index)
            }
        }
        throw IllegalArgumentException("Failed to insert row into$uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count : Int
        if (context != null) {
            runBlocking {
                count = getInstance(context!!).estateDao().deleteItem(ContentUris.parseId(uri))
                context!!.contentResolver.notifyChange(uri, null)
            }
            return count
        }
        throw IllegalArgumentException("Failed to delete row into$uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val count : Int
        if (context != null) {
            runBlocking {
                count = getInstance(context!!).estateDao().updateEstate(fromContentValues(values!!))
                context!!.contentResolver.notifyChange(uri, null)
            }
            return count
        }
        throw IllegalArgumentException("Failed to update row into$uri")
    }

}