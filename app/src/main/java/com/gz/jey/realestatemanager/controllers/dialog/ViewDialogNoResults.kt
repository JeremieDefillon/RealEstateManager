package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Code

class ViewDialogNoResults {

    private lateinit var titleCanvas: TextView
    private lateinit var message: TextView
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    fun showDialog(activity: Activity, code: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_no_results)

        titleCanvas = dialog.findViewById(R.id.title)
        message = dialog.findViewById(R.id.message)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)

        when (code) {
            Code.NO_RESULTS -> {
                titleCanvas.text = activity.getString(R.string.no_results)
                message.text = activity.getString(R.string.no_results_message)
                cancelBtn.text = activity.getString(R.string.no_results_cancel)
                editBtn.text = activity.getString(R.string.no_results_edit)
                editBtn.setOnClickListener {
                    val act = activity as MainActivity
                    act.addOrEditActivity(false)
                    dialog.dismiss()
                }
                cancelBtn.setOnClickListener {
                    dialog.dismiss()
                }
            }
            Code.FILTERS_FAILED -> {
                titleCanvas.text = activity.getString(R.string.filters_failed)
                message.text = activity.getString(R.string.filters_failed_message)
                cancelBtn.text = activity.getString(R.string.filters_failed_cancel)
                editBtn.text = activity.getString(R.string.filters_failed_edit)
                editBtn.setOnClickListener {
                    val act = activity as MainActivity
                    act.filtersActivity()
                    dialog.dismiss()
                }
                cancelBtn.setOnClickListener {
                    val act = activity as MainActivity
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra(Code.FILTERS, false)
                    act.startActivity(intent)
                    act.finish()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }
}