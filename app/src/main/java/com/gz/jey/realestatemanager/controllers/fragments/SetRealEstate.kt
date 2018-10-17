package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
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
import com.gz.jey.realestatemanager.models.*
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter


class SetRealEstate : Fragment(), PhotosAdapter.Listener{

    override fun onClickDeleteButton(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mView : View

    private var mainActivity : MainActivity? = null
    private lateinit var checks : ArrayList<ImageView>

    // FOR DESIGN
    private lateinit var adapter: PhotosAdapter

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
    private var visi : ArrayList<Int> = arrayListOf(View.GONE, View.VISIBLE)
    private var result : ArrayList<String> = ArrayList()

    private var price : Int? = null
    private var typeNum : Int? = null
    private var currencyNum : Int = 0
    private var statusNum : Int = 0
    private lateinit var poiList : ArrayList<PointsOfInterest>
    private lateinit var photosList : ArrayList<Photos>
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

        val llm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView = view.findViewById(R.id.photos_recycler_view)
        photosRecyclerView.layoutManager = llm

        soldDateLine = view.findViewById(R.id.sold_date_line)

        configureRecyclerView()

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

        editDistrict.setOnClickListener {clickEdit(Code.DISTRICT)}
        editAddress.setOnClickListener {clickEdit(Code.ADDRESS) }
        editType.setOnClickListener {clickEdit(Code.TYPE) }
        editRoomNum.setOnClickListener {clickEdit(Code.ROOM_NUM) }
        editBedNum.setOnClickListener {clickEdit(Code.BED_NUM) }
        editBathNum.setOnClickListener {clickEdit(Code.BATH_NUM) }
        editKitchenNum.setOnClickListener {clickEdit(Code.KITCHEN_NUM) }
        editDescription.setOnClickListener {clickEdit(Code.DESCRIPTION) }
        editCurrency.setOnClickListener {clickEdit(Code.CURRENCY) }
        editPrice.setOnClickListener {clickEdit(Code.PRICE) }
        addPhotos.setOnClickListener {clickEdit(Code.PHOTOS) }
        addPOI.setOnClickListener {clickEdit(Code.POI) }
        editStatus.setOnClickListener {clickEdit(Code.STATUS) }
        editSaleDate.setOnClickListener {clickEdit(Code.SALE_DATE) }
        editSoldDate.setOnClickListener {clickEdit(Code.SOLD_DATE) }
        editAgent.setOnClickListener {clickEdit(Code.AGENT) }

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
        photosList = ArrayList()

        if (mainActivity!!.realEstate != null){
            Log.d("MODE", "EDIT")
            getDtbValue(Code.DISTRICT)
            getDtbValue(Code.ADDRESS)
            getDtbValue(Code.TYPE)
            getDtbValue(Code.ROOM_NUM)
            getDtbValue(Code.BED_NUM)
            getDtbValue(Code.BATH_NUM)
            getDtbValue(Code.KITCHEN_NUM)
            getDtbValue(Code.DESCRIPTION)
            getDtbValue(Code.CURRENCY)
            getDtbValue(Code.PRICE)
            getDtbValue(Code.POI)
            getDtbValue(Code.STATUS)
            getDtbValue(Code.SALE_DATE)
            getDtbValue(Code.SOLD_DATE)
            getDtbValue(Code.AGENT)

            insert = null
        }else{
            Log.d("MODE", "ADD")
            for (c in checks){
                c.setImageDrawable(unvalidIcon)
            }

            result.clear()
            result.add(statusNum.toString())
            insert = Code.STATUS
            insertEditedValue(result)

            result.clear()
            result.add(Data.currency.toString())
            insert = Code.CURRENCY
            insertEditedValue(result)
        }
        //mainActivity!!.setLoading(false, false)
    }

    private fun configureRecyclerView() {
        this.adapter = PhotosAdapter(this)
        this.photosRecyclerView.adapter = this.adapter
        this.photosRecyclerView.layoutManager = LinearLayoutManager(this.context)
        ItemClickSupport.addTo(photosRecyclerView, R.layout.photos_item)
                .setOnItemClickListener { _, position, _ -> Log.d("PHOTO" , photosList[position].desc.toString())}
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
                price = array[0].toInt()
                valuePrice.text = Utils.convertedHighPrice(array[0])
                if(array[0].isNotEmpty()) validate(9)
                else unvalidate(9)
            }
            Code.POI -> {
                var poiV = ""
                poiList.clear()
                for (i in 0 until array.size){
                    val poi = PointsOfInterest(null, array[i].toInt(), null)
                    poiList.add(poi)
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

    private fun setEditedPhoto(){
        if(photosList.size>0)
            photosRecyclerView.visibility = View.VISIBLE
        else
            photosRecyclerView.visibility = View.GONE
        this.adapter.updateData(photosList as List<Photos>)
    }

    private fun clickEdit(code : Int){
        result.clear()
        when (code){
            Code.DISTRICT -> result.add(valueDistrict.text.toString())
            Code.ADDRESS -> result.add(valueAddress.text.toString())
            Code.TYPE -> result.add(valueType.text.toString())
            Code.ROOM_NUM -> result.add(valueRoomNum.text.toString())
            Code.BED_NUM -> result.add(valueBedNum.text.toString())
            Code.BATH_NUM -> result.add(valueBathNum.text.toString())
            Code.KITCHEN_NUM -> result.add(valueKitchenNum.text.toString())
            Code.DESCRIPTION -> result.add(valueDescription.text.toString())
            Code.CURRENCY -> result.add(currencyNum.toString())
            Code.PRICE -> result.add(price.toString())
            Code.PHOTOS -> ""
            Code.POI -> for (poi in poiList)
                result.add(poi.toString())

            Code.STATUS -> result.add(statusNum.toString())
            Code.SALE_DATE -> result.add(valueSaleDate.text.toString())
            Code.SOLD_DATE -> result.add(valueSoldDate.text.toString())
            Code.AGENT -> result.add(valueAgent.text.toString())
        }
        openInputEditor(code)
    }

    private fun getDtbValue(code : Int){
        insert = code
        result.clear()
        when (code){
            Code.DISTRICT -> if(mainActivity!!.realEstate!!.district!=null)result.add(mainActivity!!.realEstate!!.district.toString()) else result.add("")
            Code.ADDRESS -> if(mainActivity!!.realEstate!!.address!=null)result.add(mainActivity!!.realEstate!!.address.toString()) else result.add("")
            Code.TYPE -> if(mainActivity!!.realEstate!!.type!=null)result.add(mainActivity!!.realEstate!!.type.toString()) else result.add("")
            Code.ROOM_NUM -> if(mainActivity!!.realEstate!!.room!=null)result.add(mainActivity!!.realEstate!!.room.toString()) else result.add("")
            Code.BED_NUM -> if(mainActivity!!.realEstate!!.bed!=null)result.add(mainActivity!!.realEstate!!.bed.toString()) else result.add("")
            Code.BATH_NUM -> if(mainActivity!!.realEstate!!.bath!=null)result.add(mainActivity!!.realEstate!!.bath.toString()) else result.add("")
            Code.KITCHEN_NUM -> if(mainActivity!!.realEstate!!.kitchen!=null)result.add(mainActivity!!.realEstate!!.kitchen.toString()) else result.add("")
            Code.DESCRIPTION -> if(mainActivity!!.realEstate!!.description!=null)result.add(mainActivity!!.realEstate!!.description.toString()) else result.add("")
            Code.CURRENCY -> result.add(Data.currency.toString())
            Code.PRICE -> if(mainActivity!!.realEstate!!.price!=null)result.add(mainActivity!!.realEstate!!.price.toString()) else result.add("")
            Code.PHOTOS -> if(mainActivity!!.realEstate!!.price!=null)result.add("") else result.add("")
            Code.POI -> mainActivity!!.realEstateViewModel.getAllPOI(mainActivity!!.realEstate!!.id!!).observe(this, Observer<List<PointsOfInterest>>{
                    poi -> for (p in poi!!){
                    result.add(p.value.toString())
                }
            })
            Code.STATUS -> if(mainActivity!!.realEstate!!.status!=null)result.add(mainActivity!!.realEstate!!.status.toString()) else result.add("")
            Code.SALE_DATE -> if(mainActivity!!.realEstate!!.marketDate!=null)result.add(mainActivity!!.realEstate!!.marketDate.toString()) else result.add("")
            Code.SOLD_DATE -> if(mainActivity!!.realEstate!!.soldDate!=null)result.add(mainActivity!!.realEstate!!.soldDate.toString()) else result.add("")
            Code.AGENT -> if(mainActivity!!.realEstate!!.agentName!=null)result.add(mainActivity!!.realEstate!!.agentName.toString()) else result.add("")
        }
        insertEditedValue(result)
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

    fun savePhoto(legend : Int, photo : ByteArray){
        val photo = Photos(null, photo, legend, mainActivity!!.realEstate?.id)
        photosList.add(photo)
        Log.d("Photo" , photosList.size.toString())
        setEditedPhoto()
    }

    fun saveRealEstate(){
        val re = RealEstate(mainActivity!!.realEstate?.id,
                valueDistrict.text.toString(),
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


        if(mainActivity!!.realEstate!=null)
            mainActivity!!.realEstateViewModel.updateRealEstate(re)
        else
            mainActivity!!.realEstateViewModel.createRealEstate(re)

        for (p in poiList){
            p.reId = re.id
            mainActivity!!.realEstateViewModel.createPOI(p)
        }

        for (photos in photosList){
            photos.reId = re.id
            mainActivity!!.realEstateViewModel.createPhotos(photos)
        }

        mainActivity!!.saveRealEstate(this)
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