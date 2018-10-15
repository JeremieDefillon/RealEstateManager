package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.PointsOfInterest
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.repositories.PhotosDataRepository
import com.gz.jey.realestatemanager.repositories.PointsOfInterestDataRepository
import com.gz.jey.realestatemanager.repositories.RealEstateDataRepository
import java.util.concurrent.Executor

class RealEstateViewModel(// REPOSITORIES
        private val realEstateDataSource: RealEstateDataRepository, private val photosDataSource: PhotosDataRepository, private val poiDataSource: PointsOfInterestDataRepository, private val executor: Executor) : ViewModel() {

    // -------------
    // FOR REAL ESTATE
    // -------------

    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return realEstateDataSource.getAllRealEstate()
    }

    fun createRealEstate(realEstate: RealEstate) {
        executor.execute { realEstateDataSource.createRealEstate(realEstate) }
    }

    fun deleteRealEstate(realEstateId: Long) {
        executor.execute { realEstateDataSource.deleteRealEstate(realEstateId) }
    }

    fun updateRealEstate(realEstate: RealEstate) {
        executor.execute { realEstateDataSource.updateRealEstate(realEstate) }
    }

    // -------------
    // FOR POINTS OF INTEREST
    // -------------

    fun getAllPOI(realEstateId: Long): LiveData<List<PointsOfInterest>> {
        return poiDataSource.getAllPOI(realEstateId)
    }

    fun createPOI(poi: PointsOfInterest) {
        executor.execute { poiDataSource.createPOI(poi) }
    }

    fun deletePOI(realEstateId: Long) {
        executor.execute { poiDataSource.deletePOI(realEstateId) }
    }

    fun updatePOI(poi: PointsOfInterest) {
        executor.execute { poiDataSource.updatePOI(poi) }
    }

    // -------------
    // FOR PHOTOS
    // -------------

    fun getAllPhotos(reId: Long): LiveData<List<Photos>> {
        return photosDataSource.getAllPhotos(reId)
    }

    fun createPhotos(photos: Photos) {
        executor.execute { photosDataSource.createPhotos(photos) }
    }

    fun deletePhotos(reId: Long) {
        executor.execute { photosDataSource.deletePhotos(reId) }
    }

    fun updatePhotos(photos: Photos) {
        executor.execute { photosDataSource.updatePhotos(photos) }
    }
}