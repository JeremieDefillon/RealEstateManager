package com.gz.jey.realestatemanager.models.retrofit

data class Result (
        var addressComponents: List<AddressComponent>? = null,
        var formattedAddress: String? = null,
        val geometry : Geometry? = null,
        val placeId : String? = null,
        val plusCode : PlusCode? = null,
        val types : List<String>? = null
)