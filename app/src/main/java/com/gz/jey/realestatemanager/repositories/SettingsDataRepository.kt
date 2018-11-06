package com.gz.jey.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.gz.jey.realestatemanager.database.dao.SettingsDao
import com.gz.jey.realestatemanager.models.sql.Settings

class SettingsDataRepository(private val settingsDao: SettingsDao) {

    // --- GET ---

    fun getSettings(): LiveData<Settings> {
        return this.settingsDao.getSettings()
    }

    // --- CREATE ---

    fun createSettings(settings: Settings) {
        settingsDao.insertSettings(settings)
    }

    // --- UPDATE ---
    fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

}