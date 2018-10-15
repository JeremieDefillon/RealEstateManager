package com.gz.jey.realestatemanager

import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.provider.RealEstateContentProvider

import android.arch.persistence.room.Room
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
class RealEstateContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    companion object {
        // DATA SET FOR TEST
        private val REAL_ESTATE_ID: Long = 1
    }

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }

    @Test
    fun getRealEstatesWhenNoRealEstateInserted() {
        val cursor = mContentResolver!!.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, REAL_ESTATE_ID), null, null, null, null)
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor.count, `is`<Int>(1))
        cursor.close()
    }

    @Test
    fun insertAndGetRealEstate() {
        // BEFORE : Adding demo item
        mContentResolver!!.insert(RealEstateContentProvider.URI_REAL_ESTATE, generateRealEstate())
        // TEST
        val cursor = mContentResolver!!.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, REAL_ESTATE_ID), null, null, null, null)
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor.count, `is`<Int>(1))
        assertThat<Boolean>(cursor.moveToFirst(), `is`<Boolean>(true))
        assertThat<String>(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`<String>("2"))
        assertThat<String>(cursor.getString(cursor.getColumnIndexOrThrow("price")), `is`<String>("1000000"))
        assertThat<String>(cursor.getString(cursor.getColumnIndexOrThrow("surface")), `is`<String>("450"))
        assertThat<String>(cursor.getString(cursor.getColumnIndexOrThrow("room")), `is`<String>("9"))
        assertThat<String>(cursor.getString(cursor.getColumnIndexOrThrow("bed")), `is`<String>("5"))
    }

    // ---
    private fun generateRealEstate(): ContentValues {
        val values = ContentValues()
        values.put("id", "1")
        values.put("type" , "2")
        values.put("price", "1000000")
        values.put("surface", "450")
        values.put("room", "9")
        values.put("bed", "5")
        values.put("bath", "2")
        values.put("kitchen", "1")
        values.put("description", "")
        values.put("address", "")
        values.put("status", "0")
        values.put("marketDate", "2018/10/09")
        values.put("soldDate", "")
        values.put("agentName", "Jey")
        return values
    }
}