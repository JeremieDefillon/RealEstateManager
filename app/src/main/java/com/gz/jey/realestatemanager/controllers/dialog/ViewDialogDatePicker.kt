package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.models.Code
import java.text.SimpleDateFormat
import java.util.*


class ViewDialogDatePicker {

    private lateinit var titleCanvas : TextView
    private lateinit var inputDate : DatePicker
    private lateinit var cancelBtn : Button
    private lateinit var editBtn : Button
    private lateinit var image : ImageView

    private var result : ArrayList<String> = ArrayList()

    fun showDialog(activity: AddOrEditActivity, code : Int, actualValue : ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_datepicker)

        titleCanvas = dialog.findViewById(R.id.title)
        inputDate = dialog.findViewById(R.id.date_picker)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        image = dialog.findViewById(R.id.image)

        result.clear()
        result.addAll(actualValue)

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        val titleLbl = when (code) {
            Code.SALE_DATE -> activity.getString(R.string.date_of_sale)
            Code.SOLD_DATE -> activity.getString(R.string.date_of_sold)
            else -> ""
        }
        editBtn.setOnClickListener {
            result.clear()
            val year = inputDate.year
            val month = inputDate.month
            val day = inputDate.dayOfMonth

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.format(calendar.time)
            Log.d("Date", date)
            result.add(date)
            activity.insertEditedValue(result)
            dialog.dismiss()
        }
        titleCanvas.text = "$editLbl $titleLbl"
        dialog.show()
    }
}