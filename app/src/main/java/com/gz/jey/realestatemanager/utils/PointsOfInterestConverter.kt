package com.gz.jey.realestatemanager.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gz.jey.realestatemanager.models.sql.PointsOfInterest
import java.util.*


class PointsOfInterestConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<PointsOfInterest>? {
        if (data == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<PointsOfInterest>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<PointsOfInterest>?): String {
        return gson.toJson(someObjects)
    }
}