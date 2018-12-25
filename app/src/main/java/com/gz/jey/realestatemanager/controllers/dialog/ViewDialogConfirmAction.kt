package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.activities.SettingsActivity
import com.gz.jey.realestatemanager.models.Code

class ViewDialogConfirmAction{
    private lateinit var titleCanvas: TextView
    private lateinit var textView: TextView
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    /**
     * @param activity Activity
     * @param code String
     * TO SHOW DIALOG
     */
    fun showDialog(activity: Activity, code : String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_text)

        titleCanvas = dialog.findViewById(R.id.title)
        textView = dialog.findViewById(R.id.text_view)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        cancelBtn.setOnClickListener { dialog.dismiss() }

        when(code){
            Code.DELETE_PHOTOS -> {
                titleCanvas.text = activity.getString(R.string.delete_photos_title)
                textView.text = activity.getString(R.string.delete_photos_msg)
                editBtn.setOnClickListener {
                    val act = activity as AddOrEditActivity
                    act.photosManager.confirmDelete()
                    dialog.dismiss()
                }
            }
            Code.INSERT_RE -> {
                titleCanvas.text = activity.getString(R.string.insert_re_title)
                textView.text = activity.getString(R.string.insert_re_msg)
                editBtn.setOnClickListener {
                    val act = activity as SettingsActivity
                    act.insertRE()
                    dialog.dismiss()
                }
            }
            Code.DELETE_RE -> {
                titleCanvas.text = activity.getString(R.string.delete_re_title)
                textView.text = activity.getString(R.string.delete_re_msg)
                editBtn.setOnClickListener {
                    val act = activity as SettingsActivity
                    act.deleteRE()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

}