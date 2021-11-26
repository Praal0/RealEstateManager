package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.RealEstateDatabase.Companion.getInstance
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Estate.Companion.fromContentValues
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class EstateContentProvider : ContentProvider() {

    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    val TABLE_NAME: String = "estate"
    val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun onCreate(): Boolean { return true }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        if (context != null) {
            val cursor : Cursor
            val index:Long = ContentUris.parseId(uri)
            cursor = getInstance(context!!).estateDao().getEstateWithCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,uri)
            Log.e("Cursor","Cursor")
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor

        }

        throw java.lang.IllegalArgumentException("Failed to query row for uri $uri")
    }
    
    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME}

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (context != null && values != null){
            Log.e("EstateContentProvider","ContentValues : $values")
            val index = getInstance(context!!).estateDao().insertEstate((fromContentValues(values)))
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