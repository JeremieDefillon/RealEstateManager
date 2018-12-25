package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gz.jey.realestatemanager.models.sql.Photos
import java.util.*


class PhotosConverter {
    private val gson = Gson()
    /**
     * CONVERT DATA STRING TO LIST PHOTOS
     * @param data String
     * @return List<Photos>
     */
    @TypeConverter
    fun stringToList(data: String?): List<Photos> {
        if (data == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Photos>>() {}.type
        return gson.fromJson(data, listType)
    }

    /**
     * CONVERT LIST PHOTOS TO STRING
     * @param someObjects List<Photos>
     * @return String
     */
    @TypeConverter
    fun ListToString(someObjects: List<Photos>?): String? {
        if(someObjects==null)
            return null

        return gson.toJson(someObjects)
    }
}