package com.gz.jey.realestatemanager.controllers.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.utils.SetImageColor


class SetRealEstate : Fragment(){

    private var mView : View? = null

    var mainActivity: MainActivity? = null
    var checks : ArrayList<ImageView>? = null

    // FOR DESIGN
    var dollarIcon : Drawable? = null
    var euroIcon : Drawable? = null
    var unvalid : Drawable? = null
    var valid : Drawable? = null
    var edit : Drawable? = null
    var add : Drawable? = null
    var remove : Drawable? = null

    var addPhotos : Button? = null
    var removePhotos : Button? = null
    var addPOI : Button? = null
    var removePOI : Button? = null

    // + VALUES INPUT
    var photosRecyclerView : RecyclerView? = null
    var poiRecyclerView : RecyclerView? = null
    var editDistrict : Button? = null
    var editAddress : Button? = null
    var editRoomNum : Button? = null
    var editBedNum : Button? = null
    var editBathNum : Button? = null
    var editKitchenNum : Button? = null
    var editDescription : Button? = null
    var editPrice : Button? = null
    var editAgent : Button? = null
    var editType : Button? = null
    var editCurrency : Button? = null
    var editStatus : Button? = null
    var editSaleDate : Button? = null
    var editSoldDate : Button? = null

    var valueDistrict : TextView? = null
    var valueAddress : TextView? = null
    var valueRoomNum : TextView? = null
    var valueBedNum : TextView? = null
    var valueBathNum : TextView? = null
    var valueKitchenNum : TextView? = null
    var valueDescription : TextView? = null
    var valuePrice : TextView? = null
    var valueAgent : TextView? = null
    var valueType : TextView? = null
    var valueCurrency : TextView? = null
    var valueStatus : TextView? = null
    var valueSaleDate : TextView? = null
    var valueSoldDate : TextView? = null

    // FOR DATA
    var typeNum : Int? = null
    var currencyNum : Int = 0
    var poiNum : Int? = null
    var statusNum : Int? = null
    var poiList : ArrayList<Int>? = null
    var insert : String = ""

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
        mainActivity!!.changeToolBarMenu(2)
        unvalid = SetImageColor.changeDrawableColor(context!! ,R.drawable.close, ContextCompat.getColor(context!!, R.color.colorGrey))
        valid = SetImageColor.changeDrawableColor(context!! ,R.drawable.check_circle, ContextCompat.getColor(context!!, R.color.colorSecondary))
        edit = SetImageColor.changeDrawableColor(context!! ,R.drawable.edit, ContextCompat.getColor(context!!, R.color.colorSecondary))
        add = SetImageColor.changeDrawableColor(context!! ,R.drawable.add_box, ContextCompat.getColor(context!!, R.color.colorSecondary))
        remove = SetImageColor.changeDrawableColor(context!! ,R.drawable.minate_box, ContextCompat.getColor(context!!, R.color.colorError))

        photosRecyclerView = view.findViewById(R.id.photos_recycler_view)
        poiRecyclerView = view.findViewById(R.id.poi_recycler_view)

        addPhotos = view.findViewById(R.id.add_picture)
        removePhotos = view.findViewById(R.id.remove_picture)
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
        valueStatus = view.findViewById(R.id.value_status)
        valueSaleDate = view.findViewById(R.id.value_sale_date)
        valueSoldDate = view.findViewById(R.id.value_sold_date)
        valueAgent = view.findViewById(R.id.value_agent)

        dollarIcon = SetImageColor.changeDrawableColor(context!! ,R.drawable.dollar, ContextCompat.getColor(context!!, R.color.colorSecondary))
        euroIcon = SetImageColor.changeDrawableColor(context!! ,R.drawable.euro, ContextCompat.getColor(context!!, R.color.colorSecondary))

        addPhotos!!.background = add
        addPOI!!.background = add
        editDistrict!!.background = edit
        editAddress!!.background = edit
        editType!!.background = edit
        editRoomNum!!.background = edit
        editBedNum!!.background = edit
        editBathNum!!.background = edit
        editKitchenNum!!.background = edit
        editDescription!!.background = edit
        editPrice!!.background = edit
        editCurrency!!.background = edit
        photosRecyclerView!!.background = edit
        editStatus!!.background = edit
        editSaleDate!!.background = edit
        editSoldDate!!.background = edit
        editAgent!!.background = edit

        checks = ArrayList()
        checks!!.add(view.findViewById(R.id.check_0))
        checks!!.add(view.findViewById(R.id.check_1))
        checks!!.add(view.findViewById(R.id.check_2))
        checks!!.add(view.findViewById(R.id.check_3))
        checks!!.add(view.findViewById(R.id.check_4))
        checks!!.add(view.findViewById(R.id.check_5))
        checks!!.add(view.findViewById(R.id.check_6))
        checks!!.add(view.findViewById(R.id.check_7))
        checks!!.add(view.findViewById(R.id.check_8))
        checks!!.add(view.findViewById(R.id.check_9))
        checks!!.add(view.findViewById(R.id.check_10))
        checks!!.add(view.findViewById(R.id.check_11))
        checks!!.add(view.findViewById(R.id.check_12))
        checks!!.add(view.findViewById(R.id.check_13))
        checks!!.add(view.findViewById(R.id.check_14))
        checks!!.add(view.findViewById(R.id.check_15))

        addPOI!!.setOnClickListener { showPopupMenu(2) }
        //removePOI!!.setOnClickListener { removePOI(poiNum) }

        editDistrict!!.setOnClickListener {
            insert="DISTRICT"
            mainActivity!!.openInputEditor()
        }
        editAddress!!.setOnClickListener {  }
        editType!!.setOnClickListener { showPopupMenu(0) }
        editRoomNum!!.setOnClickListener {  }
        editBedNum!!.setOnClickListener {  }
        editBathNum!!.setOnClickListener {  }
        editKitchenNum!!.setOnClickListener {  }
        editDescription!!.setOnClickListener {  }
        editCurrency!!.setOnClickListener { showPopupMenu(1) }
        editPrice!!.setOnClickListener {  }
        editStatus!!.setOnClickListener { showPopupMenu(3) }
        editSaleDate!!.setOnClickListener { pickUpDate() }
        editSoldDate!!.setOnClickListener { pickUpDate() }
        editAgent!!.setOnClickListener {  }

        updateUI()
    }

    /**
     * TO UPDATE RESTAURANT LIST
     */
    private fun updateUI() {
        poiList = ArrayList()

        if (mainActivity!!.addOrEdit != null){
            Log.d("MODE", "EDIT")
            for (c in checks!!){
                c.setImageDrawable(valid)
            }
        }else{
            Log.d("MODE", "ADD")
            for (c in checks!!){
                c.setImageDrawable(unvalid)
            }
        }

        setCurrency(currencyNum)
        //mainActivity!!.setLoading(false, false)
    }

    private fun validate(ind : Int){
        checks!![ind].setImageDrawable(valid)
    }

    private fun unvalidate(ind : Int){
        checks!![ind].setImageDrawable(unvalid)
    }

    private fun showPopupMenu(men : Int) {
        Log.d("showPopupMenu()", "Method was called.")
        var popupMenu : PopupMenu? = null
        val list : ArrayList<String> = ArrayList()
        when(men){
            0->{popupMenu = PopupMenu(context!!, editType)
                list.addAll(resources.getStringArray(R.array.type_ind))}
            1->{popupMenu = PopupMenu(context!!, editCurrency)
                list.addAll(resources.getStringArray(R.array.currency_ind))}
            2->{popupMenu = PopupMenu(context!!, addPOI)
                list.addAll(resources.getStringArray(R.array.poi_ind))}
            3->{popupMenu = PopupMenu(context!!, editStatus)
                list.addAll(resources.getStringArray(R.array.status_ind))}
        }

        popupMenu!!.inflate(R.menu.select_menu)

        for((index, s) in list.withIndex()){
            popupMenu.menu.add(Menu.NONE, index, index, s)
        }

        popupMenu.setOnMenuItemClickListener{ item: MenuItem? ->
            applySelection(men, item!!.itemId)
            true
        }

        if(men==1) {
            popupMenu.menu.findItem(0).icon = dollarIcon
            popupMenu.menu.findItem(1).icon = euroIcon

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
            } catch (e: Exception){
                Log.e("Main", "Error showing menu icons.", e)
            } finally {
                popupMenu.show()
            }
        }else
            popupMenu.show()


    }

    private fun pickUpDate(){

    }

    private fun applySelection(men: Int, ind: Int){
        when(men){
            0->{typeNum = ind
                valueType!!.text = resources.getStringArray(R.array.type_ind)[ind]
                validate(2)
            }
            1->{currencyNum = ind
                setCurrency(ind)
            }
            2->{poiNum = ind
                poiList!!.add(ind)
                validate(11)
            }
            3->{statusNum = ind
                valueStatus!!.text = resources.getStringArray(R.array.status_ind)[ind]
                validate(12)
            }
        }
    }

    private fun removePOI(ind : Int?){
        if(ind==null){
            poiNum = null
            Log.e("REMOVE POI", "FAILED CAUSE NULL")
        }else{
            poiNum = null
            poiList!!.remove(ind)
            val msg =  resources.getStringArray(R.array.poi_ind)[ind]
            Log.d("REMOVE POI", msg)
        }

        if(poiList!!.size==0)
            unvalidate(11)
        else
            validate(11)
    }

    private fun setCurrency(n : Int){
        val dl = getString(R.string.dollar_symbol) + " " + getString(R.string.dollar)
        val er = getString(R.string.euro_symbol) + " " + getString(R.string.euro)
        when(n){
            0-> valueCurrency!!.text = dl
            1-> valueCurrency!!.text = er
        }

        validate(8)
    }

    fun saveValue(e :  String){
        when(insert){
            "DISTRICT" -> valueType!!.text = e
            "ADDRESS" -> valueAddress!!.text = e
            "ADDRESS" -> valueAddress!!.text = e
            "ADDRESS" -> valueAddress!!.text = e
            "ADDRESS" -> valueAddress!!.text = e
            "ADDRESS" -> valueAddress!!.text = e
        }
    }

    companion object {
        /**
         * @param mainActivity MainActivity
         * @return new RestaurantsFragment()
         */
        fun newInstance(mainActivity : MainActivity): SetRealEstate {
            val fragment = SetRealEstate()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }

}