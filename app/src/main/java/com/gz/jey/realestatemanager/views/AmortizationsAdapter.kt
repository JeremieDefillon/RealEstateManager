 package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Amortizations
import java.util.*

 class AmortizationsAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<AmortizationsViewHolder>() {

    // FOR DATA
    private lateinit var context : Context
    private var currency = 0
    private var amortizations: List<Amortizations>

    // CALLBACK
     /**
      * INTERFACE FOR ON CLICK DELETE BUTTON
      */
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.amortizations = ArrayList()
    }

     /**
      * ON CREATE VIEW
      * @param parent ViewGroup
      * @param viewType Int
      * @return AmortizationsViewHolder
      */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmortizationsViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.amortization_item, parent, false)

        return AmortizationsViewHolder(view)
    }

     /**
      * ON BIND VIEW
      * @param viewHolder AmortizationsViewHolder
      * @param position Int
      */
    override fun onBindViewHolder(viewHolder: AmortizationsViewHolder, position: Int) {
        val am = this.amortizations[position]
        viewHolder.updateWithAmortizations( am,this.callback)
    }

     /**
      * TO GET COUNT OF ITEMS
      * @return Int
      */
    override fun getItemCount(): Int {
        return this.amortizations.size
    }

     /**
      * TO UPDATE DATAS
      * @param amortizations List<Amortizations>
      * @param currency Int
      */
    fun updateData(amortizations: List<Amortizations>, currency: Int) {
        this.amortizations = amortizations
        this.currency = currency
        this.notifyDataSetChanged()
    }
}