package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SupportSQLiteQuery
import com.gz.jey.realestatemanager.database.dao.RealEstateDao
import com.gz.jey.realestatemanager.models.sql.RealEstate
import android.arch.lifecycle.MediatorLiveData



class RealEstateDataRepository(private val realEstateDao: RealEstateDao) {

    // --- GET ---

    fun getAllRealEstate(): LiveData<List<RealEstate>> {
        return this.realEstateDao.getAllRealEstate()
    }

    fun getFilteredRealEstate(req : SupportSQLiteQuery): LiveData<List<RealEstate>> {
        return this.realEstateDao.getFilteredRealEstate(req)
    }

    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return this.realEstateDao.getRealEstate(id)
    }

    // --- CREATE ---

    fun createRealEstate(realEstate: RealEstate) : Long{
        return realEstateDao.insertRealEstate(realEstate)
    }

    // --- DELETE ---
    fun deleteRealEstate(id: Long) {
        realEstateDao.deleteRealEstate(id)
    }

    // --- UPDATE ---
    fun updateRealEstate(realEstate: RealEstate) {
        realEstateDao.updateRealEstate(realEstate)
    }
    fun updateAllRealEstates(realEstates: List<RealEstate>) {
        realEstateDao.updateAllRealEstates(realEstates)
    }


}