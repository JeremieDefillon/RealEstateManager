package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.drawable.Drawable

@Entity(foreignKeys = [ForeignKey(
        entity = RealEstate::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("reId")
        )]
)
data class Photos(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @Ignore
        var image : ByteArray?,
        var desc : Int?,
        var reId : Long?
){
        constructor():this(null,null,null,null)
}