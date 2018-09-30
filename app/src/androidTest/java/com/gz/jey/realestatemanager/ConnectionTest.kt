package com.gz.jey.realestatemanager

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gz.jey.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ConnectionTest {

    private var instrumentationCtx: Context? = null

    @Before
    fun setup() {
        instrumentationCtx = InstrumentationRegistry.getContext()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testIfConnected() {
        Assert.assertTrue(Utils.isConnected(instrumentationCtx!!))
    }

}