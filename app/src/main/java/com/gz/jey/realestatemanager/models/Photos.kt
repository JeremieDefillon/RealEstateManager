package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues

@Entity(foreignKeys = [ForeignKey(
        entity = RealEstate::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("reId")
)]
)
data class Photos(
        @PrimaryKey(autoGenerate = true)
        var id: Long?,
        var image: String?,
        var legend: Int?,
        var reId: Long?
) {
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): Photos {
            val ph = Photos()
            if (values.containsKey("image")) ph.image = values.getAsString("image")
            if (values.containsKey("legend")) ph.legend = values.getAsInteger("legend")
            if (values.containsKey("reId")) ph.reId = values.getAsLong("reId")
            return ph
        }
    }

    constructor() : this(null, null, null, null)
}