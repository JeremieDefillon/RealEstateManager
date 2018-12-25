package com.gz.jey.realestatemanager.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.gz.jey.realestatemanager.repositories.*
import java.util.concurrent.Executor

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val itemDataSource: RealEstateDataRepository,
        private val filtersDataSource: FiltersDataRepository,
        private val executor: Executor
) : ViewModelProvider.Factory {

    /**
     * TO CREATE VIEW MODEL
     * @param modelClass Class<T>
     * @return T
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(itemDataSource, filtersDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}