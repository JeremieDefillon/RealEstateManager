package com.gz.jey.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.database.Cursor
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.models.sql.Settings


@Dao
interface FiltersDao {

    @Query("SELECT * FROM Filters WHERE id=:id")
    fun getFilters(id : Long): LiveData<Filters>

    @Query("SELECT * FROM Filters WHERE id=:id")
    fun getFiltersWithCursor(id : Long): Cursor

    @Query("DELETE FROM Filters")
    fun deleteFilters(): Int

    @Insert
    fun insertFilters(filters: Filters): Long

    @Update
    fun updateFilters(filters: Filters): Int

}