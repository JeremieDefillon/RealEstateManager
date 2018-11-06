package com.gz.jey.realestatemanager.repositories

import com.gz.jey.realestatemanager.database.dao.PointsOfInterestDao
import com.gz.jey.realestatemanager.models.sql.PointsOfInterest

class PointsOfInterestDataRepository(private val poiDao: PointsOfInterestDao) {

    // --- GET ---

    // --- CREATE ---

    fun createPOI(poi: PointsOfInterest) {
        poiDao.insertPointsOfInterest(poi)
    }

    // --- DELETE ---

    // --- UPDATE ---
    fun updatePOI(poi: PointsOfInterest) {
        poiDao.updatePointsOfInterest(poi)
    }

}