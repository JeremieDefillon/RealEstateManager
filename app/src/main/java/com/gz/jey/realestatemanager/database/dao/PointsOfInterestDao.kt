package com.gz.jey.realestatemanager.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor
import com.gz.jey.realestatemanager.models.sql.PointsOfInterest

@Dao
interface PointsOfInterestDao {

    @Query("SELECT * FROM PointsOfInterest WHERE id = :id")
    fun getPointsOfInterestWithCursor(id: Long): Cursor

    @Insert
    fun insertPointsOfInterest(photo: PointsOfInterest): Long

    @Update
    fun updatePointsOfInterest(photo: PointsOfInterest): Int
}