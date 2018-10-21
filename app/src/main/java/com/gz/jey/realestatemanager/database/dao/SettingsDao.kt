package com.gz.jey.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.database.Cursor
import com.gz.jey.realestatemanager.models.Settings


@Dao
interface SettingsDao {

    @Query("SELECT * FROM Settings")
    fun getSettings(): LiveData<Settings>

    @Query("SELECT * FROM Settings")
    fun getSettingsWithCursor(): Cursor

    @Query("DELETE FROM Settings")
    fun deleteSettings(): Int

    @Insert
    fun insertSettings(settings: Settings): Long

    @Update
    fun updateSettings(settings: Settings): Int

}