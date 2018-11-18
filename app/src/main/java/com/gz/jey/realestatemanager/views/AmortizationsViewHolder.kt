package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Amortizations
import java.lang.ref.WeakReference

class AmortizationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    // FOR DATA
    private var callbackWeakRef: WeakReference<AmortizationsAdapter.Listener>? = null

    private var row : TextView = itemView.findViewById(R.id.row)
    private var month : TextView = itemView.findViewById(R.id.monthly_payed)
    private var interest : TextView = itemView.findViewById(R.id.interest_refunded)
    private var cRefund : TextView = itemView.findViewById(R.id.capital_refunded)
    private var cFee : TextView = itemView.findViewById(R.id.capital_fee)


    fun updateWithAmortizations(context : Context, item: Amortizations, currency : Int, callback: AmortizationsAdapter.Listener, position: Int) {
        this.callbackWeakRef = WeakReference(callback)

        val fm = "%.2f"
        row.text = item.row.toString()
        month.text = fm.format(item.monthly_payed)
        interest.text = fm.format(item.interest_refunded)
        cRefund.text = fm.format(item.capital_refunded)
        cFee.text = fm.format(item.capital_fee)
    }


    override fun onClick(view: View) {
        callbackWeakRef!!.get()?.onClickDeleteButton(adapterPosition)
    }
}