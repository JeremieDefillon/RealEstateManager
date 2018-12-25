package com.gz.jey.realestatemanager.controllers.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.activities.SetFiltersActivity
import com.gz.jey.realestatemanager.models.Code
import kotlin.collections.ArrayList

class ViewDialogMultiChoice {
    private lateinit var titleCanvas: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var inputChoice: GridLayout
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button
    private lateinit var image: ImageView

    private val list: ArrayList<String> = ArrayList()
    private var result: ArrayList<String> = ArrayList()

    private val chk: ArrayList<CheckBox> = ArrayList()
    private val rdb: ArrayList<RadioButton> = ArrayList()

    private var checkCode: String? = null

    /**
     * @param activity Activity
     * @param code String
     * @param actualValue ArrayList<String>
     * TO SHOW DIALOG
     */
    @SuppressLint("SetTextI18n")
    fun showDialog(activity: Activity, code: String, actualValue: ArrayList<String>) {
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
                setOnClick(activity, code, 0, dialog)
            }
            Code.CURRENCY -> {
                titleLbl = activity.getString(R.string.currency)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.currency_ind))
                setOnClick(activity, code, 0, dialog)
            }
            Code.POI -> {
                titleLbl = activity.getString(R.string.points_of_interest)
                checkCode = Code.MULTICHECK
                list.addAll(activity.resources.getStringArray(R.array.poi_ind))
                setOnClick(activity, code, 1, dialog)
            }
            Code.STATUS -> {
                titleLbl = activity.getString(R.string.status)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.status_ind))
                setOnClick(activity, code, 0, dialog)
            }
            Code.FILTER_TYPE -> {
                titleLbl = activity.getString(R.string.type)
                checkCode = Code.MULTICHECK
                list.addAll(activity.resources.getStringArray(R.array.type_ind))
                setOnClick(activity, code, 2, dialog)
            }
            Code.FILTER_POI -> {
                titleLbl = activity.getString(R.string.points_of_interest)
                checkCode = Code.MULTICHECK
                list.addAll(activity.resources.getStringArray(R.array.poi_ind))
                setOnClick(activity, code, 2, dialog)
            }
            Code.FILTER_STATUS -> {
                titleLbl = activity.getString(R.string.status)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.status_ind))
                setOnClick(activity, code, 3, dialog)
            }
        }

        titleCanvas.text = "$editLbl $titleLbl"
        setCheckList(activity, code, list)
        dialog.show()

    }

    /**
     * @param activity Activity
     * @param code String
     * @param type Int
     * @param dialog Dialog
     * TO SET ON CLICK
     */
    private fun setOnClick(activity: Activity, code: String, type: Int, dialog: Dialog) {
        when (type) {
            0 -> {
                editBtn.setOnClickListener {
                    var res = ""
                    for (i in 0 until list.size) {
                        if (rdb[i].isChecked) {
                            res = i.toString()
                            break
                        }
                    }
                    val act = activity as AddOrEditActivity
                    act.addOrEdit.insertStandardValue(code, res)
                    dialog.dismiss()
                }
            }
            1 -> {
                editBtn.setOnClickListener {
                    var res = ""
                    for (i in 0 until list.size) {
                        if (chk[i].isChecked)
                            res+=i.toString()+","
                    }
                    res = res.substring(0, res.length-1)
                    val act = activity as AddOrEditActivity
                    act.addOrEdit.insertStandardValue(code, res)
                    dialog.dismiss()
                }
            }
            2 -> {
                editBtn.setOnClickListener {
                    result.clear()
                    for (i in 0 until list.size) {
                        if (chk[i].isChecked)
                            result.add(i.toString())
                    }
                    val act = activity as SetFiltersActivity
                    act.insertMultiValues(code, result)
                    dialog.dismiss()
                }
            }
            3 -> {
                editBtn.setOnClickListener {
                    for (i in 0 until list.size) {
                        result.clear()
                        if (rdb[i].isChecked) {
                            result.add(i.toString())
                            break
                        }
                    }
                    val act = activity as SetFiltersActivity
                    act.insertStandardValue(code, result[0])
                    dialog.dismiss()
                }
            }
        }
    }

    /**
     * @param activity Activity
     * @param code String
     * @param list ArrayList<String>
     * TO SET CHECKLIST
     */
    @SuppressLint("InflateParams")
    private fun setCheckList(activity: Activity, code: String, list: ArrayList<String>) {
        val params = scrollView.layoutParams
        when (activity.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                inputChoice.columnCount = Math.ceil(list.size / 6.0).toInt()
                Log.d("LANDSCAPE", params.height.toString())
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                inputChoice.columnCount = Math.ceil(list.size / 12.0).toInt()
                Log.d("PORTRAIT", params.height.toString())
            }
        }
        rdb.clear()
        chk.clear()

        for (i in 0 until list.size) {
            lateinit var checkBtn: View
            val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
            when (checkCode) {
                Code.SOLOCHECK -> {
                    checkBtn = LayoutInflater.from(activity).inflate(R.layout.radio_btn, null)
                    checkBtn.id = resourceId
                    inputChoice.addView(checkBtn)
                    rdb.add(inputChoice.getChildAt(i) as RadioButton)
                    rdb[i].text = list[i]
                    if (result.contains(i.toString()))
                        rdb[i].isChecked = true

                    rdb[i].setOnClickListener { checkedSingle(i) }
                }
                Code.MULTICHECK -> {
                    checkBtn = LayoutInflater.from(activity).inflate(R.layout.check_btn, null)
                    checkBtn.id = resourceId
                    inputChoice.addView(checkBtn)
                    chk.add(inputChoice.getChildAt(i) as CheckBox)
                    chk[i].text = list[i]
                    if (result.contains(i.toString()))
                        chk[i].isChecked = true
                }
            }
        }

        if (code != Code.LEGEND) {

            for (i in 0 until result.size) {
                val n: Int? = if (result[i].isNotEmpty()) result[i].toInt() else null
                if (n != null) {
                    when (checkCode) {
                        Code.SOLOCHECK -> rdb[n].isChecked = true
                        Code.MULTICHECK -> chk[n].isChecked = true
                    }
                }
            }
        }
    }

    /**
     * @param c Int
     * TO UNCHECK ALL OTHER CHECKBOX
     */
    private fun checkedSingle(c: Int) {
        for (i in 0 until rdb.size) {
            if (i != c)
                rdb[i].isChecked = false
        }
    }
}