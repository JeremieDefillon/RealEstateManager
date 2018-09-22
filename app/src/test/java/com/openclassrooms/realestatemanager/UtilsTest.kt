package com.openclassrooms.realestatemanager

import junit.framework.Assert
import org.junit.Test
import java.util.*

class UtilsTest{

    @Test
    fun testGetTodayDate(){
        val cal = Calendar.getInstance()
        val dString = Utils.getTodayDate().split("/")
        val day = cal.get(Calendar.DAY_OF_MONTH)
        // month's array is made from 0 to 11 so we had + 1
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)

        Assert.assertEquals(dString[2].toInt(), day)
        Assert.assertEquals(dString[1].toInt(), month)
        Assert.assertEquals(dString[0].toInt(), year)
    }

    @Test
    fun testGetTodayDateStr(){
        val cal = Calendar.getInstance()
        val dString = Utils.getTodayDateStr().split("/")
        val day = cal.get(Calendar.DAY_OF_MONTH)
        // month's array is made from 0 to 11 so we had + 1
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)

        Assert.assertEquals(dString[0].toInt(), day)
        Assert.assertEquals(dString[1].toInt(), month)
        Assert.assertEquals(dString[2].toInt(), year)
    }

    @Test
    fun testConvertDollarToEuro(){
        val dollar = 100
        val expected = 81
        val euro = Utils.convertDollarToEuro(dollar)

        Assert.assertEquals(expected, euro)
    }

    @Test
    fun testConvertEuroToDollar(){
        val euro = 100
        val expected = 123
        val dollar = Utils.convertEuroToDollar(euro)

        Assert.assertEquals(expected, dollar)
    }
}