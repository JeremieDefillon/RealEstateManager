package com.gz.jey.realestatemanager.models

import com.gz.jey.realestatemanager.models.sql.Photos

data class TempRealEstate(
        var id: Long? = null,
        var streetNumber: String = "",
        var street: String = "",
        var zipCode: String = "",
        var locality: String = "",
        var state: String = "",
        var verified: Boolean = false,
        var latitude: Double? = null,
        var longitude: Double? = null,
        var type: Int? = null,
        var surface: Int? = null,
        var room: Int? = null,
        var bed: Int? = null,
        var bath: Int? = null,
        var kitchen: Int? = null,
        var description: String = "",
        var price: Long? = null,
        var sold: Boolean = false,
        var marketDate: String = "",
        var soldDate: String = "",
        var agentName: String = "",
        var poiSchool : Boolean = false,
        var poiShops : Boolean = false,
        var poiPark : Boolean = false,
        var poiSubway : Boolean = false,
        var poiBus : Boolean = false,
        var poiTrain : Boolean = false,
        var poiHospital : Boolean = false,
        var poiAirport : Boolean = false,
        var photos: List<Photos>? = null
)