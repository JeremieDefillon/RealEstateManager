package com.gz.jey.realestatemanager.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.gz.jey.realestatemanager.database.dao.*
import com.gz.jey.realestatemanager.models.sql.*
import com.gz.jey.realestatemanager.utils.IntConverter
import com.gz.jey.realestatemanager.utils.MainPhotoConverter
import com.gz.jey.realestatemanager.utils.PhotosConverter

@Database(entities = [RealEstate::class, Photos::class, Filters::class], version = 1, exportSchema = false)
@TypeConverters(PhotosConverter::class, MainPhotoConverter::class, IntConverter::class)
abstract class ItemDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realEstateDao(): RealEstateDao
    abstract fun photosDao(): PhotosDao
    abstract fun filtersDao(): FiltersDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        /**
         * TO GET ITEM DATABASE
         * @param context Context
         * @return ItemDatabase
         */
        // --- INSTANCE ---
        fun getInstance(context: Context): ItemDatabase? {
            if (INSTANCE == null) {
                synchronized(ItemDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                ItemDatabase::class.java, "RealEstateDatabase.db")
                                .build()
                    }
                }
            }
            return INSTANCE
        }

    }
}