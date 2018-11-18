package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.db.SupportSQLiteQuery
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.models.sql.Settings
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor

class ItemViewModel(// REPOSITORIES
        private val realEstateDataSource: RealEstateDataRepository,
        private val photosDataSource: PhotosDataRepository,
        private val settingsDataSource: SettingsDataRepository,
        private val filtersDataSource: FiltersDataRepository,
        private val executor: Executor
) : ViewModel() {

    // -------------
    // FOR REAL ESTATE
    // -------------

    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return realEstateDataSource.getAllRealEstate()
    }

    fun getFilteredRealEstate(req : SupportSQLiteQuery): LiveData<List<RealEstate>> {
        return realEstateDataSource.getFilteredRealEstate(req)
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

    fun updateAllRealEstates(realEstates: List<RealEstate>) {
        executor.execute { realEstateDataSource.updateAllRealEstates(realEstates) }
    }

    // -------------
    // FOR SETTINGS
    // -------------

    fun getSettings(id : Long) : LiveData<Settings> {
        return settingsDataSource.getSettings(id)
    }

    fun createSettings(settings: Settings) {
        executor.execute {settingsDataSource.createSettings(settings) }
    }

    fun updateSettings(settings: Settings) {
        executor.execute {settingsDataSource.updateSettings(settings) }
    }


    // -------------
    // FOR FILTERS
    // -------------

    fun getFilters(id : Long) : LiveData<Filters> {
        return filtersDataSource.getFilters(id)
    }

    fun createFilters(filters: Filters) {
        executor.execute {filtersDataSource.createFilters(filters) }
    }

    fun updateFilters(filters: Filters) {
        executor.execute {filtersDataSource.updateFilters(filters) }
    }
}