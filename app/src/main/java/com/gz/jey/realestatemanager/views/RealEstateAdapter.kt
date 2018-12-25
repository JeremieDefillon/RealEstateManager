package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.BuildItems
import java.util.*

class RealEstateAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<RealEstateViewHolder>() {

    // FOR DATA
    private lateinit var context: Context
    private var realEstates: List<RealEstate>

    private var width: Int = 0
    private var height: Int = 0

    // CALLBACK
    /**
     * INTERFACE FOR ON CLICK DELETE BUTTON
     */
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.realEstates = ArrayList()
    }

    /**
     * ON CREATE VIEW
     * @param parent ViewGroup
     * @param viewType Int
     * @return RealEstateViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        context = parent.context
        val view = BuildItems().reItem(context, width, height)
        return RealEstateViewHolder(view)
    }

    /**
     * ON BIND VIEW
     * @param viewHolder RealEstateViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(viewHolder: RealEstateViewHolder, position: Int) {
        val re = this.realEstates[position]
        viewHolder.updateWithRealEstate(this.context, re, this.callback)
    }

    /**
     * TO GET COUNT OF ITEMS
     * @return Int
     */
    override fun getItemCount(): Int {
        return this.realEstates.size
    }

    /**
     * TO GET ALL REAL ESTATES
     * @return List<RealEstate>
     */
    fun getAllRealEstate(): List<RealEstate> {
        return this.realEstates
    }

    /**
     * TO UPDATE DATAS
     * @param realEstates List<RealEstate>
     * @param width Int
     * @param height Int
     */
    fun updateData(realEstates: List<RealEstate>, width: Int, height: Int) {
        this.realEstates = realEstates
        this.width = width
        this.height = height
        this.notifyDataSetChanged()
    }
}