package com.gz.jey.realestatemanager

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.models.sql.PointsOfInterest
import com.gz.jey.realestatemanager.models.sql.RealEstate
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealEstateDaoTest {

    // FOR DATA
    private var database: RealEstateManagerDatabase? = null

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    // DATA SET FOR TEST
    private val RE_ID : Long = 1
    private val RE_DEMO : RealEstate = RealEstate(RE_ID, 0, 1000000, 540, 6, 5, 2, 1, "", "", 0, "", "", "Jey")

    private val POI_DEMO : ArrayList<PointsOfInterest> = arrayListOf(
            PointsOfInterest(null, 1, RE_ID),
            PointsOfInterest(null, 4, RE_ID),
            PointsOfInterest(null, 5, RE_ID),
            PointsOfInterest(null, 6, RE_ID)
    )

    private val POI_EXPECTED : ArrayList<Int> = arrayListOf(1,4,5,6)

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetRealEstate() {
        // BEFORE : Adding a new user
        this.database!!.realEstateDao().insertRealEstate(RE_DEMO)
        for (p in POI_DEMO){
            this.database!!.pointsOfInterestDao().insertPointsOfInterest(p)
        }

        // TEST
        val re = LiveDataTestUtil.getValue(this.database!!.realEstateDao().getRealEstate(1))
        assertTrue(re.id!! == RE_DEMO.id && re.id == RE_ID)

        val poi = LiveDataTestUtil.getValue(this.database!!.pointsOfInterestDao().getPointsOfInterest(RE_ID))
        for ((i, p) in poi.withIndex()){
            assertTrue(p.value == POI_EXPECTED[i])
        }
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database?.close()
    }
}