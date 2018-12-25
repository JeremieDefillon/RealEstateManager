
package com.gz.jey.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.models.sql.RealEstate

/**
 * Created by Jey on 10/10/2018.
 */

class RealEstateContentProvider : ContentProvider() {

    /**
     * ON CREATE PROVIDER
     * @return true
     */
    override fun onCreate(): Boolean {
        return true
    }

    /**
     * TO GET CURSOR FROM QUERY
     * @param uri Uri
     * @param projection Array<String>?
     * @param selection String?
     * @param selectionArgs Array<String>?
     * @param sortOrder String?
     * @return Cursor
     */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        if (context != null) {
            val reId = ContentUris.parseId(uri)
            val cursor : Cursor = ItemDatabase.getInstance(context)!!.realEstateDao().getRealEstatesWithCursor(reId)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri") as Throwable
    }

    /**
     * TO GET TYPE
     * @param uri Uri
     * @return String
     */
    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    /**
     * TO INSERT REAL ELSTATE
     * @param uri Uri
     * @param contentValues ContentValues
     * @return Uri
     */
    override fun insert(uri: Uri, contentValues: ContentValues): Uri? {

        if (context != null) {
            val id = ItemDatabase.getInstance(context)!!.realEstateDao().insertRealEstate(RealEstate.fromContentValues(contentValues))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    /**
     * TO DELETE REAL ELSTATE
     * @param uri Uri
     * @param s String?
     * @param strings Array<String>?
     * @return Int
     */
    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val count = ItemDatabase.getInstance(context)!!.realEstateDao().deleteRealEstate(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    /**
     * TO UPDATE REAL ELSTATE
     * @param uri Uri
     * @param contentValues ContentValues
     * @param s String?
     * @param strings Array<String>?
     * @return Int
     */
    override fun update(uri: Uri, contentValues: ContentValues, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val count = ItemDatabase.getInstance(context)!!.realEstateDao().updateRealEstate(RealEstate.fromContentValues(contentValues))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }

    companion object {

        const val AUTHORITY = "com.gz.jey.realestatemanager.provider"
        val TABLE_NAME = RealEstate::class.java.simpleName!!
        val URI_REAL_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")!!
    }
}
