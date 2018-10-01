package com.gz.jey.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate


@Dao
interface RealEstateDao {

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    fun getRealEstate(id: Long): LiveData<RealEstate>

    @Query("SELECT * FROM RealEstate")
    fun getAllRealState(): LiveData<List<RealEstate>>

    @Insert
    fun insertRealEstate(realEstate: RealEstate): Long

    @Update
    fun updateRealEstate(realEstate: RealEstate): Int

    @Query("DELETE FROM RealEstate WHERE id = :id")
    fun deleteRealEstate(id: Long): Int
}