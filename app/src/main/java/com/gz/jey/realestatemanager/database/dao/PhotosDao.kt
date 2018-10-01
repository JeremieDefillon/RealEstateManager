package com.gz.jey.realestatemanager.database.dao

import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.gz.jey.realestatemanager.models.Photos


@Dao
interface PhotosDao {

    @Query("SELECT * FROM Photos WHERE reId = :reId")
    fun getPhotos(reId: Long): LiveData<List<Photos>>

    @Insert
    fun insertPhotos(photo: Photos): Long

    @Update
    fun updatePhotos(photo: Photos): Int

    @Query("DELETE FROM Photos WHERE id = :id")
    fun deletePhotos(id: Long): Int
}