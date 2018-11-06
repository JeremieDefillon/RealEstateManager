package com.gz.jey.realestatemanager.api

import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.BuildConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiStreams {

    /**
     * RETROFIT BUILDER
     * @return Retrofit
     */
    private val retrofit: Retrofit
        get() {
            return Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

    /**
     * FETCH RESTAURANTS LIST REQUEST WITH GOOGLE PLACE
     * @param loc LatLng
     * @param lang Int
     * @return Observable<Place>
     */
    fun streamGeoCode(loc: LatLng): Observable<GeoCode> {
        val location = loc.latitude.toString()+","+loc.longitude.toString()
        //location = "45.750000,4.850000"
        val radius = "8000"
        val rankby = "distance"
        val language = if (Data.lang==1) "fr" else "en"
        val type = "restaurant"

        val apiService = this.retrofit.create(ApiService::class.java)

        return apiService.getRestaurants(type, location, radius, rankby, language, BuildConfig.GOOGLE_MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS)
    }

    /**
     * FETCH RESTAURANT DETAILS REQUEST WITH GOOGLE PLACE
     * @param id String
     * @param lang Int
     * @return Observable<Details>
     */
    fun getStaticMap(address : String, dimen :Int, pos :LatLng, key : String ) : String{
        val max = dimen.toString()
        val lat = pos.latitude
        val lng = pos.longitude

        val url = StringBuilder()
        url.append("https://maps.googleapis.com/maps/api/staticmap?")
        url.append("center=$address")
        url.append("&size=$max")
        url.append("&maptype=roadmap")
        url.append("&markers=color:blue%7Clabel:S%7C$lat,$lng")
        url.append("&key=$key")

        return url.toString()
    }
}