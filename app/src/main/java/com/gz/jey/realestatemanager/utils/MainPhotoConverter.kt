package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gz.jey.realestatemanager.models.sql.Photos
import java.util.*


class MainPhotoConverter {
    private val gson = Gson()
    /**
     * CONVERT DATA STRING TO LIST PHOTOS
     * @param data String
     * @return Photos
     */
    @TypeConverter
    fun stringToList(data: String?): Photos? {
        if (data == null)
            return null

        val listType = object : TypeToken<Photos>() {}.type
        return gson.fromJson(data, listType)
    }

    /**
     * CONVERT LIST PHOTOS TO STRING
     * @param someObjects Photos
     * @return String
     */
    @TypeConverter
    fun ListToString(someObjects: Photos?): String? {
        if(someObjects==null)
            return null

        return gson.toJson(someObjects)
    }
}