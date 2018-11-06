package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.gz.jey.realestatemanager.database.dao.RealEstateDao
import com.gz.jey.realestatemanager.models.sql.RealEstate

class RealEstateDataRepository(private val realEstateDao: RealEstateDao) {

    // --- GET ---

    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return this.realEstateDao.getAllRealEstate()
    }

    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return this.realEstateDao.getRealEstate(id)
    }

    fun getRealEstateBySelect(): LiveData<RealEstate> {
        return this.realEstateDao.getRealEstateBySelect()
    }

    // --- CREATE ---

    fun createRealEstate(realEstate: RealEstate) {
        realEstateDao.insertRealEstate(realEstate)
    }

    // --- DELETE ---
    fun deleteRealEstate(id: Long) {
        realEstateDao.deleteRealEstate(id)
    }

    // --- UPDATE ---
    fun updateRealEstate(realEstate: RealEstate) {
        realEstateDao.updateRealEstate(realEstate)
    }

}