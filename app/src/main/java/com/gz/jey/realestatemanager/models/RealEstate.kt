package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "RealEstate")
data class RealEstate(
        @PrimaryKey(autoGenerate = true)var id : Long?,
        @ColumnInfo(name="type")var type : String?,
        @ColumnInfo(name="price")var price : Int?,
        @ColumnInfo(name="surface")var surface : Int?,
        @ColumnInfo(name="room")var room : Int?,
        @ColumnInfo(name="bed")var bed : Int?,
        @ColumnInfo(name="bath")var bath : Int?,
        @ColumnInfo(name="description")var description : String?,
        //@ColumnInfo(name="photo")var photo : ArrayList<Photos>?,
        @ColumnInfo(name="address")var address : String?,
        @ColumnInfo(name="pointsOfInterest")var pointsOfInterest : String?,
        @ColumnInfo(name="status")var status : String?,
        @ColumnInfo(name="marketDate")var marketDate : String?,
        @ColumnInfo(name="soldDate")var soldDate : String?,
        @ColumnInfo(name="agentId")var agentId : String?
){
        constructor():this(null,"",0,0,0,0,0,"","", "","","","","")
}