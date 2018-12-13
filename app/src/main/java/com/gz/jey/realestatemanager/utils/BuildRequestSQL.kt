package com.gz.jey.realestatemanager.utils

import android.arch.persistence.db.SimpleSQLiteQuery
import android.util.Log
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Filters

class BuildRequestSQL {

    fun setBuild(fi: Filters): SimpleSQLiteQuery? {
        val array = getArrayIndex(fi)
        val str = StringBuilder()
        str.append("SELECT * FROM RealEstate WHERE ")
        val typs = if (fi.type != null) fi.type!!.size  else 0
        val pois = if (fi.poi != null) fi.poi!!.size else 0
        var tot = if(typs+pois==0) 0 else (typs + pois)
        if(typs>0) tot-=1
        if(pois>0) tot-=1
        var args: Array<Any> = Array(array.size + tot) { _ -> "" }
        var count = -1

        for (a in array) {
            when (a) {
                0 -> {
                    count++
                    args = multiTypeArgs(count, args, fi.type!!)
                    str.append(multiTypeStr(count, typs))
                    count += typs - 1
                }
                1 -> {
                    count++
                    args = multiPoiArgs(count, args, fi.poi!!)
                    str.append(multiPoiStr(count, fi.poi!!))
                    count += pois - 1
                }
                2 -> {
                    count++
                    args[count] = fi.minRoom!!
                    str.append(uniqValStr(count, a, fi))
                }
                3 -> {
                    count++
                    args[count] = fi.maxRoom!!
                    str.append(uniqValStr(count, a, fi))
                }
                4 -> {
                    count++
                    args[count] = fi.locality!!
                    str.append(uniqValStr(count, a, fi))
                }
                5 -> {
                    count++
                    args[count] = fi.distance!!
                    str.append(uniqValStr(count, a, fi))
                }
                6 -> {
                    count++
                    args[count] = fi.status == 1
                    str.append(uniqValStr(count, a, fi))
                }
                7 -> {
                    count++
                    args[count] = fi.date!!
                    str.append(uniqValStr(count, a, fi))
                }
                8 -> {
                    count++
                    args[count] = if(Data.currency==1) Utils.convertEuroToDollar(fi.minPrice!!) else fi.minPrice!!
                    str.append(uniqValStr(count, a, fi))
                }
                9 -> {
                    count++
                    args[count] = if(Data.currency==1) Utils.convertEuroToDollar(fi.maxPrice!!) else fi.maxPrice!!
                    str.append(uniqValStr(count, a, fi))
                }
                10 -> {
                    count++
                    args[count] = fi.minSurface!!
                    str.append(uniqValStr(count, a, fi))
                }
                11 -> {
                    count++
                    args[count] = fi.maxSurface!!
                    str.append(uniqValStr(count, a, fi))
                }
                12 -> {
                    count++
                    args[count] = fi.minPhoto!!
                    str.append(uniqValStr(count, a, fi))
                }
            }
        }

        val ar : ArrayList<String> = ArrayList()
        for (i in args)
            ar.add(i.toString())

        // AS re LEFT OUTER JOIN Photos AS ph ON ph.reid=id
        Log.d("SQL FILTERS", str.toString() + " <> " + ar.toString())

        return if(args.isNotEmpty())
            SimpleSQLiteQuery(str.toString(), args)
        else
            null
    }

    private fun uniqValStr(count: Int, index: Int, fi : Filters): String {
        val req = StringBuilder()
        if (count > 0) req.append(" AND ")
        var table = ""
        when (index) {
            2 -> table = "room>=?"
            3 -> table = "room<=?"
            4 -> table = "locality=? COLLATE NOCASE"
            5 -> table = "distance=?"
            6 -> table = "sold=?"
            7 -> table = if(fi.status==0) "marketDate>=?" else "soldDate>=?"
            8 -> table = "price>=?"
            9 -> table = "price<=?"
            10 -> table = "surface>=?"
            11 -> table = "surface<=?"
            12 -> table = "photoNum>=?"
        }
        req.append(table)
        return req.toString()
    }

    private fun multiTypeStr(count: Int, max: Int): String {
        val req = StringBuilder()
        if (count > 0) req.append(" AND ")
        if (max > 1) req.append("(")
        for (i in 0 until max) {
            if (i != 0)
                req.append(" OR ")
            req.append("type=?")
        }
        if (max > 1) req.append(")")
        return req.toString()
    }

    private fun multiPoiStr(count: Int, poi: List<Int>): String {
        val req = StringBuilder()
        if (count > 0) req.append(" AND ")
        var table = ""
        for ((i, p) in poi.withIndex()) {
            if (i != 0)
               req.append(" AND ")
            when (p) {
                0 -> table = "poiSchool=?"
                1 -> table = "poiShops=?"
                2 -> table = "poiPark=?"
                3 -> table = "poiSubway=?"
                4 -> table = "poiBus=?"
                5 -> table = "poiTrain=?"
                6 -> table = "poiHospital=?"
                7 -> table = "poiAirport=?"
            }
            req.append(table)
        }
        return req.toString()
    }

    private fun multiTypeArgs(count: Int, args: Array<Any>, value: List<Int>): Array<Any> {
        for ((i,v) in value.withIndex())
            args[count+i] = v
        return args
    }

    private fun multiPoiArgs(count: Int, args: Array<Any>, value: List<Int>): Array<Any> {
        for ((i) in value.withIndex()) {
            args[count + i] = true
        }
        return args
    }

    private fun getArrayIndex(fi: Filters): ArrayList<Int> {
        val array: ArrayList<Int> = ArrayList()
        if (fi.type != null) array.add(0)
        if (fi.poi != null) array.add(1)
        if (fi.minRoom != null) array.add(2)
        if (fi.maxRoom != null) array.add(3)
        if (fi.locality != null) array.add(4)
        if (fi.distance != null) array.add(5)
        if (fi.status != null) array.add(6)
        if (fi.date != null) array.add(7)
        if (fi.minPrice != null) array.add(8)
        if (fi.maxPrice != null) array.add(9)
        if (fi.minSurface != null) array.add(10)
        if (fi.maxSurface != null) array.add(11)
        if (fi.minPhoto != null) array.add(12)

        return array
    }
}