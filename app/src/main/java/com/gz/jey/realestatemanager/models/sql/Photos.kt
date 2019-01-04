package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "Photos")
data class Photos(
        @PrimaryKey(autoGenerate = true)
        var id: Long?,
        var reid: Long?,
        var image: String?,
        var legend: Int,
        var num: Int,
        var selected: Boolean = false
) : Parcelable {

    constructor() : this(null, null, null, 0, 0, false)

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(reid)
        parcel.writeString(image)
        parcel.writeInt(legend)
        parcel.writeInt(num)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photos> {
        override fun createFromParcel(parcel: Parcel): Photos {
            return Photos(parcel)
        }

        override fun newArray(size: Int): Array<Photos?> {
            return arrayOfNulls(size)
        }

        fun fromContentValues(values: ContentValues): Photos {
            val ph = Photos()
            if (values.containsKey("reid")) ph.reid = values.getAsLong("reid")
            if (values.containsKey("image")) ph.image = values.getAsString("image")
            if (values.containsKey("legend")) ph.legend = values.getAsInteger("legend")
            if (values.containsKey("num")) ph.num = values.getAsInteger("num")
            return ph
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Photos> = object : Parcelable.Creator<Photos> {
            override fun createFromParcel(source: Parcel): Photos = Photos(source)
            override fun newArray(size: Int): Array<Photos?> = arrayOfNulls(size)
        }
    }
}