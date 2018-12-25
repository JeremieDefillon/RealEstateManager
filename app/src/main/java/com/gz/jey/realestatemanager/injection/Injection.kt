package com.gz.jey.realestatemanager.injection

import android.content.Context
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideRealEstateDataSource(context: Context): RealEstateDataRepository {
        val database = ItemDatabase.getInstance(context)
        return RealEstateDataRepository(database!!.realEstateDao())
    }

    private fun provideFiltersDataSource(context: Context): FiltersDataRepository {
        val database = ItemDatabase.getInstance(context)
        return FiltersDataRepository(database!!.filtersDao())
    }

    private fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()!!
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceRealEstate = provideRealEstateDataSource(context)
        val dataSourceFilters = provideFiltersDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceRealEstate, dataSourceFilters,executor)
    }
}