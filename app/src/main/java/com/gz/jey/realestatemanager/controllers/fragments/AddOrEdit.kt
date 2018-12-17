package com.gz.jey.realestatemanager.controllers.fragments

import android.content.Context
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
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogDatePicker
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputText
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMultiChoice
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
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
    private lateinit var mListener: AddOrEditListener
    private var disposable: Disposable? = null

    // ICON
    private lateinit var addIcon: Drawable
    private lateinit var checkIcon: Drawable
    private lateinit var uncheckIcon: Drawable
    private lateinit var lookIcon: Drawable
    private lateinit var validateIcon: Drawable
    private lateinit var unValidateIcon: Drawable

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_or_edit, container, false)
        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(TEMP_RE))
            tempRE = savedInstanceState.getParcelable(TEMP_RE)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AddOrEditListener)
            mListener = context
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
        editValues()
        setItems()
    }

    override fun onClickDeleteButton(position: Int) {
        // DELETE
    }

    private fun setItems() {
        locate_btn.setCompoundDrawables(lookIcon, null, unValidateIcon, null)

        locate_btn.setOnClickListener {
            val res: ArrayList<String?> = arrayListOf(tempRE!!.streetNumber, tempRE!!.street, tempRE!!.zipCode, tempRE!!.locality, tempRE!!.state)
            mListener.openAddressInput(res)
        }
        street_num_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.STREET_NUM, tempRE!!.streetNumber) }
        street_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.STREET, tempRE!!.street) }
        locality_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.LOCALITY, tempRE!!.locality) }
        zip_code_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.ZIP_CODE, tempRE!!.zipCode) }
        state_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.STATE, tempRE!!.state) }
        type_button.setOnClickListener {
            val res: ArrayList<String> = arrayListOf()
            if (tempRE!!.type != null)
                res.add(tempRE!!.type.toString())
            ViewDialogMultiChoice().showDialog(activity!!, Code.TYPE, res)
        }
        surface_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.SURFACE, tempRE!!.surface.toString()) }
        val p = if (tempRE!!.price != null)
            if (Data.currency == 1) Utils.convertDollarToEuro(tempRE!!.price!!) else tempRE!!.price
        else null
        price_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.PRICE, p.toString()) }
        poi_button.setOnClickListener {
            val res: ArrayList<String> = arrayListOf()
            if (tempRE!!.poiSchool) res.add("0")
            if (tempRE!!.poiShops) res.add("1")
            if (tempRE!!.poiPark) res.add("2")
            if (tempRE!!.poiSubway) res.add("3")
            if (tempRE!!.poiBus) res.add("4")
            if (tempRE!!.poiTrain) res.add("5")
            if (tempRE!!.poiHospital) res.add("6")
            if (tempRE!!.poiAirport) res.add("7")
            ViewDialogMultiChoice().showDialog(activity!!, Code.POI, res)
        }
        total_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.ROOM_NUM, tempRE!!.room.toString()) }
        bedroom_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.BED_NUM, tempRE!!.bed.toString()) }
        bathroom_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.BATH_NUM, tempRE!!.bath.toString()) }
        kitchen_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.KITCHEN_NUM, tempRE!!.kitchen.toString()) }
        description_button.setOnClickListener { ViewDialogInputText().showDialog(activity!!, Code.DESCRIPTION, tempRE!!.description) }
        photo_button.setOnClickListener { mListener.setFragment(1)}
        photo_icon.background = addIcon
        sold_button.setOnClickListener { checkSold() }
        market_date_button.setOnClickListener { ViewDialogDatePicker().showDialog(activity!!, Code.SALE_DATE) }
        sold_date_button.setOnClickListener { ViewDialogDatePicker().showDialog(activity!!, Code.SOLD_DATE) }
    }

    fun insertStandardValue(code: String, value: String) {

        when (code) {
            Code.STREET_NUM -> tempRE!!.streetNumber = value
            Code.STREET -> tempRE!!.street = value
            Code.ZIP_CODE -> tempRE!!.zipCode = value
            Code.LOCALITY -> tempRE!!.locality = value
            Code.STATE -> tempRE!!.state = value
            Code.TYPE -> tempRE!!.type = value.toInt()
            Code.SURFACE -> tempRE!!.surface = value.toInt()
            Code.PRICE -> {
                tempRE!!.price = if (value.isNotEmpty())
                    if (Data.currency == 1) Utils.convertEuroToDollar(value.toLong()) else value.toLong()
                else null
            }
            Code.POI -> {
                val v: ArrayList<Boolean> = separateValue(value)
                tempRE!!.poiSchool = v[0]
                tempRE!!.poiShops = v[1]
                tempRE!!.poiPark = v[2]
                tempRE!!.poiSubway = v[3]
                tempRE!!.poiBus = v[4]
                tempRE!!.poiTrain = v[5]
                tempRE!!.poiHospital = v[6]
                tempRE!!.poiAirport = v[7]
            }
            Code.ROOM_NUM -> tempRE!!.room = value.toInt()
            Code.BED_NUM -> tempRE!!.bed = value.toInt()
            Code.BATH_NUM -> tempRE!!.bath = value.toInt()
            Code.KITCHEN_NUM -> tempRE!!.kitchen = value.toInt()
            Code.DESCRIPTION -> tempRE!!.description = value
            Code.SALE_DATE -> tempRE!!.marketDate = value
            Code.SOLD_DATE -> tempRE!!.soldDate = value
            Code.AGENT -> tempRE!!.agentName = value
        }

        editValues()
    }

    fun insertLocation(address: String) {
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

    fun setLocation(gc: GeoCode) {
        if (gc.results != null &&
                gc.results.isNotEmpty() &&
                gc.results[0].geometry != null &&
                gc.results[0].geometry!!.location != null &&
                gc.results[0].address_components != null) {

            val lat = gc.results[0].geometry!!.location!!.lat
            val lng = gc.results[0].geometry!!.location!!.lng
            if (lat != null && lng != null) {
                tempRE!!.latitude = lat
                tempRE!!.longitude = lng
            }

            for (ac in gc.results[0].address_components!!) {
                if (ac.types != null)
                    for (t in ac.types)
                        when (t) {
                            "street_number" -> tempRE!!.streetNumber = ac.long_name ?: ""
                            "route" -> tempRE!!.street = ac.long_name ?: ""
                            "locality" -> tempRE!!.locality = ac.long_name ?: ""
                            "postal_code" -> tempRE!!.zipCode = ac.long_name ?: ""
                            "country" -> tempRE!!.state = ac.long_name ?: ""
                        }
            }
            tempRE!!.verified = true
        } else
            tempRE!!.verified = false

        editValues()
    }

    private fun editValues() {

        if (tempRE != null) {
            Log.d("TEMP RE", "NOT NUL")
            if (tempRE!!.streetNumber.isNotEmpty()) {
                Log.d("STR NUM", tempRE!!.streetNumber)
            } else {
                Log.d("STR NUM", "EMPTY")
            }
        } else {
            Log.d("TEMP RE", "NUL")
        }
        street_num_value.text = tempRE!!.streetNumber
        street_value.text = tempRE!!.street
        locality_value.text = tempRE!!.locality
        zip_code_value.text = tempRE!!.zipCode
        state_value.text = tempRE!!.state
        type_value.text = if (tempRE!!.type != null) resources.getStringArray(R.array.type_ind)[tempRE!!.type!!] else ""
        surface_value.text = if (tempRE!!.surface != null) Utils.getSurfaceFormat(activity!!, tempRE!!.surface) else ""
        val p = if (tempRE!!.price != null)
            if (Data.currency == 1) Utils.convertDollarToEuro(tempRE!!.price!!) else tempRE!!.price
        else null
        price_value.text = if (tempRE!!.price != null) Utils.convertedHighPrice(this.context!!, p) else ""
        poi_value.text = getPoiList()
        total_value.text = if (tempRE!!.room != null) tempRE!!.room.toString() else ""
        bedroom_value.text = if (tempRE!!.bed != null) tempRE!!.bed.toString() else ""
        bathroom_value.text = if (tempRE!!.bath != null) tempRE!!.bath.toString() else ""
        kitchen_value.text = if (tempRE!!.kitchen != null) tempRE!!.kitchen.toString() else ""
        description_value.text = tempRE!!.description
        market_date_value.text = if(Data.lang==1)Utils.getDateFr(tempRE!!.marketDate) else Utils.getDateEn(tempRE!!.marketDate)
        sold_date_value.text = if(Data.lang==1)Utils.getDateFr(tempRE!!.soldDate) else Utils.getDateEn(tempRE!!.soldDate)
        agent_value.text = tempRE!!.agentName

        checkingValidate()
    }

    fun saveRealEstate() {
        val re = RealEstate(
                tempRE!!.id,
                tempRE!!.streetNumber,
                tempRE!!.street,
                tempRE!!.zipCode,
                tempRE!!.locality,
                tempRE!!.state,
                tempRE!!.verified,
                tempRE!!.latitude,
                tempRE!!.longitude,
                tempRE!!.type,
                tempRE!!.surface,
                tempRE!!.room,
                tempRE!!.bed,
                tempRE!!.bath,
                tempRE!!.kitchen,
                tempRE!!.description,
                tempRE!!.price,
                tempRE!!.sold,
                tempRE!!.marketDate,
                tempRE!!.soldDate,
                tempRE!!.agentName,
                tempRE!!.poiSchool,
                tempRE!!.poiShops,
                tempRE!!.poiPark,
                tempRE!!.poiSubway,
                tempRE!!.poiBus,
                tempRE!!.poiTrain,
                tempRE!!.poiHospital,
                tempRE!!.poiAirport,
                tempRE!!.photos)

        if (tempRE!!.id != null) itemViewModel!!.updateRealEstate(re)
        else itemViewModel!!.createRealEstate(re)

        mListener.savedRe(re)
    }

    private fun checkSold() {
        tempRE!!.sold = !tempRE!!.sold
        checkingValidate()
    }

    private fun checkingValidate() {
        mListener.setSave((tempRE!!.street.isNotEmpty()
                && tempRE!!.locality.isNotEmpty()
                && tempRE!!.type != null
                && tempRE!!.surface != null
                && tempRE!!.price != null
                && tempRE!!.room != null
                && tempRE!!.marketDate.isNotEmpty()
                && ((tempRE!!.sold && tempRE!!.soldDate.isNotEmpty()) || (!tempRE!!.sold))
                && (tempRE!!.photos != null && tempRE!!.photos!!.isNotEmpty())))

        sold_check.isChecked = tempRE!!.sold
        val col = if (tempRE!!.sold) ContextCompat.getColor(context!!, R.color.colorSecondaryDark) else ContextCompat.getColor(context!!, R.color.colorBlack)
        sold_date_lbl.setTextColor(col)
        if (tempRE!!.verified)
            locate_btn.setCompoundDrawables(lookIcon, null, validateIcon, null)
        else
            locate_btn.setCompoundDrawables(lookIcon, null, unValidateIcon, null)

        if (tempRE!!.description.isNotEmpty()) {
            description_icon.visibility = View.GONE
            description_value.text = tempRE!!.description
        } else {
            description_icon.visibility = View.VISIBLE
            description_icon.background = addIcon
            description_value.text = tempRE!!.description
        }
        if (tempRE!!.photos != null && tempRE!!.photos!!.isNotEmpty()) {
            photo_icon.visibility = View.VISIBLE
            photo_info.visibility = View.VISIBLE
            photo_value.background = null
            for (p in tempRE!!.photos!!) {
                if (p.main) {
                    photo_icon.visibility = View.GONE
                    photo_info.visibility = View.GONE
                    glideThis(p.image!!)
                    break
                }
            }
        } else {
            photo_info.visibility = View.VISIBLE
            photo_icon.visibility = View.VISIBLE
            photo_value.background = null
        }
    }

    private fun setIcon() {
        addIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.add_box, ContextCompat.getColor(activity!!, R.color.colorSecondary))
        checkIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.check_box, ContextCompat.getColor(activity!!, R.color.colorSecondary))
        uncheckIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.uncheck_box, ContextCompat.getColor(activity!!, R.color.colorGrey))
        lookIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.search, ContextCompat.getColor(activity!!, R.color.colorPrimary))
        validateIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.check_circle, ContextCompat.getColor(activity!!, R.color.colorSecondary))
        unValidateIcon = SetImageColor.changeDrawableColor(activity!!, R.drawable.check_circle, ContextCompat.getColor(activity!!, R.color.colorGrey))
    }

    private fun glideThis(ph: String) {
        Glide.with(this)
                .load(ph)
                .into(photo_value)
    }

    private fun separateValue(s: String): ArrayList<Boolean> {
        val ss = s.split(",")
        val bo: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false, false)
        for (i in ss)
            bo[i.toInt()] = true

        return bo
    }

    private fun getPoiList(): String {
        val sb = StringBuilder()
        if (tempRE!!.poiSchool) sb.append(resources.getStringArray(R.array.poi_ind)[0] + "\r\n")
        if (tempRE!!.poiShops) sb.append(resources.getStringArray(R.array.poi_ind)[1] + "\r\n")
        if (tempRE!!.poiPark) sb.append(resources.getStringArray(R.array.poi_ind)[2] + "\r\n")
        if (tempRE!!.poiSubway) sb.append(resources.getStringArray(R.array.poi_ind)[3] + "\r\n")
        if (tempRE!!.poiBus) sb.append(resources.getStringArray(R.array.poi_ind)[4] + "\r\n")
        if (tempRE!!.poiTrain) sb.append(resources.getStringArray(R.array.poi_ind)[5] + "\r\n")
        if (tempRE!!.poiHospital) sb.append(resources.getStringArray(R.array.poi_ind)[6] + "\r\n")
        if (tempRE!!.poiAirport) sb.append(resources.getStringArray(R.array.poi_ind)[7] + "\r\n")

        return if (sb.toString().isNotEmpty()) sb.substring(0, sb.toString().length - 2)
        else ""
    }

    override fun onDestroy() {
        super.onDestroy()
        this.disposable = null
    }

    companion object {
        const val TEMP_RE = "TEMP_RE"
        var tempRE: TempRealEstate? = null
        var itemViewModel: ItemViewModel? = null
        /**
         * @param addOrEditActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(tempRealEstate: TempRealEstate, vm: ItemViewModel): AddOrEdit {
            val fragment = AddOrEdit()
            val bundle = Bundle()
            bundle.putParcelable(TEMP_RE, tempRealEstate)
            fragment.arguments = bundle
            tempRE = tempRealEstate
            itemViewModel = vm
            return fragment
        }
    }

    interface AddOrEditListener {
        fun openAddressInput(res : ArrayList<String?>)
        fun setFragment(index : Int)
        fun savedRe(re : RealEstate)
        fun setSave(b : Boolean)
    }
}