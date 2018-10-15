package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.gz.jey.realestatemanager.repositories.PhotosDataRepository
import com.gz.jey.realestatemanager.repositories.PointsOfInterestDataRepository
import com.gz.jey.realestatemanager.repositories.RealEstateDataRepository
import java.util.concurrent.Executor

class ViewModelFactory(private val itemDataSource: RealEstateDataRepository, private val photoDataSource: PhotosDataRepository, private val poiDataSource: PointsOfInterestDataRepository, private val executor: Executor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)) {
            return RealEstateViewModel(itemDataSource, photoDataSource, poiDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}