 package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.sql.RealEstate
import java.util.*

 class RealEstateAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<RealEstateViewHolder>() {

    // FOR DATA
    private lateinit var context : Context
    private var currency = 0
     private var selected : Int? = null
    private var realEstates: List<RealEstate>

    // CALLBACK
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.realEstates = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.real_estate_item, parent, false)

        return RealEstateViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RealEstateViewHolder, position: Int) {
        Log.d("RE ADAPTER", "POSITION = $position")
        val re = this.realEstates[position]

        viewHolder.updateWithRealEstate(this.context, re, currency ,this.callback, position ,this.selected)
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

    fun updateData(realEstates: List<RealEstate>, currency: Int, selected: Int?) {
        this.realEstates = realEstates
        this.currency = currency
        this.selected = selected
        this.notifyDataSetChanged()
    }
}