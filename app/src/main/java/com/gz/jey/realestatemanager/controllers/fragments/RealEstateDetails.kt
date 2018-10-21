package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.graphics.Typeface
import android.graphics.drawable.Drawable
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
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter
import kotlinx.android.synthetic.main.real_estate_details.*

class RealEstateDetails : Fragment(), PhotosAdapter.Listener{
    override fun onClickDeleteButton(position: Int) {

    }

    private var mView : View? = null

    var mainActivity: MainActivity? = null
    private lateinit var adapter : PhotosAdapter
    private lateinit var photos : List<Photos>

    // FOR VIEWS
    private lateinit var recyclerView : RecyclerView
    private lateinit var descriptionValue : TextView
    private lateinit var locationIcon : ImageView
    private lateinit var locationValue : TextView
    private lateinit var mapReceiver : ImageView
    private lateinit var nfLocation : TextView
    private lateinit var statGridLayout : GridLayout
    private lateinit var priceIcon : ImageView
    private lateinit var priceValue : TextView
    private lateinit var saleStatusIcon : ImageView
    private lateinit var statusText : TextView
    private lateinit var agentName : TextView

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
        this.recyclerView = view.findViewById(R.id.media_recycler_view)
        this.descriptionValue = view.findViewById(R.id.description_value)
        this.locationIcon = view.findViewById(R.id.location_icon)
        this.locationValue = view.findViewById(R.id.location_value)
        this.mapReceiver = view.findViewById(R.id.map_receiver)
        this.nfLocation = view.findViewById(R.id.nf_location)
        this.statGridLayout = view.findViewById(R.id.stat_grid_layout)
        this.priceIcon = view.findViewById(R.id.price_icon)
        this.priceValue = view.findViewById(R.id.price_value)
        this.saleStatusIcon = view.findViewById(R.id.sale_status_icon)
        this.statusText = view.findViewById(R.id.status_text)
        this.agentName = view.findViewById(R.id.agent_name)
        configureRecyclerView()
        initRealEstateDetails()
    }

    private fun configureRecyclerView() {
        this.adapter = PhotosAdapter(this)
        val llm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
    }

    private fun initRealEstateDetails(){
        Log.d("RE DETAILS", "OK")
        getRealEstate()
    }

    private fun getRealEstate() {
        mainActivity!!.realEstateViewModel.getRealEstate(mainActivity!!.realEstate!!.id!!).observe(this, Observer<RealEstate>{ re -> updateRealEstateDetails(re!!)})
    }

    private fun updateRealEstateDetails(item: RealEstate) {
        mainActivity!!.unsetRE()
        val black = ContextCompat.getColor(view!!.context, R.color.colorBlack)
        val second = ContextCompat.getColor(view!!.context, R.color.colorSecondary)
        val grey = ContextCompat.getColor(view!!.context, R.color.colorGrey)
        mainActivity!!.realEstateViewModel.getAllPhotos(item.id!!)
            .observe(this, Observer<List<Photos>>{ p -> this.photos = p!! })
        this.adapter.updateData(photos)

        descriptionValue.text = if(item.description!=null && item.description!!.isNotEmpty()) item.description
                                else getString(R.string.nc)
        descriptionValue.typeface = if(item.description!=null && item.description!!.isNotEmpty()) Typeface.DEFAULT else Typeface.DEFAULT
        val locIc = SetImageColor.changeDrawableColor(view!!.context, R.drawable.location, black)
        locationIcon.background = locIc
        locationValue.text = if(item.address!=null && item.address!!.isNotEmpty()) item.address else getString(R.string.nc)
        locationValue.typeface = if(item.address!=null && item.address!!.isNotEmpty()) Typeface.DEFAULT else Typeface.DEFAULT
        if(item.address!=null && getLocation(item.address!!)!=null){
            mapReceiver.background = getLocation(item.address!!)
            nfLocation.visibility = View.GONE
        }
        val inflater : LayoutInflater = LayoutInflater.from(view!!.context)
        for (i in 0 until 7) {
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
                    statIcon.background = SetImageColor.changeDrawableColor(child.context, R.drawable.poi, black)
                    statLbl.text = getString(R.string.points_of_interest)
                    val poi = arrayListOf<Int>(0,1,3,6)
                    val sb = StringBuilder()
                    for (p in poi)
                        sb.append(resources.getStringArray(R.array.poi_ind)[p]).append("\r\n")

                    statValue.text = sb.toString()
                }
            }
        }

        val symb = if(item.currency !=null && item.currency==1) R.drawable.euro else R.drawable.dollar
        priceIcon.background = SetImageColor.changeDrawableColor(view!!.context, symb, second)
        priceValue.text = if(item.currency !=null) Utils.convertedHighPrice(item.currency!!,item.price.toString())
                    else Utils.convertedHighPrice(0,item.price.toString())
        val status = if(item.status !=null)resources.getStringArray(R.array.status_ind)[item.status!!] else null

        if(item.status == 0) {
            sale_status_icon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.check_circle, grey)
            statusText.text = status + " since " + item.marketDate
        }else{
            sale_status_icon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.check_circle, second)
            statusText.text = status + " since " + item.soldDate
        }

        agentName.text = item.agentName

    }

    private fun getLocation(adr : String) : Drawable?{
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mainActivity = null
    }

    private fun getMainPhoto(it : RealEstate): Photos?{
        mainActivity!!.realEstateViewModel.getAllPhotos(it.id!!).observe(this, Observer<List<Photos>>{
            p -> this.photo = if(p!!.isNotEmpty()){
                Log.d("IS", "NOT NULL")
                p[0]
            }else{
                Log.d("IS", "NULL")
                null
            }
        })
        return this.photo
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