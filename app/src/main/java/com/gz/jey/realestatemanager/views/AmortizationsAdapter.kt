 package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.amortizations = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmortizationsViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.amortization_item, parent, false)

        return AmortizationsViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: AmortizationsViewHolder, position: Int) {
        val am = this.amortizations[position]
        viewHolder.updateWithAmortizations(this.context, am, currency ,this.callback, position)
    }

    override fun getItemCount(): Int {
        return this.amortizations.size
    }

    fun getAllAmortizations(): List<Amortizations> {
        return this.amortizations
    }

    fun getAmortizations(position: Int): Amortizations {
        return this.amortizations[position]
    }

    fun updateData(amortizations: List<Amortizations>, currency: Int) {
        this.amortizations = amortizations
        this.currency = currency
        this.notifyDataSetChanged()
    }
}