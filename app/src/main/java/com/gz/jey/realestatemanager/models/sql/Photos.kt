package com.gz.jey.realestatemanager.models.sql

import android.arch.persistence.room.Entity
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
        var main: Boolean = false,
        var selected: Boolean = false
) : Parcelable {
    constructor() : this(null, null, null, 0, 0, false)

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readInt(),
            source.readInt(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(image)
        writeInt(legend)
        writeInt(num)
        writeInt((if (main) 1 else 0))
        writeInt((if (selected) 1 else 0))
    }

    companion object { // --- UTILS ---
        fun fromContentValues(values: ContentValues): Photos {
            val ph = Photos()
            if (values.containsKey("reid")) ph.reid = values.getAsLong("reid")
            if (values.containsKey("image")) ph.image = values.getAsString("image")
            if (values.containsKey("legend")) ph.legend = values.getAsInteger("legend")
            if (values.containsKey("num")) ph.num = values.getAsInteger("num")
            if (values.containsKey("main")) ph.main = values.getAsBoolean("main")
            return ph
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Photos> = object : Parcelable.Creator<Photos> {
            override fun createFromParcel(source: Parcel): Photos = Photos(source)
            override fun newArray(size: Int): Array<Photos?> = arrayOfNulls(size)
        }
    }
}