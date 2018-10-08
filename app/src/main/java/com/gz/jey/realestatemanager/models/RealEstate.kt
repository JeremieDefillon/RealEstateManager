package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "RealEstate")
data class RealEstate(
        @PrimaryKey(autoGenerate = true)var id : Long?,
        var type : Int?,
        var price : Int?,
        var surface : Int?,
        var room : Int?,
        var bed : Int?,
        var bath : Int?,
        var kitchen : Int?,
        var description : String?,
        @NotNull @Ignore
        var photo : ArrayList<Photos>?,
        var address : String?,
        @NotNull @Ignore
        var pointsOfInterest : ArrayList<Int>?,
        var status : Int?,
        var marketDate : String?,
        var soldDate : String?,
        var agentId : String?
){
        constructor():this(null,null,null,null,null,null,null, null,"", null,"", null,null,"","","")
}