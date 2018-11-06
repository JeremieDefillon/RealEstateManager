package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues

@Entity(tableName = "PointsOfInterest")
data class PointsOfInterest(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        var value : Int?
){
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): PointsOfInterest {
            val poi = PointsOfInterest()
            if (values.containsKey("value")) poi.value = values.getAsInteger("value")
            return poi
        }
    }
    constructor():this(null,null)
}