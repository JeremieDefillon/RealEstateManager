package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.models.Code
import java.util.*


class ViewDialogMultiChoice {

    private lateinit var titleCanvas: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var inputChoice: GridLayout
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button
    private lateinit var image: ImageView

    private val list: ArrayList<String> = ArrayList()
    private var result: ArrayList<String> = ArrayList()
    private var uri: String? = null

    private var checkCode: Int? = null

    fun showDialog(activity: AddOrEditActivity, code: Int, actualValue: ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_multi_choice)

        titleCanvas = dialog.findViewById(R.id.title)
        inputChoice = dialog.findViewById(R.id.grid_layout)
        scrollView = dialog.findViewById(R.id.scroll_view)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        image = dialog.findViewById(R.id.image)

        result.clear()
        result.addAll(actualValue)

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        var titleLbl = ""

        when (code) {
            Code.TYPE -> {
                titleLbl = activity.getString(R.string.type)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.type_ind))
                setOnClick(activity, 0, dialog)
            }
            Code.CURRENCY -> {
                titleLbl = activity.getString(R.string.currency)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.currency_ind))
                setOnClick(activity, 0, dialog)
            }
            Code.POI -> {
                titleLbl = activity.getString(R.string.points_of_interest)
                checkCode = Code.MULTICHECK
                list.addAll(activity.resources.getStringArray(R.array.poi_ind))
                setOnClick(activity, 1, dialog)
            }
            Code.STATUS -> {
                titleLbl = activity.getString(R.string.status)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.status_ind))
                setOnClick(activity, 0, dialog)
            }
            Code.LEGEND -> {
                titleLbl = activity.getString(R.string.legend)
                uri = actualValue[0]
                image.visibility = View.VISIBLE
                Glide.with(activity)
                        .load(uri)
                        .into(image)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.rooms_ind))
                setOnClick(activity, 2, dialog)
            }
        }

        titleCanvas.text = "$editLbl $titleLbl"
        setCheckList(activity, dialog, list)
        dialog.show()

    }

    private fun setOnClick(activity: AddOrEditActivity, code: Int, dialog: Dialog) {
        when (code) {
            0 -> {
                editBtn.setOnClickListener {
                    for (i in 0 until list.size) {
                        result.clear()
                        val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
                        if ((dialog.findViewById(resourceId) as RadioButton).isChecked) {
                            result.add(i.toString())
                            break
                        }
                    }
                    activity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
            1 -> {
                editBtn.setOnClickListener {
                    result.clear()
                    for (i in 0 until list.size) {
                        val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
                        if (dialog.findViewById<CheckBox>(resourceId).isChecked) {
                            result.add(i.toString())
                        }
                    }
                    activity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
            2 -> {
                editBtn.setOnClickListener {
                    for (i in 0 until list.size) {
                        result.clear()
                        val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
                        if ((dialog.findViewById(resourceId) as RadioButton).isChecked) {
                            activity.savePhoto(uri!!, i)
                            dialog.dismiss()
                            break
                        }
                    }
                }
            }
        }
    }

    private fun setCheckList(activity: AddOrEditActivity, dialog: Dialog, list: ArrayList<String>) {
        val params = scrollView.layoutParams
        when (activity.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                inputChoice.columnCount =  Math.ceil(list.size / 7.0).toInt()
                Log.d("LANDSCAPE", params.height.toString())
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                inputChoice.columnCount =  Math.ceil(list.size / 14.0).toInt()
                Log.d("PORTRAIT", params.height.toString())}
        }

        for (i in 0 until list.size) {
            lateinit var checkBtn: View
            val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
            when (checkCode) {
                Code.SOLOCHECK -> {
                    checkBtn = LayoutInflater.from(activity).inflate(R.layout.radio_btn, null)
                    checkBtn.id = resourceId
                    inputChoice.addView(checkBtn)
                    val rd = dialog.findViewById<RadioButton>(resourceId)
                    rd.text = list[i]
                    Log.d(rd.text.toString(),list[i])
                    rd.setOnClickListener {
                        checkedSingle(activity ,dialog, i, list.size)
                    }
                }
                Code.MULTICHECK -> {
                    checkBtn = LayoutInflater.from(activity).inflate(R.layout.check_btn, null)
                    checkBtn.id = resourceId
                    inputChoice.addView(checkBtn)
                    val rd = dialog.findViewById<CheckBox>(resourceId)
                    rd.text = list[i]
                }
            }
        }

        for (i in 0 until result.size) {
            val n: String? = result[i]
            if (n != null) {
                val resourceId = activity.resources.getIdentifier("check_$n", "id", activity.packageName)
                when (checkCode) {
                    Code.SOLOCHECK -> {
                        val rd = dialog.findViewById<RadioButton>(resourceId)
                        if (rd != null)
                            rd.isChecked = true
                    }
                    Code.MULTICHECK -> {
                        val rd = dialog.findViewById<CheckBox>(resourceId)
                        rd.isChecked = true
                    }
                }
            }
        }
    }

    private fun checkedSingle(activity : AddOrEditActivity, dialog: Dialog ,c : Int, max :Int){
        for (i in 0 until max){
            val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
            if(i!=c)
                dialog.findViewById<RadioButton>(resourceId).isChecked = false
        }
    }
}