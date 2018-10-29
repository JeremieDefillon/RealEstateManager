package com.gz.jey.realestatemanager.repositories

import com.gz.jey.realestatemanager.database.dao.PhotosDao
import com.gz.jey.realestatemanager.models.Photos

class PhotosDataRepository(private val photosDao: PhotosDao) {

    // --- CREATE ---

    fun createPhotos(photos: Photos) {
        photosDao.insertPhotos(photos)
    }

    // --- DELETE ---
    fun deletePhotos(id: Long) {
        photosDao.deletePhotos(id)
    }

    // --- UPDATE ---
    fun updatePhotos(photos: Photos) {
        photosDao.updatePhotos(photos)
    }

}