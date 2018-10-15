package com.gz.jey.realestatemanager.injection

import android.content.Context
import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.repositories.PhotosDataRepository
import com.gz.jey.realestatemanager.repositories.PointsOfInterestDataRepository
import com.gz.jey.realestatemanager.repositories.RealEstateDataRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    fun provideRealEstateDataSource(context: Context): RealEstateDataRepository {
        val database = RealEstateManagerDatabase.getInstance(context)
        return RealEstateDataRepository(database!!.realEstateDao())
    }

    fun providePhotosDataSource(context: Context): PhotosDataRepository {
        val database = RealEstateManagerDatabase.getInstance(context)
        return PhotosDataRepository(database!!.photosDao())
    }

    fun providePoIDataSource(context: Context): PointsOfInterestDataRepository {
        val database = RealEstateManagerDatabase.getInstance(context)
        return PointsOfInterestDataRepository(database!!.pointsOfInterestDao())
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceRealEstate = provideRealEstateDataSource(context)
        val dataSourcePhotos = providePhotosDataSource(context)
        val dataSourcePoI = providePoIDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceRealEstate, dataSourcePhotos, dataSourcePoI,executor)
    }
}