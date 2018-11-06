package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.activities.SetFilters
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.utils.Utils
import kotlin.collections.ArrayList

class ViewDialogMinMax {

    private lateinit var titleCanvas: TextView
    private lateinit var labelMin: TextView
    private lateinit var labelMax: TextView
    private lateinit var inputMin: TextInputEditText
    private lateinit var inputMax: TextInputEditText
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    private val list: ArrayList<String> = ArrayList()
    private lateinit var result: String

    fun showDialog(activity: Activity, code: Int, actualValue: ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_min_max)

        titleCanvas = dialog.findViewById(R.id.title)
        inputMin = dialog.findViewById(R.id.input_min)
        inputMax = dialog.findViewById(R.id.input_max)
        labelMin = dialog.findViewById(R.id.min_lbl)
        labelMax = dialog.findViewById(R.id.max_lbl)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        result = if(actualValue.isNotEmpty()) actualValue[0] else ""

        cancelBtn.setOnClickListener { dialog.dismiss() }


        val editLbl = activity.getString(R.string.insert)
        var titleLbl = ""

        when (code) {
            Code.FILTER_SURFACE -> { titleLbl = activity.getString(R.string.filter_surface) }
            Code.FILTER_PRICE -> { titleLbl = activity.getString(R.string.filter_price) }
        }

        if(actualValue.isNotEmpty()){
            when(actualValue.size){
                1 -> inputMin.text = SpannableStringBuilder(actualValue[0])
                2 -> inputMax.text = SpannableStringBuilder(actualValue[1])
            }
        }

        editBtn.setOnClickListener {
            val res : ArrayList<String> = ArrayList()
            res.add(inputMin.text.toString())
            res.add(inputMax.text.toString())
            val act = activity as SetFilters
            act.insertEditedValue(code, res)
            dialog.dismiss()
        }
        titleCanvas.text = "$editLbl $titleLbl"
        dialog.show()
    }
}