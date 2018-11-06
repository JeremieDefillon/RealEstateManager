package com.gz.jey.realestatemanager.models.retrofit

data class GeoCode (
        var results: List<Result>? = null,
        var status: String? = null
)