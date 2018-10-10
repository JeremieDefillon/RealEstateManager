package com.gz.jey.realestatemanager.controllers.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialog
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils


class SetRealEstate : Fragment(){

    private lateinit var mView : View

    private var mainActivity : MainActivity? = null
    private lateinit var checks : ArrayList<ImageView>

    // FOR DESIGN
    private lateinit var addIcon : Drawable
    private lateinit var removeIcon : Drawable
    private lateinit var editIcon : Drawable
    private lateinit var validIcon : Drawable
    private lateinit var unvalidIcon : Drawable
    private lateinit var dollarIcon : Drawable
    private lateinit var euroIcon : Drawable

    private lateinit var addPhotos : Button
    private lateinit var removePhotos : Button
    private lateinit var addPOI : Button

    // + VALUES INPUT
    private lateinit var soldDateLine : LinearLayout
    private lateinit var photosRecyclerView : RecyclerView
    private lateinit var editDistrict : Button
    private lateinit var editAddress : Button
    private lateinit var editRoomNum : Button
    private lateinit var editBedNum : Button
    private lateinit var editBathNum : Button
    private lateinit var editKitchenNum : Button
    private lateinit var editDescription : Button
    private lateinit var editPrice : Button
    private lateinit var editAgent : Button
    private lateinit var editType : Button
    private lateinit var editCurrency : Button
    private lateinit var editStatus : Button
    private lateinit var editSaleDate : Button
    private lateinit var editSoldDate : Button

    private lateinit var valueDistrict : TextView
    private lateinit var valueAddress : TextView
    private lateinit var valueRoomNum : TextView
    private lateinit var valueBedNum : TextView
    private lateinit var valueBathNum : TextView
    private lateinit var valueKitchenNum : TextView
    private lateinit var valueDescription : TextView
    private lateinit var valuePrice : TextView
    private lateinit var valuePoi : TextView
    private lateinit var valueAgent : TextView
    private lateinit var valueType : TextView
    private lateinit var valueCurrency : TextView
    private lateinit var valueStatus : TextView
    private lateinit var valueSaleDate : TextView
    private lateinit var valueSoldDate : TextView

    // FOR DATA
    private var oblArray : ArrayList<Int> = arrayListOf(0,2,3,8,9)
    private var visi : ArrayList<Int> = arrayListOf(View.GONE,View.VISIBLE)
    private var result : ArrayList<String> = ArrayList()

    private var price : Int? = null
    private var typeNum : Int? = null
    private var currencyNum : Int = 0
    private var statusNum : Int = 0
    private lateinit var poiList : ArrayList<Int>
    var insert : Int? = null

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.set_real_estate, container, false)
        mainActivity = activity as MainActivity

        return mView
    }

    /**
     * CALLED WHEN VIEW CREATED
     * @param view View
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setIcon()
        mainActivity!!.enableSave = false
        mainActivity!!.changeToolBarMenu(2)

        photosRecyclerView = view.findViewById(R.id.photos_recycler_view)
        soldDateLine = view.findViewById(R.id.sold_date_line)

        addPhotos = view.findViewById(R.id.add_picture)
        //removePhotos = view.findViewById(R.id.remove_picture)
        addPOI = view.findViewById(R.id.add_poi)
        editDistrict = view.findViewById(R.id.edit_district)
        editAddress = view.findViewById(R.id.edit_address)
        editType = view.findViewById(R.id.edit_type)
        editRoomNum = view.findViewById(R.id.edit_room_num)
        editBedNum = view.findViewById(R.id.edit_bed_num)
        editBathNum = view.findViewById(R.id.edit_bath_num)
        editKitchenNum = view.findViewById(R.id.edit_kitchen_num)
        editDescription = view.findViewById(R.id.edit_description)
        editCurrency = view.findViewById(R.id.edit_currency)
        editPrice = view.findViewById(R.id.edit_price)
        editStatus = view.findViewById(R.id.edit_status)
        editSaleDate = view.findViewById(R.id.edit_sale_date)
        editSoldDate = view.findViewById(R.id.edit_sold_date)
        editAgent = view.findViewById(R.id.edit_agent)

        valueDistrict = view.findViewById(R.id.value_district)
        valueAddress = view.findViewById(R.id.value_address)
        valueType = view.findViewById(R.id.value_type)
        valueRoomNum = view.findViewById(R.id.value_room_num)
        valueBedNum = view.findViewById(R.id.value_bed_num)
        valueBathNum = view.findViewById(R.id.value_bath_num)
        valueKitchenNum = view.findViewById(R.id.value_kitchen_num)
        valueDescription = view.findViewById(R.id.value_description)
        valueCurrency = view.findViewById(R.id.value_currency)
        valuePrice = view.findViewById(R.id.value_price)
        valuePoi = view.findViewById(R.id.value_poi)
        valueStatus = view.findViewById(R.id.value_status)
        valueSaleDate = view.findViewById(R.id.value_sale_date)
        valueSoldDate = view.findViewById(R.id.value_sold_date)
        valueAgent = view.findViewById(R.id.value_agent)
        valueDescription.movementMethod = ScrollingMovementMethod()

        dollarIcon = SetImageColor.changeDrawableColor(context!! ,R.drawable.dollar, ContextCompat.getColor(context!!, R.color.colorSecondary))
        euroIcon = SetImageColor.changeDrawableColor(context!! ,R.drawable.euro, ContextCompat.getColor(context!!, R.color.colorSecondary))

        addPhotos.background = addIcon
        addPOI.background = addIcon
        editDistrict.background = editIcon
        editAddress.background = editIcon
        editType.background = editIcon
        editRoomNum.background = editIcon
        editBedNum.background = editIcon
        editBathNum.background = editIcon
        editKitchenNum.background = editIcon
        editDescription.background = editIcon
        editPrice.background = editIcon
        editCurrency.background = editIcon
        photosRecyclerView.background = editIcon
        editStatus.background = editIcon
        editSaleDate.background = editIcon
        editSoldDate.background = editIcon
        editAgent.background = editIcon

        checks = ArrayList()
        checks.add(view.findViewById(R.id.check_0))
        checks.add(view.findViewById(R.id.check_1))
        checks.add(view.findViewById(R.id.check_2))
        checks.add(view.findViewById(R.id.check_3))
        checks.add(view.findViewById(R.id.check_4))
        checks.add(view.findViewById(R.id.check_5))
        checks.add(view.findViewById(R.id.check_6))
        checks.add(view.findViewById(R.id.check_7))
        checks.add(view.findViewById(R.id.check_8))
        checks.add(view.findViewById(R.id.check_9))
        checks.add(view.findViewById(R.id.check_10))
        checks.add(view.findViewById(R.id.check_11))
        checks.add(view.findViewById(R.id.check_12))
        checks.add(view.findViewById(R.id.check_13))
        checks.add(view.findViewById(R.id.check_14))
        checks.add(view.findViewById(R.id.check_15))

        editDistrict.setOnClickListener {
            result.clear()
            result.add(valueDistrict.text.toString())
            openInputEditor(Code.DISTRICT) }
        editAddress.setOnClickListener {
            result.clear()
            result.add(valueAddress.text.toString())
            openInputEditor(Code.ADDRESS) }
        editType.setOnClickListener {
            result.clear()
            result.add(typeNum.toString())
            openInputEditor(Code.TYPE) }
        editRoomNum.setOnClickListener {
            result.clear()
            result.add(valueRoomNum.text.toString())
            openInputEditor(Code.ROOM_NUM) }
        editBedNum.setOnClickListener {
            result.clear()
            result.add(valueBedNum.text.toString())
            openInputEditor(Code.BED_NUM) }
        editBathNum.setOnClickListener {
            result.clear()
            result.add(valueBathNum.text.toString())
            openInputEditor(Code.BATH_NUM) }
        editKitchenNum.setOnClickListener {
            result.clear()
            result.add(valueKitchenNum.text.toString())
            openInputEditor(Code.KITCHEN_NUM) }
        editDescription.setOnClickListener {
            result.clear()
            result.add(valueDescription.text.toString())
            openInputEditor(Code.DESCRIPTION) }
        editCurrency.setOnClickListener {
            result.clear()
            result.add(currencyNum.toString())
            openInputEditor(Code.CURRENCY) }
        editPrice.setOnClickListener {
            result.clear()
            result.add(price.toString())
            openInputEditor(Code.PRICE) }
        addPhotos.setOnClickListener {
            result.clear()
            //result.add(valueDistrict.text.toString())
            openInputEditor(Code.PHOTOS) }
        addPOI.setOnClickListener {
            result.clear()
            for (poi in poiList){
                result.add(poi.toString())
            }
            openInputEditor(Code.POI) }
        editStatus.setOnClickListener {
            result.clear()
            result.add(statusNum.toString())
            openInputEditor(Code.STATUS) }
        editSaleDate.setOnClickListener {
            result.clear()
            result.add(valueSaleDate.text.toString())
            openInputEditor(Code.SALE_DATE) }
        editSoldDate.setOnClickListener {
            result.clear()
            result.add(valueSoldDate.text.toString())
            openInputEditor(Code.SOLD_DATE) }
        editAgent.setOnClickListener {
            result.clear()
            result.add(valueAgent.text.toString())
            openInputEditor(Code.AGENT) }

        updateUI()
    }

    private fun setIcon(){
        unvalidIcon = SetImageColor.changeDrawableColor(context!! , R.drawable.close, ContextCompat.getColor(context!!, R.color.colorGrey))
        validIcon = SetImageColor.changeDrawableColor(context!! , R.drawable.check_circle, ContextCompat.getColor(context!!, R.color.colorSecondary))
        editIcon = SetImageColor.changeDrawableColor(context!! , R.drawable.edit, ContextCompat.getColor(context!!, R.color.colorSecondary))
        addIcon = SetImageColor.changeDrawableColor(context!! , R.drawable.add_box, ContextCompat.getColor(context!!, R.color.colorSecondary))
        removeIcon = SetImageColor.changeDrawableColor(context!! , R.drawable.minate_box, ContextCompat.getColor(context!!, R.color.colorError))
    }

    /**
     * TO UPDATE RESTAURANT LIST
     */
    private fun updateUI() {
        poiList = ArrayList()

        if (mainActivity!!.addOrEdit != null){
            Log.d("MODE", "EDIT")
            for (c in checks){
                c.setImageDrawable(validIcon)
            }
        }else{
            Log.d("MODE", "ADD")
            for (c in checks){
                c.setImageDrawable(unvalidIcon)
            }
        }

        setCurrency(currencyNum)
        valueStatus.text = resources.getStringArray(R.array.status_ind)[statusNum]
        validate(12)
        //mainActivity!!.setLoading(false, false)
    }

    private fun validate(ind : Int){
        checks[ind].setImageDrawable(validIcon)
        checkAll()
    }

    private fun unvalidate(ind : Int){
        checks[ind].setImageDrawable(unvalidIcon)
        checkAll()
    }

    private fun checkAll(){
        for (i in oblArray){
            if(checks[i].drawable == unvalidIcon){
                mainActivity!!.enableSave=false
                mainActivity!!.changeToolBarMenu(2)
                return
            }
        }
        mainActivity!!.enableSave = true
        mainActivity!!.changeToolBarMenu(2)
    }

    fun insertEditedValue(array : ArrayList<String>){
        when(insert){
            Code.DISTRICT -> {
                valueDistrict.text = array[0]
                if(array[0].isNotEmpty()) validate(0)
                else unvalidate(0)
            }
            Code.ADDRESS -> {
                valueAddress.text = array[0]
                if(array[0].isNotEmpty()) validate(1)
                else unvalidate(1)
            }
            Code.TYPE -> {
                typeNum = array[0].toInt()
                valueType.text = resources.getStringArray(R.array.type_ind)[typeNum!!]
                if(array[0].isNotEmpty()) validate(2)
                else unvalidate(2)
            }
            Code.ROOM_NUM -> {
                valueRoomNum.text = array[0]
                if(array[0].isNotEmpty()) validate(3)
                else unvalidate(3)
            }
            Code.BED_NUM -> {
                valueBedNum.text = array[0]
                if(array[0].isNotEmpty()) validate(4)
                else unvalidate(4)
            }
            Code.BATH_NUM -> {
                valueBathNum.text = array[0]
                if(array[0].isNotEmpty()) validate(5)
                else unvalidate(5)
            }
            Code.KITCHEN_NUM -> {
                valueKitchenNum.text = array[0]
                if(array[0].isNotEmpty()) validate(6)
                else unvalidate(6)
            }
            Code.DESCRIPTION -> {
                valueDescription.text = array[0]
                if(array[0].isNotEmpty()) validate(7)
                else unvalidate(7)
            }
            Code.CURRENCY -> {
                currencyNum = array[0].toInt()
                setCurrency(currencyNum)
            }
            Code.PRICE -> {
                valuePrice.text = Utils.convertedHighPrice(array[0])
                if(array[0].isNotEmpty()) validate(9)
                else unvalidate(9)
            }
            Code.POI -> {
                var poiV = ""
                poiList.clear()
                for (i in 0 until array.size){
                    poiList.add(array[i].toInt())
                    val sent = resources.getStringArray(R.array.poi_ind)[array[i].toInt()]
                    val coma = if (i==(array.size-1)) "" else ","
                    poiV += "$sent$coma "
                }

                if(poiV.length > 16)
                    poiV = poiV.substring(0, 14) + " ..."

                valuePoi.text = poiV
                if(poiList.isNotEmpty()) validate(11)
                else unvalidate(11)
            }
            Code.STATUS -> {
                statusNum = array[0].toInt()
                valueStatus.text = resources.getStringArray(R.array.status_ind)[statusNum]
                soldDateLine.visibility = visi[statusNum]
                if(array[0].isNotEmpty()) validate(12)
                else unvalidate(12)
            }
            Code.SALE_DATE -> {
                valueSaleDate.text = array[0]
                if(array[0].isNotEmpty()) validate(13)
                else unvalidate(13)
            }
            Code.SOLD_DATE -> {
                valueSoldDate.text = array[0]
                if(array[0].isNotEmpty()) validate(14)
                else unvalidate(14)
            }
            Code.AGENT -> {
                valueAgent.text = array[0]
                if(array[0].isNotEmpty()) validate(15)
                else unvalidate(15)
            }
        }
    }

    private fun setCurrency(n : Int){
        val dl = getString(R.string.dollar_symbol) + " " + getString(R.string.dollar)
        val er = getString(R.string.euro_symbol) + " " + getString(R.string.euro)
        when(n){
            0-> valueCurrency.text = dl
            1-> valueCurrency.text = er
        }

        validate(8)
    }

    private fun openInputEditor(code : Int){
        insert = code
        val alert = ViewDialog()
        alert.showDialog(this, activity!!, code, result)
    }

    fun saveRealEstate(){
        val re = RealEstate(null,
                typeNum,
                price,
                0,
                if(valueRoomNum.text.isNotEmpty()) valueRoomNum.text.toString().toInt() else null,
                if(valueBedNum.text.isNotEmpty()) valueBedNum.text.toString().toInt() else null,
                if(valueBathNum.text.isNotEmpty()) valueBathNum.text.toString().toInt() else null,
                if(valueKitchenNum.text.isNotEmpty()) valueKitchenNum.text.toString().toInt() else null,
                valueDescription.text.toString(),
                valueAddress.text.toString(),
                statusNum,
                valueSaleDate.text.toString(),
                valueSaleDate.text.toString(),
                valueAgent.text.toString())

        mainActivity!!.database.realEstateDao().insertRealEstate(re)
        mainActivity!!.removeFragment(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mainActivity = null
    }

    companion object {
        /**
         * @param mainActivity!! MainActivity
         * @return new RestaurantsFragment()
         */
        fun newInstance(mainActivity : MainActivity): SetRealEstate {
            val fragment = SetRealEstate()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }

}