package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.gz.jey.realestatemanager.database.dao.FiltersDao
import com.gz.jey.realestatemanager.models.sql.Filters

class FiltersDataRepository(private val filtersDao: FiltersDao) {

    // --- GET ---

    fun getFilters(id : Long): LiveData<Filters> {
        return this.filtersDao.getFilters(id)
    }

    // --- CREATE ---

    fun createFilters(filters: Filters) {
        filtersDao.insertFilters(filters)
    }

    // --- UPDATE ---
    fun updateFilters(filters: Filters) {
        filtersDao.updateFilters(filters)
    }

}