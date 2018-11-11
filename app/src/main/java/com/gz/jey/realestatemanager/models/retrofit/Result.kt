package com.gz.jey.realestatemanager.models.retrofit

data class Result (
        val address_components: List<AddressComponent>? = null,
        val formatted_address: String? = null,
        val geometry : Geometry? = null,
        val place_id : String? = null,
        val plus_code : PlusCode? = null,
        val types : List<String>? = null
)