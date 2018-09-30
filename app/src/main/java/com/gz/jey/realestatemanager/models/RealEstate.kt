package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "RealEstate")
data class RealEstate(
        @PrimaryKey(autoGenerate = true)var id : Long?,
        var type : String?,
        var price : Int?,
        var surface : Int?,
        var room : Int?,
        var bed : Int?,
        var bath : Int?,
        var description : String?,
        @NotNull @Ignore
        var photo : ArrayList<Photos>?,
        var address : String?,
        var pointsOfInterest : String?,
        var status : String?,
        var marketDate : String?,
        var soldDate : String?,
        var agentId : String?
){
        constructor():this(null,"",0,0,0,0,0,"", null,"", "","","","","")
}