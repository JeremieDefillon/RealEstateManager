package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.content.ContentValues
import com.gz.jey.realestatemanager.utils.IntConverter

@Entity(tableName = "Filters")
data class Filters(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @TypeConverters(IntConverter::class)
        var type : List<Int>?,
        @TypeConverters(IntConverter::class)
        var poi : List<Int>?,
        var minRoom : Int?,
        var maxRoom : Int?,
        var locality : String?,
        var distance : Int?,
        var status : Int?,
        var date : String?,
        var minPrice : Long?,
        var maxPrice : Long?,
        var minSurface : Int?,
        var maxSurface : Int?,
        var minPhoto : Int?
){
  companion object {
    // --- UTILS ---
    fun fromContentValues(values: ContentValues): Filters {
      val filters = Filters()
      if (values.containsKey("minRoom")) filters.minRoom = values.getAsInteger("minRoom")
      if (values.containsKey("maxRoom")) filters.maxRoom = values.getAsInteger("maxRoom")
      if (values.containsKey("locality")) filters.locality = values.getAsString("locality")
      if (values.containsKey("distance")) filters.distance = values.getAsInteger("distance")
      if (values.containsKey("status")) filters.status = values.getAsInteger("status")
      if (values.containsKey("date")) filters.date = values.getAsString("date")
      if (values.containsKey("minPrice")) filters.minPrice = values.getAsLong("minPrice")
      if (values.containsKey("maxPrice")) filters.maxPrice = values.getAsLong("maxPrice")
      if (values.containsKey("minSurface")) filters.minSurface = values.getAsInteger("minSurface")
      if (values.containsKey("maxSurface")) filters.maxSurface = values.getAsInteger("maxSurface")
      if (values.containsKey("minPhoto")) filters.minPhoto = values.getAsInteger("minPhoto")
      return filters
    }
  }
  constructor():this(null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null)
}