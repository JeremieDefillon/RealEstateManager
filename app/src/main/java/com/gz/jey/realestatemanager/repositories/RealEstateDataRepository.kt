package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SupportSQLiteQuery
import com.gz.jey.realestatemanager.database.dao.RealEstateDao
import com.gz.jey.realestatemanager.models.sql.RealEstate


class RealEstateDataRepository(private val realEstateDao: RealEstateDao) {

    // --- GET ---
    /**
     * TO GET ALL REAL ESTATES
     * @return LiveData<List<RealEstate>>
     */
    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return this.realEstateDao.getAllRealEstate()
    }

    /**
     * TO GET FILTERED REAL ESTATE
     * @param req SupportSQLiteQuery
     * @return LiveData<List<RealEstate>>
     */
    fun getFilteredRealEstate(req : SupportSQLiteQuery): LiveData<List<RealEstate>> {
        return this.realEstateDao.getFilteredRealEstate(req)
    }

    /**
     * TO GET REAL ESTATE BY ID
     * @param id Long
     * @return LiveData<RealEstate>
     */
    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return this.realEstateDao.getRealEstate(id)
    }

    // --- CREATE ---
    /**
     * TO CREATE REAL ESTATE
     * @param realEstate RealEstate
     * @return Long
     */
    fun createRealEstate(realEstate: RealEstate) : Long{
        return realEstateDao.insertRealEstate(realEstate)
    }

    /**
     * TO DELETE REAL ESTATE BY ID
     * @param id Long
     */
    // --- DELETE ---
    fun deleteRealEstate(id: Long) {
        realEstateDao.deleteRealEstate(id)
    }

    /**
     * TO UPDATE REAL ESTATE
     * @param realEstate RealEstate
     */
    // --- UPDATE ---
    fun updateRealEstate(realEstate: RealEstate) {
        realEstateDao.updateRealEstate(realEstate)
    }

}