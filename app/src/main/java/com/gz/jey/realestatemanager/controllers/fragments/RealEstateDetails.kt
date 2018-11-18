package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.api.ApiStreams
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogNoResults
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.models.sql.Settings
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter

class RealEstateDetails : Fragment(), PhotosAdapter.Listener{
    override fun onClickDeleteButton(position: Int) {

    }

    private var mView : View? = null

    var mainActivity: MainActivity? = null
    private lateinit var adapter : PhotosAdapter
    private lateinit var photos : List<Photos>

    // FOR VIEWS
    private lateinit var noRe : TextView
    private lateinit var scrv : ScrollView

    private lateinit var recyclerView : RecyclerView
    private lateinit var descriptionValue : TextView
    private lateinit var locationIcon : ImageView
    private lateinit var locationValue : TextView
    private lateinit var mapReceiver : ImageView
    private lateinit var nfLocation : TextView
    private lateinit var statGridLayout : GridLayout

    private var photo : Photos? = null

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.real_estate_details, container, false)
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
        this.photos = ArrayList()
        this.noRe = view.findViewById(R.id.no_real_estate)
        this.scrv = view.findViewById(R.id.scroll_view)
        this.recyclerView = view.findViewById(R.id.media_recycler_view)
        this.descriptionValue = view.findViewById(R.id.description_value)
        this.locationIcon = view.findViewById(R.id.location_icon)
        this.locationValue = view.findViewById(R.id.location_value)
        this.mapReceiver = view.findViewById(R.id.map_receiver)
        this.nfLocation = view.findViewById(R.id.nf_location)
        this.statGridLayout = view.findViewById(R.id.stat_grid_layout)
        configureRecyclerView()
        init()
    }

    private fun configureRecyclerView() {
        this.adapter = PhotosAdapter(this)
        val llm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
    }

    fun init(){
        Log.d("RE DETAILS", "OK")
        getRealEstate()
    }

    private fun getRealEstate() {
        if(mainActivity!!.realEstate!=null) {
            scrv.visibility = View.VISIBLE
            noRe.visibility = View.GONE
            mainActivity!!.itemViewModel.getRealEstate(mainActivity!!.realEstate!!.id!!).observe(this, Observer<RealEstate> { re -> updateRealEstateDetails(re!!) })
            mainActivity!!.realEstate = null
        }else{
            scrv.visibility = View.GONE
            noRe.visibility = View.VISIBLE
        }
    }

    private fun updateRealEstateDetails(item: RealEstate) {
        val black = ContextCompat.getColor(view!!.context, R.color.colorBlack)
        val second = ContextCompat.getColor(view!!.context, R.color.colorSecondary)
        val grey = ContextCompat.getColor(view!!.context, R.color.colorGrey)

        this.adapter.updateData(item.photos as List<Photos>)

        descriptionValue.text = if(item.description.isNotEmpty()) item.description
                                else getString(R.string.nc)
        descriptionValue.typeface = if(item.description.isNotEmpty()) Typeface.DEFAULT else Typeface.DEFAULT
        val locIc = SetImageColor.changeDrawableColor(view!!.context, R.drawable.location, black)
        locationIcon.background = locIc

        val loc = Utils.formatedLocation(item.street, item.zipCode, item.locality, item.state)

        locationValue.text = if(loc.isNotEmpty()) loc else getString(R.string.nc)

        if(loc.isNotEmpty()){
            val pos = if(item.latitude!=null && item.longitude!=null)LatLng(item.latitude!!, item.longitude!!)
            else null
            val url = ApiStreams.getStaticMap(loc, 200, pos)
            Glide.with(context!!)
                    .load(url)
                    .into(mapReceiver)
            nfLocation.visibility = View.GONE
        }
        val inflater : LayoutInflater = LayoutInflater.from(view!!.context)
        for (i in 0 until 10) {
            val child : View = inflater.inflate(R.layout.stat_item, null)
            statGridLayout.addView(child)
            val g = statGridLayout.getChildAt(i) as GridLayout
            val statIcon = g.findViewById<ImageView>(R.id.stat_icon)
            val statLbl = g.findViewById<TextView>(R.id.stat_label)
            val statValue = g.findViewById<TextView>(R.id.stat_value)

            when(i){
                0 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.home, black)
                    statLbl.text = getString(R.string.type)
                    statValue.text = if(item.type !=null)resources.getStringArray(R.array.type_ind)[item.type!!] else getString(R.string.nc)
                }
                1 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.surface, black)
                    statLbl.text = getString(R.string.surface)
                    statValue.text = if(item.surface !=null)item.surface!!.toString() + " m²" else getString(R.string.nc)
                }
                2 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.room, black)
                    statLbl.text = getString(R.string.room_number)
                    statValue.text = if(item.room !=null)item.room!!.toString() else getString(R.string.nc)
                }
                3 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.bed, black)
                    statLbl.text = getString(R.string.bed_number)
                    statValue.text = if(item.bed !=null)item.bed!!.toString() else getString(R.string.nc)
                }
                4 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.bath, black)
                    statLbl.text = getString(R.string.bath_number)
                    statValue.text = if(item.bath !=null)item.bath!!.toString() else getString(R.string.nc)
                }
                5 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.kitchen, black)
                    statLbl.text = getString(R.string.kitchen_number)
                    statValue.text = if(item.kitchen !=null)item.kitchen!!.toString() else getString(R.string.nc)
                }
                6 -> {
                    var currency = 0
                    statLbl.text = getString(R.string.price)
                    mainActivity!!.itemViewModel.getSettings(Code.SETTINGS)
                            .observe(this, Observer<Settings> { st ->
                                if(st!=null){
                                    currency = st.currency
                                    setPriceState(currency, item.price, item.surface, statIcon, statValue)
                            }else
                                    setPriceState(currency, item.price, item.surface, statIcon, statValue)
                            })

                }
                7 -> {
                    val status = if(item.sold)resources.getStringArray(R.array.status_ind)[1] else resources.getStringArray(R.array.status_ind)[0]
                    statLbl.text = getString(R.string.status)
                    when(item.sold){
                        false-> {
                            statIcon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.check_circle, grey)
                            val sb = StringBuilder()
                                    .append(status)
                                    .append(" since \r\n")
                                    .append(item.marketDate)
                            statValue.text = sb.toString()
                        }
                        true-> {
                            statIcon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.check_circle, second)
                            val sb = StringBuilder()
                                    .append(status)
                                    .append(" since \r\n")
                                    .append(item.soldDate)
                            statValue.text = sb.toString()
                        }
                    }
                }
                8 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.perm_identity, black)
                    statLbl.text = getString(R.string.agent)
                    statValue.text = if(item.agentName !=null) item.agentName else getString(R.string.nc)
                }
                9 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.poi, black)
                    statLbl.text = getString(R.string.points_of_interest)
                    val poi : ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false, false)
                    val sb = StringBuilder()
                    poi[0] = item.poiSchool
                    poi[1] = item.poiShops
                    poi[2] = item.poiPark
                    poi[3] = item.poiSubway
                    poi[4] = item.poiBus
                    poi[5] = item.poiTrain
                    poi[6] = item.poiHospital
                    poi[7] = item.poiAirport
                    for ((i,p) in poi.withIndex())
                            if(p)
                                sb.append(resources.getStringArray(R.array.poi_ind)[i]).append("\r\n")


                    statValue.text = sb.toString()
                }
            }
        }
    }

    private fun setPriceState(currency : Int, price : Long?, surface : Int?, icon : ImageView, value : TextView){
        val symb = if(currency==1) R.drawable.euro else R.drawable.dollar
        icon.background = SetImageColor.changeDrawableColor(mainActivity!!, symb, ContextCompat.getColor(view!!.context, R.color.colorSecondary))
        val sb = StringBuilder()
                .append(Utils.convertedHighPrice(this.context!!, currency,price))
                .append("\r\n")
                .append(Utils.getPPMFormat(this.context!!, currency,price, surface))
        value.text = sb.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mainActivity = null
    }

    companion object {
        /**
         * @param mainActivity MainActivity
         * @return new RealEstateDetails()
         */
        fun newInstance(mainActivity : MainActivity): RealEstateDetails {
            val fragment = RealEstateDetails()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}