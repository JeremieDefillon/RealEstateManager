package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.db.SupportSQLiteQuery
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor

class ItemViewModel(// REPOSITORIES
        private val realEstateDataSource: RealEstateDataRepository,
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