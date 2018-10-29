package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.content.ContentValues
import com.gz.jey.realestatemanager.utils.PhotosConverter
import com.gz.jey.realestatemanager.utils.PointsOfInterestConverter


@Entity(tableName = "RealEstate")
data class RealEstate(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        var district: String? = null,
        var address: String? = null,
        var type: Int? = null,
        var surface: Int? = null,
        var room: Int? = null,
        var bed: Int? = null,
        var bath: Int? = null,
        var kitchen: Int? = null,
        var description: String? = null,
        var currency: Int? = null,
        var price: Long? = null,
        var status: Int? = null,
        var marketDate: String? = null,
        var soldDate: String? = null,
        var agentName: String? = null,
        var isSelected: Boolean = false,
        @TypeConverters(PhotosConverter::class)
        var photos: List<Photos>? = null,
        @TypeConverters(PointsOfInterestConverter::class)
        var poi: List<PointsOfInterest>? = null
) {
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): RealEstate {
            val re = RealEstate()
            if (values.containsKey("district")) re.district = values.getAsString("district")
            if (values.containsKey("type")) re.type = values.getAsInteger("type")
            if (values.containsKey("price")) re.price = values.getAsLong("price")
            if (values.containsKey("surface")) re.surface = values.getAsInteger("surface")
            if (values.containsKey("room")) re.room = values.getAsInteger("room")
            if (values.containsKey("bed")) re.bed = values.getAsInteger("bed")
            if (values.containsKey("bath")) re.bath = values.getAsInteger("bath")
            if (values.containsKey("kitchen")) re.kitchen = values.getAsInteger("kitchen")
            if (values.containsKey("description")) re.description = values.getAsString("description")
            if (values.containsKey("address")) re.address = values.getAsString("address")
            if (values.containsKey("status")) re.status = values.getAsInteger("status")
            if (values.containsKey("marketDate")) re.marketDate = values.getAsString("marketDate")
            if (values.containsKey("soldDate")) re.soldDate = values.getAsString("soldDate")
            if (values.containsKey("agentName")) re.agentName = values.getAsString("agentName")
            return re
        }
    }

    constructor() : this(null,
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "",
            0,
            null,
            0,
            "",
            "",
            "",
            false,
            null,
            null)

}