package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.utils.Utils
import kotlin.collections.ArrayList


class ViewDialogInputText {

    private lateinit var titleCanvas: TextView
    private lateinit var inputText: TextInputEditText
    private lateinit var overview: TextView
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    private val list: ArrayList<String> = ArrayList()
    private lateinit var result: String

    fun showDialog(activity: AddOrEditActivity, code: Int, actualValue: ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_input_text)

        titleCanvas = dialog.findViewById(R.id.title)
        inputText = dialog.findViewById(R.id.input_text)
        overview = dialog.findViewById(R.id.overview)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        result = if(actualValue.isNotEmpty()) actualValue[0] else ""

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        var titleLbl = ""

        when (code) {
            Code.DISTRICT -> { titleLbl = activity.getString(R.string.district) }
            Code.ADDRESS -> { titleLbl = activity.getString(R.string.address) }
            Code.SURFACE -> {
                titleLbl = activity.getString(R.string.surface)
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Code.ROOM_NUM -> {
                titleLbl = activity.getString(R.string.room_number)
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Code.BED_NUM -> {
                titleLbl = activity.getString(R.string.bed_number)
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Code.BATH_NUM -> {
                titleLbl = activity.getString(R.string.bath_number)
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Code.KITCHEN_NUM -> {
                titleLbl = activity.getString(R.string.kitchen_number)
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Code.DESCRIPTION -> { titleLbl = activity.getString(R.string.description) }
            Code.PRICE -> {
                titleLbl = activity.getString(R.string.price)
                overview.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                inputText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        overview.text = getPriceOverview(activity, editable.toString())
                    }
                })
            }
            Code.AGENT -> { titleLbl = activity.getString(R.string.agent) }
        }

        editBtn.setOnClickListener {
            val res : ArrayList<String> = ArrayList()
            res.add(inputText.text.toString())
            activity.insertEditedValue(res)
            dialog.dismiss()
        }
        titleCanvas.text = "$editLbl $titleLbl"
        dialog.show()
    }

    private fun getPriceOverview(activity: Activity, ed: String): String {
        val sb = StringBuilder()
        val edit = if (ed.isEmpty()) 0 else ed.toInt()
        val num = Utils.convertedHighPrice(activity, 0, edit)
        val overv = activity.getString(R.string.overview)

        val currency = 1
        when (currency) {
            0 -> sb.append(overv).append(" : ").append(activity.getString(R.string.dollar_symbol)).append(" ").append(num)
            1 -> sb.append(overv).append(" : ").append(num).append(" ").append(activity.getString(R.string.euro_symbol))
        }

        return sb.toString()
    }

}