package com.gz.jey.realestatemanager.controllers.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.Button
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.LoanSimulatorActivity
import com.gz.jey.realestatemanager.models.Amortizations
import com.gz.jey.realestatemanager.views.AmortizationsAdapter
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView


class ViewDialogAmortizationTable {

    private lateinit var cancelBtn: Button
    // FOR VIEWS
    private lateinit var adapter : AmortizationsAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var totRow : TextView
    private lateinit var totMonthly : TextView
    private lateinit var totInterest : TextView
    private lateinit var totCapital : TextView
    //private lateinit var totFee : TextView

    /**
     * @param activity LoanSimulatorActivity
     * @param amort ArrayList<Amortizations>
     * TO SHOW DIALOG
     */
    @SuppressLint("SetTextI18n")
    fun showDialog(activity: LoanSimulatorActivity, amort : ArrayList<Amortizations>) {
        val d2 = "%.2f"
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_amortization_table)

        this.cancelBtn = dialog.findViewById(R.id.cancel_btn)
        this.recyclerView = dialog.findViewById(R.id.recycler_view)
        this.totRow = dialog.findViewById(R.id.total)
        this.totMonthly = dialog.findViewById(R.id.monthly_tot)
        this.totInterest = dialog.findViewById(R.id.interest_tot)
        this.totCapital = dialog.findViewById(R.id.capital_refund_tot)
        //this.totFee = dialog.findViewById(R.id.capital_fee_tot)
        configureRecyclerView(activity)

        cancelBtn.setOnClickListener { dialog.dismiss() }
        this.adapter.updateData(amort as List<Amortizations>, 0)

        var totm = 0f
        var toti = 0f
        var totc = 0f
        //var totf = 0f
        for (a in amort){
            totm += a.monthly_payed
            toti += a.interest_refunded
            totc += a.capital_refunded
           // totf = a.capital_fee
        }



        val tot = amort.size
        totRow.text = "Total\r\n $tot"
        val totM = d2.format(totm)
        totMonthly.text = "Payed\r\n $totM"
        val totI = d2.format(toti)
        totInterest.text = "Interest\r\n $totI"
        val totC = d2.format(totc)
        totCapital.text = "Capital \r\n $totC"
        //totFee.text = d2.format(totf)

        dialog.show()
    }

    /**
     * @param activity LoanSimulatorActivity
     * TO CONFIGURE RECYCLERVIEW
     */
    private fun configureRecyclerView(activity : LoanSimulatorActivity) {
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.adapter = AmortizationsAdapter(activity)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
    }
}