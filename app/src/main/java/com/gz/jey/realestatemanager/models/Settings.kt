package com.gz.jey.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues


@Entity(tableName = "Settings")
data class Settings(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        var currency: Int = 0,
        var lang: Int = 0,
        var reId: Long?,
        var addOrEdit: Boolean = false,
        var enableNotif: Boolean = true
) {
    companion object {
        // --- UTILS ---
        fun fromContentValues(values: ContentValues): Settings {
            val st = Settings()
            if (values.containsKey("currency")) st.currency = values.getAsInteger("currency")
            if (values.containsKey("lang")) st.lang = values.getAsInteger("lang")
            if (values.containsKey("reId")) st.reId = values.getAsLong("reId")
            if (values.containsKey("addOrEdit")) st.addOrEdit = values.getAsBoolean("addOrEdit")
            if (values.containsKey("enableNotif")) st.enableNotif = values.getAsBoolean("enableNotif")
            return st
        }
    }

    constructor() : this(
            null,
            0,
            0,
            null,
            false,
            true)

}