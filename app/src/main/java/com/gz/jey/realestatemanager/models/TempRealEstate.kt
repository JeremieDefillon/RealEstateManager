package com.gz.jey.realestatemanager.models

import android.os.Parcel
import android.os.Parcelable
import com.gz.jey.realestatemanager.models.sql.Photos

data class TempRealEstate(
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
        var poiSchool: Boolean = false,
        var poiShops: Boolean = false,
        var poiPark: Boolean = false,
        var poiSubway: Boolean = false,
        var poiBus: Boolean = false,
        var poiTrain: Boolean = false,
        var poiHospital: Boolean = false,
        var poiAirport: Boolean = false,
        var photos: List<Photos>? = null,
        var selected : Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            ArrayList<Photos>().apply { source.readList(this, Photos::class.java.classLoader)},
            source.readValue(Int::class.java.classLoader) as Int
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(streetNumber)
        writeString(street)
        writeString(zipCode)
        writeString(locality)
        writeString(state)
        writeInt((if (verified) 1 else 0))
        writeValue(latitude)
        writeValue(longitude)
        writeValue(type)
        writeValue(surface)
        writeValue(room)
        writeValue(bed)
        writeValue(bath)
        writeValue(kitchen)
        writeString(description)
        writeValue(price)
        writeInt((if (sold) 1 else 0))
        writeString(marketDate)
        writeString(soldDate)
        writeString(agentName)
        writeInt((if (poiSchool) 1 else 0))
        writeInt((if (poiShops) 1 else 0))
        writeInt((if (poiPark) 1 else 0))
        writeInt((if (poiSubway) 1 else 0))
        writeInt((if (poiBus) 1 else 0))
        writeInt((if (poiTrain) 1 else 0))
        writeInt((if (poiHospital) 1 else 0))
        writeInt((if (poiAirport) 1 else 0))
        writeList(photos)
        writeValue(selected)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TempRealEstate> = object : Parcelable.Creator<TempRealEstate> {
            override fun createFromParcel(source: Parcel): TempRealEstate = TempRealEstate(source)
            override fun newArray(size: Int): Array<TempRealEstate?> = arrayOfNulls(size)
        }
    }
}