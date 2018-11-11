package com.gz.jey.realestatemanager.api

import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.BuildConfig
import com.gz.jey.realestatemanager.models.retrofit.GeoCode
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
    fun streamGeoCode(address: String): Observable<GeoCode> {
        val apiService = this.retrofit.create(ApiService::class.java)
        return apiService.getGeoCode(address, BuildConfig.GEOCODE_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(200, TimeUnit.SECONDS)
    }

    /**
     * FETCH RESTAURANT DETAILS REQUEST WITH GOOGLE PLACE
     * @param id String
     * @param lang Int
     * @return Observable<Details>
     */
    fun getStaticMap(address : String, dimen :Int, pos :LatLng) : String{
        val max = dimen.toString()+"x"+dimen.toString()
        val lat = pos.latitude
        val lng = pos.longitude
        val key = BuildConfig.GEOCODE_KEY

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