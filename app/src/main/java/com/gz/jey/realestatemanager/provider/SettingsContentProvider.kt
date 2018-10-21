
package com.gz.jey.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.models.Settings

/**
 * Created by Jey on 10/10/2018.
 */

class SettingsContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        if (context != null) {
            val cursor : Cursor = RealEstateManagerDatabase.getInstance(context)!!.settingsDao().getSettingsWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues): Uri? {

        if (context != null) {
            val id = RealEstateManagerDatabase.getInstance(context)!!.settingsDao().insertSettings(Settings.fromContentValues(contentValues))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val count = RealEstateManagerDatabase.getInstance(context)!!.settingsDao().deleteSettings()
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(uri: Uri, contentValues: ContentValues, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val count = RealEstateManagerDatabase.getInstance(context)!!.settingsDao().updateSettings(Settings.fromContentValues(contentValues))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }

    companion object {

        val AUTHORITY = "com.gz.jey.realestatemanager.provider"
        val TABLE_NAME = RealEstate::class.java.simpleName
        val URI_REAL_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }
}
