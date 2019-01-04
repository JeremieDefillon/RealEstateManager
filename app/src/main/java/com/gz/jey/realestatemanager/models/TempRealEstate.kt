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
        var mainPhoto: Photos? = null,
        var selected : Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(Photos.CREATOR),
            parcel.readParcelable(Photos::class.java.classLoader),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(streetNumber)
        parcel.writeString(street)
        parcel.writeString(zipCode)
        parcel.writeString(locality)
        parcel.writeString(state)
        parcel.writeByte(if (verified) 1 else 0)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeValue(type)
        parcel.writeValue(surface)
        parcel.writeValue(room)
        parcel.writeValue(bed)
        parcel.writeValue(bath)
        parcel.writeValue(kitchen)
        parcel.writeString(description)
        parcel.writeValue(price)
        parcel.writeByte(if (sold) 1 else 0)
        parcel.writeString(marketDate)
        parcel.writeString(soldDate)
        parcel.writeString(agentName)
        parcel.writeByte(if (poiSchool) 1 else 0)
        parcel.writeByte(if (poiShops) 1 else 0)
        parcel.writeByte(if (poiPark) 1 else 0)
        parcel.writeByte(if (poiSubway) 1 else 0)
        parcel.writeByte(if (poiBus) 1 else 0)
        parcel.writeByte(if (poiTrain) 1 else 0)
        parcel.writeByte(if (poiHospital) 1 else 0)
        parcel.writeByte(if (poiAirport) 1 else 0)
        parcel.writeTypedList(photos)
        parcel.writeParcelable(mainPhoto, flags)
        parcel.writeInt(selected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TempRealEstate> {
        override fun createFromParcel(parcel: Parcel): TempRealEstate {
            return TempRealEstate(parcel)
        }

        override fun newArray(size: Int): Array<TempRealEstate?> {
            return arrayOfNulls(size)
        }
    }
}