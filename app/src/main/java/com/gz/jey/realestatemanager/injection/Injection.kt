package com.gz.jey.realestatemanager.injection

import android.content.Context
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    /**
     * @param context Context
     * TO GET REAL ESTATE DATA REPOSITORY
     * @return RealEstateDataRepository
     */
    private fun provideRealEstateDataSource(context: Context): RealEstateDataRepository {
        val database = ItemDatabase.getInstance(context)
        return RealEstateDataRepository(database!!.realEstateDao())
    }

    /**
     * @param context Context
     * TO GET FILTERS DATA REPOSITORY
     * @return FiltersDataRepository
     */
    private fun provideFiltersDataSource(context: Context): FiltersDataRepository {
        val database = ItemDatabase.getInstance(context)
        return FiltersDataRepository(database!!.filtersDao())
    }

    /**
     * TO PROVIDE EXECUTOR
     * @return Executor
     */
    private fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()!!
    }

    /**
     * TO VIEW MODEL FACTORY
     * @param context Context
     * @return ViewModelFactory
     */
    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceRealEstate = provideRealEstateDataSource(context)
        val dataSourceFilters = provideFiltersDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceRealEstate, dataSourceFilters,executor)
    }
}