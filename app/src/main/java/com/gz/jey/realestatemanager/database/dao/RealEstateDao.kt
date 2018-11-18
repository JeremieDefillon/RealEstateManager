package com.gz.jey.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import android.database.Cursor
import com.gz.jey.realestatemanager.models.sql.RealEstate

@Dao
interface RealEstateDao {

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    fun getRealEstate(id: Long): LiveData<RealEstate>

    @RawQuery(observedEntities = [RealEstate::class])
    fun getFilteredRealEstate(query: SupportSQLiteQuery): LiveData<List<RealEstate>>

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    fun getRealEstatesWithCursor(id: Long): Cursor

    @Query("SELECT * FROM RealEstate WHERE id != 999999999999999999")
    fun getAllRealEstate(): LiveData<List<RealEstate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRealEstate(realEstate: RealEstate): Long

    @Update
    fun updateRealEstate(realEstate: RealEstate): Int

    @Update
    fun updateAllRealEstates(realEstates: List<RealEstate>): Int

    @Query("DELETE FROM RealEstate WHERE id = :id")
    fun deleteRealEstate(id: Long): Int
}