package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class IntConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Int>? {
        if (data == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<Int>?): String {
        return gson.toJson(someObjects)
    }
}