package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.RealEstateDatabase.Companion.getInstance
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Estate.Companion.fromContentValues
import java.lang.IllegalArgumentException

class EstateContentProvider : ContentProvider() {

    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    private val TABLE_NAME: String = Estate::class.java.simpleName



    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        if (context != null) {
            val mandateNumberID = ContentUris.parseId(uri)
            val cursor = getInstance(context!!).estateDao().getEstateWithCursor(mandateNumberID)
            cursor!!.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row from uri$uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.estate/" + AUTHORITY + "." + TABLE_NAME
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (context != null) {
            val id = getInstance(context!!).estateDao().insertEstate(fromContentValues(values!!))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }
        throw IllegalArgumentException("Failed to insert row into$uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        if (context != null) {
            val count =
                getInstance(context!!).estateDao().deleteItem(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into$uri")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        if (context != null) {

            val count = getInstance(context!!).estateDao()
                .updateEstate(fromContentValues(values!!))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into$uri")
    }



}