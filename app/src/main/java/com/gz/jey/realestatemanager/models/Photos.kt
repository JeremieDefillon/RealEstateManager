package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
        entity = RealEstate::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("reId")
        )]
)
data class Photos(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        var image : String?,
        var legend : Int?,
        var reId : Long?
){
        constructor():this(null,null,null,null)
}