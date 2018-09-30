package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.ColumnInfo
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
        var path : String?,
        var desc : String?,
        var reId : Long?
){
        constructor():this(0,"","",0)
}