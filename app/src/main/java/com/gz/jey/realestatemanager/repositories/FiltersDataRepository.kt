package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.gz.jey.realestatemanager.database.dao.FiltersDao
import com.gz.jey.realestatemanager.models.sql.Filters

class FiltersDataRepository(private val filtersDao: FiltersDao) {

    // --- GET ---
    /**
     * TO GET FILTERS
     * @param id Long
     * @return LiveData<Filters>
     */
    fun getFilters(id: Long): LiveData<Filters> {
        return this.filtersDao.getFilters(id)
    }

    // --- CREATE ---
    /**
     * TO CREATE FILTERS
     * @param filters Filters
     * @return Long
     */
    fun createFilters(filters: Filters) : Long{
        return filtersDao.insertFilters(filters)
    }

    // --- UPDATE ---
    /**
     * TO UPDATE FILTERS
     * @param filters Filters
     */
    fun updateFilters(filters: Filters) {
        filtersDao.updateFilters(filters)
    }

}