package com.gz.jey.realestatemanager.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.gz.jey.realestatemanager.database.dao.PhotosDao
import com.gz.jey.realestatemanager.database.dao.RealEstateDao
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate


@Database(entities = [RealEstate::class, Photos::class], version = 1, exportSchema = false)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realEstateDao(): RealEstateDao
    abstract fun photosDao(): PhotosDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): RealEstateManagerDatabase? {
            if (INSTANCE == null) {
                synchronized(RealEstateManagerDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                RealEstateManagerDatabase::class.java, "RealEstateDatabase.db")
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