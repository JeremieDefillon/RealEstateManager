package com.gz.jey.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

class Location: AppCompatActivity(){

    private val TAG = "LOCATION"

    // FOR PERMISSIONS
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34
    var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient


    /**
     * GET DEVICE LOCATION
     */
    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        mFusedLocationProviderClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        Log.d(TAG, " TASK DEVICE LOCATION SUCCESS")
                        retLoc(LatLng(task.result!!.latitude, task.result!!.longitude))

                    } else {
                        Log.e("MAP LOCATION", "Exception: %s", task.exception)
                        // Prompt the user for permission.
                        getLocationPermission()
                    }
                }
    }

    private fun retLoc(loc: LatLng) : LatLng?{
        return loc
    }

    /**
     * GET LOCATION PERMISSION
     */
    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
}