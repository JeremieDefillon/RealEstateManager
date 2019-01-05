package com.gz.jey.realestatemanager

import com.gz.jey.realestatemanager.utils.Utils
import junit.framework.Assert
import org.junit.Test
import java.util.*
import java.text.SimpleDateFormat


class UtilsTest{

    @Test
    fun testGetTodayDateEn(){
        val cal = Calendar.getInstance()
        val date = cal.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = dateFormat.format(date)

        val dString = Utils.getDateEn(formattedDate).split("/")
        val day = cal.get(Calendar.DAY_OF_MONTH)
        // month's array is made from 0 to 11 so we had + 1
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)

        Assert.assertEquals(dString[2].toInt(), day)
        Assert.assertEquals(dString[1].toInt(), month)
        Assert.assertEquals(dString[0].toInt(), year)
    }

    @Test
    fun testGetTodayDateFr(){
        val cal = Calendar.getInstance()
        val date = cal.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = dateFormat.format(date)

        val dString = Utils.getDateFr(formattedDate).split("/")
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
        val dollar : Long = 100
        val expected : Long = 81
        val euro = Utils.convertDollarToEuro(dollar)

        Assert.assertEquals(expected, euro)
    }

    @Test
    fun testConvertEuroToDollar(){
        val euro : Long = 100
        val expected : Long = 123
        val dollar = Utils.convertEuroToDollar(euro)

        Assert.assertEquals(expected, dollar)
    }
}