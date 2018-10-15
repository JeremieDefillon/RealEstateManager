package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.gz.jey.realestatemanager.database.dao.PointsOfInterestDao
import com.gz.jey.realestatemanager.models.PointsOfInterest

class PointsOfInterestDataRepository(private val poiDao: PointsOfInterestDao) {

    // --- GET ---

    fun getAllPOI(reId: Long): LiveData<List<PointsOfInterest>> {
        return this.poiDao.getPointsOfInterest(reId)
    }

    // --- CREATE ---

    fun createPOI(poi: PointsOfInterest) {
        poiDao.insertPointsOfInterest(poi)
    }

    // --- DELETE ---
    fun deletePOI(id: Long) {
        poiDao.deletePointsOfInterest(id)
    }

    // --- UPDATE ---
    fun updatePOI(poi: PointsOfInterest) {
        poiDao.updatePointsOfInterest(poi)
    }

}