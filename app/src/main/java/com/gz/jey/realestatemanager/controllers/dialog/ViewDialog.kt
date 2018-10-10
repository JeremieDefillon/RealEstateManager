package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.fragments.SetRealEstate
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class ViewDialog {

    private lateinit var titleCanvas : TextView
    private lateinit var overview : TextView
    private lateinit var inputText : TextInputEditText
    private lateinit var inputChoice : GridLayout
    private lateinit var inputDate : DatePicker
    private lateinit var cancelBtn : Button
    private lateinit var editBtn : Button

    private val list : ArrayList<String> = ArrayList()
    private var result : ArrayList<String> = ArrayList()

    private var checkCode : Int? = null

    fun showDialog(fragActivity: SetRealEstate ,activity: Activity, code : Int, actualValue : ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)

        titleCanvas = dialog.findViewById(R.id.title_canvas)
        overview = dialog.findViewById(R.id.overview)
        inputText = dialog.findViewById(R.id.input_text)
        inputChoice = dialog.findViewById(R.id.grid_layout)
        inputDate = dialog.findViewById(R.id.date_picker)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)

        result.clear()
        result.addAll(actualValue)

        if(actualValue.isNotEmpty()){
            val editable : Editable? = SpannableStringBuilder(actualValue[0])
            if(editable != null)
                inputText.text = editable
        }

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        var titleLbl = ""

        when(code){
            Code.DISTRICT -> { titleLbl = activity.getString(R.string.district)
                inputText.visibility = View.VISIBLE
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.ADDRESS -> {titleLbl = activity.getString(R.string.address)
                inputText.visibility = View.VISIBLE
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.TYPE -> {titleLbl = activity.getString(R.string.type)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.type_ind))
                inputChoice.visibility = View.VISIBLE
                setOnClick(activity,1, fragActivity, dialog)
            }
            Code.ROOM_NUM -> {titleLbl = activity.getString(R.string.room_number)
                inputText.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.BED_NUM -> {titleLbl = activity.getString(R.string.bed_number)
                inputText.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.BATH_NUM -> {titleLbl = activity.getString(R.string.bath_number)
                inputText.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.KITCHEN_NUM -> {titleLbl = activity.getString(R.string.kitchen_number)
                inputText.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.DESCRIPTION -> {titleLbl = activity.getString(R.string.description)
                inputText.visibility = View.VISIBLE
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.CURRENCY -> {
                titleLbl = activity.getString(R.string.currency)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.currency_ind))
                inputChoice.visibility = View.VISIBLE
                setOnClick(activity,1, fragActivity, dialog)
            }
            Code.PRICE -> {titleLbl = activity.getString(R.string.price)
                overview.visibility = View.VISIBLE
                inputText.visibility = View.VISIBLE
                inputText.inputType = InputType.TYPE_CLASS_NUMBER
                inputText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) { }
                    override fun onTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) { }
                    override fun afterTextChanged(editable : Editable) {
                        overview.text = getPriceOverview(activity, editable.toString())
                    }
                })
                setOnClick(activity,0, fragActivity, dialog)
            }
            Code.PHOTOS -> {titleLbl = activity.getString(R.string.photos)
                inputText.visibility = View.VISIBLE}
            Code.POI -> {titleLbl = activity.getString(R.string.points_of_interest)
                checkCode = Code.MULTICHECK
                list.addAll(activity.resources.getStringArray(R.array.poi_ind))
                inputChoice.visibility = View.VISIBLE
                setOnClick(activity,2, fragActivity, dialog)
            }
            Code.STATUS -> {titleLbl = activity.getString(R.string.status)
                checkCode = Code.SOLOCHECK
                list.addAll(activity.resources.getStringArray(R.array.status_ind))
                inputChoice.visibility = View.VISIBLE
                setOnClick(activity,1, fragActivity, dialog)
            }
            Code.SALE_DATE -> {titleLbl = activity.getString(R.string.date_of_sale)
                inputDate.visibility = View.VISIBLE
                setOnClick(activity,3, fragActivity, dialog)
            }
            Code.SOLD_DATE -> {titleLbl = activity.getString(R.string.date_of_sold)
                inputDate.visibility = View.VISIBLE
                setOnClick(activity,3, fragActivity, dialog)
            }
            Code.AGENT -> {titleLbl = activity.getString(R.string.agent)
                inputText.visibility = View.VISIBLE
                setOnClick(activity,0, fragActivity, dialog)
            }
        }

        titleCanvas.text = "$editLbl $titleLbl"

        when(View.VISIBLE) {
            inputText.visibility -> dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            inputChoice.visibility ->{setCheckList(activity, dialog, list)}
        }
        dialog.show()
    }

    private fun setOnClick(activity: Activity, code : Int , fragActivity: SetRealEstate, dialog: Dialog){
        when(code){
            0 -> {
                editBtn.setOnClickListener {
                    result.clear()
                    result.add(inputText.text.toString())
                    fragActivity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
            1 -> {
                editBtn.setOnClickListener {
                    for (i in 0 until list.size) {
                        result.clear()
                        val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
                        if((dialog.findViewById(resourceId) as RadioButton).isChecked) {
                            result.add(i.toString())
                            break
                        }
                    }
                    fragActivity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
            2 -> {
                editBtn.setOnClickListener {
                    result.clear()
                    for (i in 0 until list.size) {
                        val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
                        if(dialog.findViewById<CheckBox>(resourceId).isChecked) {
                            result.add(i.toString())
                        }
                    }
                    fragActivity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
            3 -> {
                editBtn.setOnClickListener {
                    result.clear()
                    val year = inputDate.year
                    val month = inputDate.month
                    val day = inputDate.dayOfMonth

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)

                    val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                    val date = format.format(calendar.time)
                    Log.d("Date", date)
                    result.add(date)
                    fragActivity.insertEditedValue(result)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun setCheckList(activity: Activity, dialog: Dialog, list: ArrayList<String>){

        val radioGroup : RadioGroup = LayoutInflater.from(activity).inflate(R.layout.radio_group, null) as RadioGroup
        when(checkCode){
            Code.SOLOCHECK -> inputChoice.addView(radioGroup)
        }

        for (i in 0 until list.size) {
            lateinit var checkBtn : View
            val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)
            when(checkCode){
                Code.SOLOCHECK -> {
                    checkBtn = LayoutInflater.from(activity).inflate(R.layout.radio_btn, null)
                    checkBtn.id = resourceId
                    radioGroup.addView(checkBtn)
                    val rd = dialog.findViewById<RadioButton>(resourceId)
                    rd.text = list[i]
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

        for (i in 0 until result.size){
            val n : String? = result[i]
            if(n != null){
                val resourceId = activity.resources.getIdentifier("check_$n", "id", activity.packageName)
                when(checkCode){
                    Code.SOLOCHECK -> {
                        val rd = dialog.findViewById<RadioButton>(resourceId)
                        if(rd != null)
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

    private fun getPriceOverview(activity: Activity, ed : String) : String{
        val sb = StringBuilder()
        val edit = if(ed.isEmpty())"0" else ed
        val num = Utils.convertedHighPrice(edit)
        val overv = activity.getString(R.string.overview)

        when(Data.currency){
            0 -> sb.append(overv).append(" : ").append(activity.getString(R.string.dollar_symbol)).append(" ").append(num)
            1 -> sb.append(overv).append(" : ").append(num).append(" ").append(activity.getString(R.string.euro_symbol))
        }

        return sb.toString()
    }
}