package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.content.ContentValues
import com.gz.jey.realestatemanager.utils.PhotosConverter

@Entity(tableName = "RealEstate")
data class RealEstate(
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null,
        var streetNumber: String = "",
        var street: String = "",
        var zipCode: String = "",
        var locality: String = "",
        var state: String = "",
        var verified: Boolean = false,
        var latitude: Double? = null,
        var longitude: Double? = null,
        var type: Int? = null,
        var surface: Int? = null,
        var room: Int? = null,
        var bed: Int? = null,
        var bath: Int? = null,
        var kitchen: Int? = null,
        var description: String = "",
        var price: Long? = null,
        var sold: Boolean = false,
        var marketDate: String = "",
        var soldDate: String = "",
        var agentName: String = "",
        var poiSchool : Boolean = false,
        var poiShops : Boolean = false,
        var poiPark : Boolean = false,
        var poiSubway : Boolean = false,
        var poiBus : Boolean = false,
        var poiTrain : Boolean = false,
        var poiHospital : Boolean = false,
        var poiAirport : Boolean = false,
        @TypeConverters(PhotosConverter::class)
        var photos: List<Photos>? = null,
        var photoNum: Int = 0,
        var selected: Int = 0
) {
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): RealEstate {
            val re = RealEstate()
            if (values.containsKey("streetNumber")) re.streetNumber = values.getAsString("streetNumber")
            if (values.containsKey("street")) re.street = values.getAsString("street")
            if (values.containsKey("zipCode")) re.zipCode = values.getAsString("zipCode")
            if (values.containsKey("locality")) re.locality = values.getAsString("locality")
            if (values.containsKey("state")) re.state = values.getAsString("state")
            if (values.containsKey("latitude")) re.latitude = values.getAsDouble("latitude")
            if (values.containsKey("longitude")) re.longitude = values.getAsDouble("longitude")
            if (values.containsKey("type")) re.type = values.getAsInteger("type")
            if (values.containsKey("price")) re.price = values.getAsLong("price")
            if (values.containsKey("surface")) re.surface = values.getAsInteger("surface")
            if (values.containsKey("room")) re.room = values.getAsInteger("room")
            if (values.containsKey("bed")) re.bed = values.getAsInteger("bed")
            if (values.containsKey("bath")) re.bath = values.getAsInteger("bath")
            if (values.containsKey("kitchen")) re.kitchen = values.getAsInteger("kitchen")
            if (values.containsKey("description")) re.description = values.getAsString("description")
            if (values.containsKey("sold")) re.sold = values.getAsBoolean("sold")
            if (values.containsKey("marketDate")) re.marketDate = values.getAsString("marketDate")
            if (values.containsKey("soldDate")) re.soldDate = values.getAsString("soldDate")
            if (values.containsKey("agentName")) re.agentName = values.getAsString("agentName")
            if (values.containsKey("poiSchool")) re.poiSchool = values.getAsBoolean("poiSchool")
            if (values.containsKey("poiShops")) re.poiShops = values.getAsBoolean("poiShops")
            if (values.containsKey("poiPark")) re.poiPark = values.getAsBoolean("poiPark")
            if (values.containsKey("poiSubway")) re.poiSubway = values.getAsBoolean("poiSubway")
            if (values.containsKey("poiBus")) re.poiBus = values.getAsBoolean("poiBus")
            if (values.containsKey("poiTrain")) re.poiTrain = values.getAsBoolean("poiTrain")
            if (values.containsKey("poiHospital")) re.poiHospital = values.getAsBoolean("poiHospital")
            if (values.containsKey("poiAirport")) re.poiAirport = values.getAsBoolean("poiAirport")
            if (values.containsKey("photoNum")) re.photoNum = values.getAsInteger("photoNum")
            if (values.containsKey("selected")) re.selected = values.getAsInteger("selected")
            return re
        }
    }

    constructor() : this(null,
            "",
            "",
            "",
            "",
            "",
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "",
            null,
            false,
            "",
            "",
            "",
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null,
            0,
            0)

}