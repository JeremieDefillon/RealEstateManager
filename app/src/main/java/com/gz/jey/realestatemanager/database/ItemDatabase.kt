package com.gz.jey.realestatemanager.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.gz.jey.realestatemanager.database.dao.*
import com.gz.jey.realestatemanager.models.sql.*
import com.gz.jey.realestatemanager.utils.IntConverter
import com.gz.jey.realestatemanager.utils.PhotosConverter

@Database(entities = [RealEstate::class, Photos::class, Settings::class, Filters::class], version = 1, exportSchema = false)
@TypeConverters(PhotosConverter::class, IntConverter::class)
abstract class ItemDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realEstateDao(): RealEstateDao
    abstract fun photosDao(): PhotosDao
    abstract fun settingsDao(): SettingsDao
    abstract fun filtersDao(): FiltersDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): ItemDatabase? {
            if (INSTANCE == null) {
                synchronized(ItemDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                ItemDatabase::class.java, "RealEstateDatabase.db")
                                //.addCallback(prepopulateDatabase())
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }

        /*// ---
        private fun prepopulateDatabase(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val contentValues = ContentValues()
                    contentValues.put("id", 1)
                    contentValues.put("type", "Flat")

                    db.insert("RealEstate", OnConflictStrategy.IGNORE, contentValues)
                }
            }
        }*/
    }
}