package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import android.widget.Button
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.LoanSimulator
import com.gz.jey.realestatemanager.models.Amortizations
import com.gz.jey.realestatemanager.views.AmortizationsAdapter
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView


class ViewDialogAmortizationTable {

    private val TAG = "INPUT ADDRESS"

    private lateinit var cancelBtn: Button
    // FOR VIEWS
    private lateinit var adapter : AmortizationsAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var totMonthly : TextView
    private lateinit var totInterest : TextView
    private lateinit var totCapital : TextView


    fun showDialog(activity: LoanSimulator, amort : ArrayList<Amortizations>) {
        val d2 = "%.2f"
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_amortization_table)

        this.cancelBtn = dialog.findViewById(R.id.cancel_btn)
        this.recyclerView = dialog.findViewById(R.id.recycler_view)
        this.totMonthly = dialog.findViewById(R.id.monthly_tot)
        this.totInterest = dialog.findViewById(R.id.interest_tot)
        this.totCapital = dialog.findViewById(R.id.capital_tot)
        configureRecyclerView(activity)

        cancelBtn.setOnClickListener { dialog.dismiss() }
        this.adapter.updateData(amort as List<Amortizations>, 0)

        var totm = 0f
        var toti = 0f
        var totc = 0f
        for (a in amort){
            totm += a.monthly_payed
            toti += a.interest_refunded
            totc += a.capital_refunded
        }

        totMonthly.text = d2.format(totm)
        totInterest.text = d2.format(toti)
        totCapital.text = d2.format(totc)

        dialog.show()
    }

    private fun configureRecyclerView(activity : LoanSimulator) {
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.adapter = AmortizationsAdapter(activity)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
    }
}