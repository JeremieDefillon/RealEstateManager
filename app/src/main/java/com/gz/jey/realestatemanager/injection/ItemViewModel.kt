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
    /**
     * TO GET ALL REAL ESTATE
     * @return LiveData<List<RealEstate>>
     */
    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return realEstateDataSource.getAllRealEstate()
    }

    /**
     * TO GET FILTERED REAL ESTATE
     * @param req SupportSQLiteQuery
     * @return LiveData<List<RealEstate>>
     */
    fun getFilteredRealEstate(req : SupportSQLiteQuery): LiveData<List<RealEstate>> {
        return realEstateDataSource.getFilteredRealEstate(req)
    }

    /**
     * TO GET REAL ESTATE
     * @param id Long
     * @return LiveData<RealEstate>
     */
    fun getRealEstate(id : Long): LiveData<RealEstate> {
        return realEstateDataSource.getRealEstate(id)
    }

    /**
     * TO CREATE REAL ESTATE
     * @param realEstate RealEstate
     */
    fun createRealEstate(realEstate: RealEstate) {
        executor.execute { realEstateDataSource.createRealEstate(realEstate) }
    }

    /**
     * TO DELETE REAL ESTATE
     * @param realEstateId Long
     */
    fun deleteRealEstate(realEstateId: Long) {
        executor.execute { realEstateDataSource.deleteRealEstate(realEstateId) }
    }

    /**
     * TO UPDATE REAL ESTATE
     * @param realEstate RealEstate
     */
    fun updateRealEstate(realEstate: RealEstate) {
        executor.execute { realEstateDataSource.updateRealEstate(realEstate) }
    }

    // -------------
    // FOR FILTERS
    // -------------
    /**
     * TO GET FILTERS
     * @param id Long
     * @return LiveData<Filters>
     */
    fun getFilters(id : Long) : LiveData<Filters> {
        return filtersDataSource.getFilters(id)
    }

    /**
     * TO CREATE FILTERS
     * @param filters Filters
     */
    fun createFilters(filters: Filters) {
        executor.execute {filtersDataSource.createFilters(filters) }
    }

    /**
     * TO UPDATE FILTERS
     * @param filters Filters
     */
    fun updateFilters(filters: Filters) {
        executor.execute {filtersDataSource.updateFilters(filters) }
    }
}