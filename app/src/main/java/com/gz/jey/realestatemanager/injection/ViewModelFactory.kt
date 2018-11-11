package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor

class ViewModelFactory(
        private val itemDataSource: RealEstateDataRepository,
        private val photoDataSource: PhotosDataRepository,
        private val settingsDataSource: SettingsDataRepository,
        private val filtersDataSource: FiltersDataRepository,
        private val executor: Executor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(itemDataSource, photoDataSource, settingsDataSource, filtersDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}