package com.gz.jey.realestatemanager.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.gz.jey.realestatemanager.database.dao.PhotosDao
import com.gz.jey.realestatemanager.database.dao.PointsOfInterestDao
import com.gz.jey.realestatemanager.database.dao.RealEstateDao
import com.gz.jey.realestatemanager.database.dao.SettingsDao
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.PointsOfInterest
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.models.Settings
import com.gz.jey.realestatemanager.utils.PhotosConverter
import com.gz.jey.realestatemanager.utils.PointsOfInterestConverter


@Database(entities = [RealEstate::class, Photos::class, PointsOfInterest::class, Settings::class], version = 1, exportSchema = false)
@TypeConverters(PhotosConverter::class,PointsOfInterestConverter::class)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realEstateDao(): RealEstateDao
    abstract fun photosDao(): PhotosDao
    abstract fun pointsOfInterestDao(): PointsOfInterestDao
    abstract fun settingsDao(): SettingsDao

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