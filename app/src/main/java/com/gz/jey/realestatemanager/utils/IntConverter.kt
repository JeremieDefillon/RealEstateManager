package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class IntConverter {
    private val gson = Gson()

    /**
     * CONVERT DATA STRING TO LIST INT
     * @param data String
     * @return List<Int>
     */
    @TypeConverter
    fun stringToList(data: String?): List<Int>? {
        if (data == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, listType)
    }

    /**
     * CONVERT LIST INT TO STRING
     * @param someObjects List<Int>
     * @return String
     */
    @TypeConverter
    fun ListToString(someObjects: List<Int>?): String {
        return gson.toJson(someObjects)
    }
}