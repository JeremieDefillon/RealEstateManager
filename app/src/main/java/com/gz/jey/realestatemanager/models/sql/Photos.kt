package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues

@Entity(tableName = "Photos")
data class Photos(
        @PrimaryKey(autoGenerate = true)
        var id: Long?,
        var image: String?,
        var legend: Int?,
        var num: Int?,
        var main: Boolean = false
) {
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): Photos {
            val ph = Photos()
            if (values.containsKey("image")) ph.image = values.getAsString("image")
            if (values.containsKey("legend")) ph.legend = values.getAsInteger("legend")
            if (values.containsKey("num")) ph.num = values.getAsInteger("num")
            if (values.containsKey("main")) ph.main = values.getAsBoolean("main")
            return ph
        }
    }

    constructor() : this(null, null, null, null, false)
}