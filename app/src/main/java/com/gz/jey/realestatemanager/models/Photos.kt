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
        @ColumnInfo(name="id")var id : Long?,
        @ColumnInfo(name="path")var path : String?,
        @ColumnInfo(name="desc")var desc : String?,
        @ColumnInfo(name="reId")var reId : Long?
){
        constructor():this(0,"","",0)
}