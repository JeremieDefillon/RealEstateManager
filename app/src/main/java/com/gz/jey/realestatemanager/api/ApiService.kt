package com.gz.jey.realestatemanager.api

import com.gz.jey.realestatemanager.models.retrofit.GeoCode
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /**
     * REQUEST TO GET RESTAURANT AROUND LOCATION
     * @return Observable<Place>
     */
    @GET("maps/api/geocode/json?")
    fun getGeoCode(
            @Query("address") address: String,
            @Query("key") key: String
    ) : Observable<GeoCode>

}
