package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
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
        var address : String?,
        var status : Int?,
        var marketDate : String?,
        var soldDate : String?,
        var agentName : String?
){
        companion object {
                // --- UTILS ---
                fun fromContentValues(values: ContentValues): RealEstate {
                        val re = RealEstate()
                        if (values.containsKey("type")) re.type = values.getAsInteger("type")
                        if (values.containsKey("price")) re.price = values.getAsInteger("price")
                        if (values.containsKey("surface")) re.surface = values.getAsInteger("surface")
                        if (values.containsKey("room")) re.room = values.getAsInteger("room")
                        if (values.containsKey("bed")) re.bed = values.getAsInteger("bed")
                        if (values.containsKey("bath")) re.bath = values.getAsInteger("bath")
                        if (values.containsKey("kitchen")) re.kitchen = values.getAsInteger("kitchen")
                        if (values.containsKey("description")) re.description = values.getAsString("description")
                        if (values.containsKey("address")) re.address = values.getAsString("address")
                        if (values.containsKey("status")) re.status = values.getAsInteger("status")
                        if (values.containsKey("marketDate")) re.marketDate = values.getAsString("marketDate")
                        if (values.containsKey("soldDate")) re.soldDate = values.getAsString("soldDate")
                        if (values.containsKey("agentName")) re.agentName = values.getAsString("agentName")
                        return re
                }
        }
        constructor():this(null,null,null,null,null,null,null, null,"", "", null,"","","")
}