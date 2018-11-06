package com.gz.jey.realestatemanager.database.dao

import android.arch.persistence.room.Update
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.database.Cursor
import com.gz.jey.realestatemanager.models.sql.Photos

@Dao
interface PhotosDao {

    @Query("SELECT * FROM Photos WHERE id = :id")
    fun getPhotosWithCursor(id: Long): Cursor

    @Insert
    fun insertPhotos(photo: Photos): Long

    @Update
    fun updatePhotos(photo: Photos): Int

    @Query("DELETE FROM Photos WHERE id = :id")
    fun deletePhotos(id: Long): Int
}