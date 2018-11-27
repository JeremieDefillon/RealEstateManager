 package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.BuildItems
import java.util.*

 class RealEstateAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<RealEstateViewHolder>() {

    // FOR DATA
    private lateinit var context : Context
    private var selected : Int? = null
    private var realEstates: List<RealEstate>

    private var width : Int = 0
    private var height : Int = 0

    // CALLBACK
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.realEstates = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        context = parent.context
        val view = BuildItems().reItem(context, width, height)
        return RealEstateViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RealEstateViewHolder, position: Int) {
        Log.d("RE ADAPTER", "POSITION = $position")
        val re = this.realEstates[position]
        viewHolder.updateWithRealEstate(this.context, re, this.callback, position ,this.selected)
    }

    override fun getItemCount(): Int {
        return this.realEstates.size
    }

    fun getAllRealEstate(): List<RealEstate> {
        return this.realEstates
    }

    fun getRealEstate(position: Int): RealEstate {
        return this.realEstates[position]
    }

    fun updateData(realEstates: List<RealEstate>, selected: Int?, width : Int, height: Int) {
        this.realEstates = realEstates
        this.selected = selected
        this.width = width
        this.height = height
        this.notifyDataSetChanged()
    }
}