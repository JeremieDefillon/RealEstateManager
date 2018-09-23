package com.openclassrooms.realestatemanager.models

data class RealEstate(
        val type : String? = null,
        val price : String? = null,
        val surface : String? = null,
        val bed : String? = null,
        val description : String? = null,
        val photo : ArrayList<Photos>? = null,
        val address : String? = null,
        val pointsOfInterest : ArrayList<PointOfInterest>? = null,
        val status : String? = null,
        val marketDate : String? = null,
        val soldDate : String? = null,
        val agent : String? = null
)