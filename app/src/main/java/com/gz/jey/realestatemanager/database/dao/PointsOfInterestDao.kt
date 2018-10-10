package com.gz.jey.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.gz.jey.realestatemanager.models.PointsOfInterest

@Dao
interface PointsOfInterestDao {

    @Query("SELECT * FROM PointsOfInterest WHERE reId = :reId")
    fun getPointsOfInterest(reId: Long): LiveData<List<PointsOfInterest>>

    @Insert
    fun insertPointsOfInterest(photo: PointsOfInterest): Long

    @Update
    fun updatePointsOfInterest(photo: PointsOfInterest): Int

    @Query("DELETE FROM PointsOfInterest WHERE id = :id")
    fun deletePointsOfInterest(id: Long): Int
}