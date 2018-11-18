package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.api.ApiStreams
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogDatePicker
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputAddress
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputText
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMultiChoice
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.TempRealEstate
import com.gz.jey.realestatemanager.models.retrofit.GeoCode
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.RealEstateAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_add_or_edit.*

class AddOrEdit : Fragment(), RealEstateAdapter.Listener {
    private var mView: View? = null

    // FOR DATA
    var act: AddOrEditActivity? = null
    var disposable : Disposable? = null
    var rid : Long? = null
    var currency = 0

    // ICON
    lateinit var addIcon : Drawable
    lateinit var editIcon : Drawable
    lateinit var checkIcon : Drawable
    lateinit var uncheckIcon : Drawable
    lateinit var lookIcon : Drawable
    lateinit var validateIcon : Drawable
    lateinit var unValidateIcon : Drawable

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_or_edit, container, false)
        act = activity as AddOrEditActivity
        return mView
    }

    /**
     * CALLED WHEN VIEW CREATED
     * @param view View
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setIcon()
        getRealEstate()
    }

    override fun onClickDeleteButton(position: Int) {
        // DELETE
    }

    private fun getRealEstate() {
        if(act!!.tempRE!=null){
            Log.d("TEMP RE", act!!.tempRE!!.toString())
            editValues()
            setItems()
        }else{
            if(act!!.intent.getBooleanExtra(Code.IS_EDIT, false)){
                if(act!!.intent.hasExtra(Code.RE_ID))
                    act!!.itemViewModel.getRealEstate(act!!.intent.getLongExtra(Code.RE_ID, 0))
                            .observe(this, Observer<RealEstate> { r -> updateRealEstate(r) })
            }else
                updateRealEstate(null)
        }
    }

    private fun updateRealEstate(realEstate: RealEstate?) {
        val re = realEstate ?: RealEstate(null,"", "","","","",false,null,null,null,null,
                null,null,null,null,"",null,false,"","","",
                false,false,false,false,false,false,false,false,null)

        act!!.tempRE = TempRealEstate(re.id,re.streetNumber,re.street,re.zipCode,re.locality,re.state,re.verified,re.latitude,re.longitude,re.type,re.surface,
                re.room,re.bed,re.bath,re.kitchen,re.description,re.price,re.sold,re.marketDate,re.soldDate,re.agentName,
                re.poiSchool,re.poiShops,re.poiPark,re.poiSubway,re.poiBus,re.poiTrain,re.poiHospital,re.poiAirport,re.photos)

        editValues()
        setItems()
    }

    private fun setItems(){
        locate_search.background = lookIcon

        locate_button.setOnClickListener {
            val res: ArrayList<String?> = arrayListOf(act!!.tempRE!!.streetNumber, act!!.tempRE!!.street, act!!.tempRE!!.zipCode, act!!.tempRE!!.locality, act!!.tempRE!!.state)
            ViewDialogInputAddress().showDialog(
                    act!!.mGeoDataClient!!,
                    act!!, act!!,
                    res) }
        street_num_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.STREET_NUM, act!!.tempRE!!.streetNumber) }
        street_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.STREET, act!!.tempRE!!.street) }
        locality_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.LOCALITY, act!!.tempRE!!.locality) }
        zip_code_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.ZIP_CODE, act!!.tempRE!!.zipCode) }
        state_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.STATE, act!!.tempRE!!.state) }
        type_button.setOnClickListener {
            val res: ArrayList<String> = arrayListOf()
            if(act!!.tempRE!!.type!=null)
                res.add(act!!.tempRE!!.type.toString())
            ViewDialogMultiChoice().showDialog(act!!, Code.TYPE, res)
        }
        surface_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.SURFACE, act!!.tempRE!!.surface.toString()) }
        price_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.PRICE, act!!.tempRE!!.price.toString()) }
        poi_button.setOnClickListener {
            val res: ArrayList<String> = arrayListOf()
            if(act!!.tempRE!!.poiSchool) res.add("0")
            if(act!!.tempRE!!.poiShops) res.add("1")
            if(act!!.tempRE!!.poiPark) res.add("2")
            if(act!!.tempRE!!.poiSubway) res.add("3")
            if(act!!.tempRE!!.poiBus) res.add("4")
            if(act!!.tempRE!!.poiTrain) res.add("5")
            if(act!!.tempRE!!.poiHospital) res.add("6")
            if(act!!.tempRE!!.poiAirport) res.add("7")
            ViewDialogMultiChoice().showDialog(act!!, Code.POI, res)
        }
        total_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.ROOM_NUM, act!!.tempRE!!.room.toString()) }
        bedroom_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.BED_NUM, act!!.tempRE!!.bed.toString()) }
        bathroom_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.BATH_NUM, act!!.tempRE!!.bath.toString()) }
        kitchen_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.KITCHEN_NUM, act!!.tempRE!!.kitchen.toString()) }
        description_button.setOnClickListener { ViewDialogInputText().showDialog(act!!, Code.DESCRIPTION, act!!.tempRE!!.description) }
        photo_button.setOnClickListener {

            act!!.setFragment(1)
        }
        sold_button.setOnClickListener { checkSold() }
        market_date_button.setOnClickListener { ViewDialogDatePicker().showDialog(act!!, Code.SALE_DATE) }
        sold_date_button.setOnClickListener { ViewDialogDatePicker().showDialog(act!!, Code.SOLD_DATE) }
    }


    fun insertStandardValue(code : String, value : String){
        when(code){
            Code.STREET_NUM -> act!!.tempRE!!.streetNumber = value
            Code.STREET -> act!!.tempRE!!.street = value
            Code.ZIP_CODE -> act!!.tempRE!!.zipCode = value
            Code.LOCALITY -> act!!.tempRE!!.locality = value
            Code.STATE -> act!!.tempRE!!.state = value
            Code.TYPE -> act!!.tempRE!!.type = value.toInt()
            Code.SURFACE -> act!!.tempRE!!.surface = value.toInt()
            Code.PRICE -> act!!.tempRE!!.price = value.toLong()
            Code.POI -> {
                val v : ArrayList<Boolean> = separateValue(value)
                act!!.tempRE!!.poiSchool = v[0]
                act!!.tempRE!!.poiShops = v[1]
                act!!.tempRE!!.poiPark = v[2]
                act!!.tempRE!!.poiSubway = v[3]
                act!!.tempRE!!.poiBus = v[4]
                act!!.tempRE!!.poiTrain = v[5]
                act!!.tempRE!!.poiHospital = v[6]
                act!!.tempRE!!.poiAirport = v[7]
            }
            Code.ROOM_NUM -> act!!.tempRE!!.room = value.toInt()
            Code.BED_NUM -> act!!.tempRE!!.bed = value.toInt()
            Code.BATH_NUM -> act!!.tempRE!!.bath = value.toInt()
            Code.KITCHEN_NUM -> act!!.tempRE!!.kitchen = value.toInt()
            Code.DESCRIPTION -> act!!.tempRE!!.description = value
            Code.SALE_DATE -> act!!.tempRE!!.marketDate = value
            Code.SOLD_DATE -> act!!.tempRE!!.soldDate = value
            Code.AGENT -> act!!.tempRE!!.agentName = value
        }

        editValues()
    }

    fun insertLocation(address : String){
        disposable = ApiStreams.streamGeoCode(address)
                .subscribeWith(object : DisposableObserver<GeoCode>() {
                    override fun onNext(gc: GeoCode) {
                        setLocation(gc)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("GC RX", e.toString())
                    }

                    override fun onComplete() {}
                })
    }

    fun setLocation(gc : GeoCode){
        if(gc.results!=null && gc.results.isNotEmpty()){
            if(gc.results[0].address_components != null){
                for (ac in gc.results[0].address_components!!){
                    if(ac.types != null)
                        for(t in ac.types)
                            when(t){
                                "street_number" -> act!!.tempRE!!.streetNumber = ac.long_name ?: ""
                                "route" -> act!!.tempRE!!.street = ac.long_name ?: ""
                                "locality" -> act!!.tempRE!!.locality = ac.long_name ?: ""
                                "postal_code" -> act!!.tempRE!!.zipCode = ac.long_name ?: ""
                                "country" -> act!!.tempRE!!.state = ac.long_name ?: ""
                            }
                }
                act!!.tempRE!!.verified = true
            }else{
                act!!.tempRE!!.verified = false
            }
        }else{
            act!!.tempRE!!.verified = false
        }
        editValues()
    }

    private fun editValues(){
        street_num_value.text = act!!.tempRE!!.streetNumber
        street_value.text = act!!.tempRE!!.street
        locality_value.text = act!!.tempRE!!.locality
        zip_code_value.text = act!!.tempRE!!.zipCode
        state_value.text = act!!.tempRE!!.state
        type_value.text = if(act!!.tempRE!!.type!=null) resources.getStringArray(R.array.type_ind)[act!!.tempRE!!.type!!] else ""
        surface_value.text = if(act!!.tempRE!!.surface!=null) Utils.getSurfaceFormat(act!! ,act!!.tempRE!!.surface) else ""
        price_value.text = if(act!!.tempRE!!.price!=null) Utils.convertedHighPrice(act!!, currency, act!!.tempRE!!.price) else ""
        poi_value.text = getPoiList()
        total_value.text = if(act!!.tempRE!!.room!=null) act!!.tempRE!!.room.toString() else ""
        bedroom_value.text = if(act!!.tempRE!!.bed!=null) act!!.tempRE!!.bed.toString() else ""
        bathroom_value.text = if(act!!.tempRE!!.bath!=null) act!!.tempRE!!.bath.toString() else ""
        kitchen_value.text = if(act!!.tempRE!!.kitchen!=null) act!!.tempRE!!.kitchen.toString() else ""
        description_value.text = act!!.tempRE!!.description
        market_date_value.text = act!!.tempRE!!.marketDate
        sold_date_value.text = act!!.tempRE!!.soldDate
        agent_value.text = act!!.tempRE!!.agentName

        if(act!!.tempRE!!.photos!=null && act!!.tempRE!!.photos!!.isNotEmpty()){
            for(p in act!!.tempRE!!.photos!!)
                if(p.main)
                    Glide.with(act!!)
                            .load(p.image)
                            .into(photo_value)
        }

        checkingValidate()
    }

    fun saveRealEstate(){
        val re = RealEstate(
                act!!.tempRE!!.id,
                act!!.tempRE!!.streetNumber,
                act!!.tempRE!!.street,
                act!!.tempRE!!.zipCode,
                act!!.tempRE!!.locality,
                act!!.tempRE!!.state,
                act!!.tempRE!!.verified,
                act!!.tempRE!!.latitude,
                act!!.tempRE!!.longitude,
                act!!.tempRE!!.type,
                act!!.tempRE!!.surface,
                act!!.tempRE!!.room,
                act!!.tempRE!!.bed,
                act!!.tempRE!!.bath,
                act!!.tempRE!!.kitchen,
                act!!.tempRE!!.description,
                act!!.tempRE!!.price,
                act!!.tempRE!!.sold,
                act!!.tempRE!!.marketDate,
                act!!.tempRE!!.soldDate,
                act!!.tempRE!!.agentName,
                act!!.tempRE!!.poiSchool,
                act!!.tempRE!!.poiShops,
                act!!.tempRE!!.poiPark,
                act!!.tempRE!!.poiSubway,
                act!!.tempRE!!.poiBus,
                act!!.tempRE!!.poiTrain,
                act!!.tempRE!!.poiHospital,
                act!!.tempRE!!.poiAirport,
                act!!.tempRE!!.photos)

        if(act!!.tempRE!!.id!=null) act!!.itemViewModel.updateRealEstate(re)
        else act!!.itemViewModel.createRealEstate(re)
    }

    private fun checkSold(){
        act!!.tempRE!!.sold = !act!!.tempRE!!.sold
        checkingValidate()
    }

    private fun checkingValidate(){
        act!!.setSave((act!!.tempRE!!.street.isNotEmpty()
                && act!!.tempRE!!.locality.isNotEmpty()
                && act!!.tempRE!!.type!=null
                && act!!.tempRE!!.surface != null
                && act!!.tempRE!!.price != null
                && act!!.tempRE!!.room != null
                && act!!.tempRE!!.marketDate.isNotEmpty()
                && ((act!!.tempRE!!.sold && act!!.tempRE!!.soldDate.isNotEmpty()) || (!act!!.tempRE!!.sold))
                && (act!!.tempRE!!.photos != null && act!!.tempRE!!.photos!!.isNotEmpty())))

        sold_check.isChecked = act!!.tempRE!!.sold
        val col = if(act!!.tempRE!!.sold) ContextCompat.getColor(context!!, R.color.colorSecondaryDark) else ContextCompat.getColor(context!!, R.color.colorBlack)
        sold_date_lbl.setTextColor(col)
        locate_check.background = if(act!!.tempRE!!.verified) validateIcon else unValidateIcon

        if(act!!.tempRE!!.description.isNotEmpty()) {
            description_icon.background = editIcon
            description_value.text = act!!.tempRE!!.description
        }else{
            description_icon.background = addIcon
            description_value.text = act!!.tempRE!!.description
        }

        photo_icon.background = addIcon
        photo_value.background = null

        if(act!!.tempRE!!.photos!=null && act!!.tempRE!!.photos!!.isNotEmpty()) {
            for(p in act!!.tempRE!!.photos!!){
                if(p.main){
                    photo_icon.background = editIcon
                    Glide.with(this)
                            .load(p.image)
                            .into(photo_value)
                    break
                }
            }
        }
    }

    private fun setIcon(){
        addIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.add_box, ContextCompat.getColor(act!!, R.color.colorSecondary))
        editIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.edit, ContextCompat.getColor(act!!, R.color.colorGrey))
        checkIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.check_box, ContextCompat.getColor(act!!, R.color.colorSecondary))
        uncheckIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.uncheck_box, ContextCompat.getColor(act!!, R.color.colorGrey))
        lookIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.search, ContextCompat.getColor(act!!, R.color.colorPrimary))
        validateIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.check_circle, ContextCompat.getColor(act!!, R.color.colorSecondary))
        unValidateIcon = SetImageColor.changeDrawableColor(act!!, R.drawable.check_circle, ContextCompat.getColor(act!!, R.color.colorGrey))
    }

    private fun separateValue( s : String) : ArrayList<Boolean>{
        val ss = s.split(",")
        val bo : ArrayList<Boolean> = arrayListOf(false,false,false,false,false,false,false,false)
        for(i in ss)
            bo[i.toInt()] = true

        return bo
    }

    private fun getPoiList() : String {
        val sb = StringBuilder()
        if(act!!.tempRE!!.poiSchool) sb.append(resources.getStringArray(R.array.poi_ind)[0]+", ")
        if(act!!.tempRE!!.poiShops) sb.append(resources.getStringArray(R.array.poi_ind)[1]+", ")
        if(act!!.tempRE!!.poiPark) sb.append(resources.getStringArray(R.array.poi_ind)[2]+", ")
        if(act!!.tempRE!!.poiSubway) sb.append(resources.getStringArray(R.array.poi_ind)[3]+", ")
        if(act!!.tempRE!!.poiBus) sb.append(resources.getStringArray(R.array.poi_ind)[4]+", ")
        if(act!!.tempRE!!.poiTrain) sb.append(resources.getStringArray(R.array.poi_ind)[5]+", ")
        if(act!!.tempRE!!.poiHospital) sb.append(resources.getStringArray(R.array.poi_ind)[6]+", ")
        if(act!!.tempRE!!.poiAirport) sb.append(resources.getStringArray(R.array.poi_ind)[7]+", ")

        return if(sb.toString().isNotEmpty()) sb.substring(0, sb.toString().length-2)
        else ""
    }

    override fun onDestroy() {
        super.onDestroy()
        this.act = null
        this.disposable = null
    }

    companion object {
        /**
         * @param addOrEditActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(addOrEditActivity: AddOrEditActivity): AddOrEdit {
            val fragment = AddOrEdit()
            fragment.act = addOrEditActivity
            return fragment
        }
    }
}