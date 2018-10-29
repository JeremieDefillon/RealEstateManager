package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.PointsOfInterest
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.models.Settings
import com.gz.jey.realestatemanager.repositories.PhotosDataRepository
import com.gz.jey.realestatemanager.repositories.PointsOfInterestDataRepository
import com.gz.jey.realestatemanager.repositories.RealEstateDataRepository
import com.gz.jey.realestatemanager.repositories.SettingsDataRepository
import java.util.concurrent.Executor

class RealEstateViewModel(// REPOSITORIES
        private val realEstateDataSource: RealEstateDataRepository,
        private val photosDataSource: PhotosDataRepository,
        private val poiDataSource: PointsOfInterestDataRepository,
        private val settingsDataSource: SettingsDataRepository,
        private val executor: Executor
) : ViewModel() {

    // -------------
    // FOR REAL ESTATE
    // -------------

    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return realEstateDataSource.getAllRealEstate()
    }

    fun getRealEstateBySelect(): LiveData<RealEstate>{
        return realEstateDataSource.getRealEstateBySelect()
    }

    fun getRealEstate(id : Long): LiveData<RealEstate> {
        return realEstateDataSource.getRealEstate(id)
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
    // FOR SETTINGS
    // -------------

    fun getSettings() : LiveData<Settings> {
        return settingsDataSource.getSettings()
    }

    fun createSettings(settings: Settings) {
        executor.execute {settingsDataSource.createSettings(settings) }
    }

    fun updateSettings(settings: Settings) {
        executor.execute {settingsDataSource.updateSettings(settings) }
    }
}