package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gz.jey.realestatemanager.models.sql.Photos
import java.util.*


class PhotosConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Photos> {
        if (data == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Photos>>() {}.type
        return gson.fromJson(data, listType)
    }
    

    @TypeConverter
    fun ListToString(someObjects: List<Photos>?): String? {
        if(someObjects==null)
            return null

        return gson.toJson(someObjects)
    }
}