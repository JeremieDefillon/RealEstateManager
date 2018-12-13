package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Point
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
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.api.ApiStreams
import com.gz.jey.realestatemanager.controllers.activities.MapsActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogBigPhotos
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.BuildItems
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter

class RealEstateDetails : Fragment(), PhotosAdapter.Listener {
    override fun onClickDeleteButton(position: Int) {}

    private var mView: View? = null

    private lateinit var adapter: PhotosAdapter
    private lateinit var photos: List<Photos>

    // FOR VIEWS
    private lateinit var noRe: TextView
    private lateinit var scrv: ScrollView

    private lateinit var recyclerView: RecyclerView
    private lateinit var descriptionValue: TextView
    private lateinit var statGridLayout: GridLayout
    private lateinit var locLayout: LinearLayout
    private var dialog : ViewDialogBigPhotos? = null

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.real_estate_details, container, false)
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
        this.statGridLayout = view.findViewById(R.id.stat_grid_layout)
        this.locLayout = view.findViewById(R.id.location_layout)
        configureRecyclerView()
        init()
    }

    private fun configureRecyclerView() {
        this.adapter = PhotosAdapter(this)
        val llm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
        ItemClickSupport.addTo(recyclerView, R.layout.photos_item)
                .setOnItemClickListener { _, position, _ ->
                    this.openPhotos(position, this.adapter.getAllPhotos())
                }
    }

    fun init() {
        Log.d("RE DETAILS OK ID", Data.reID.toString())
        getRealEstate()
    }

    private fun getRealEstate() {
        if (Data.reID != null) {
            scrv.visibility = View.VISIBLE
            noRe.visibility = View.GONE
            RealEstateList.itemViewModel!!.getRealEstate(Data.reID!!)
                    .observe(this, Observer<RealEstate> { re -> updateRealEstateDetails(re!!) })
        } else {
            scrv.visibility = View.GONE
            noRe.visibility = View.VISIBLE
        }
    }

    private fun updateRealEstateDetails(item: RealEstate) {
        val second = ContextCompat.getColor(view!!.context, R.color.colorSecondary)
        val grey = ContextCompat.getColor(view!!.context, R.color.colorGrey)

        locLayout.removeAllViews()
        statGridLayout.removeAllViews()

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val ns = if (Data.tabMode && Utils.isLandscape(context!!)) (size.y * 0.2f).toInt()
        else if (Data.tabMode && !Utils.isLandscape(context!!)) (size.y * 0.15f).toInt()
        else if (!Data.tabMode && Utils.isLandscape(context!!)) (size.x * 0.2f).toInt()
        else (size.y * 0.2f).toInt()

        this.adapter.updateData(item.photos as List<Photos>, size.x)

        descriptionValue.textSize = (ns * 0.1f) / context!!.resources.displayMetrics.density
        descriptionValue.text = if (item.description.isNotEmpty()) item.description
        else getString(R.string.nc)
        descriptionValue.typeface = if (item.description.isNotEmpty()) Typeface.DEFAULT else Typeface.DEFAULT

        val loc = Utils.formatedLocation(item.streetNumber, item.street, item.zipCode, item.locality, item.state)

        val loca: View = BuildItems().statItem(context!!, ns)
        locLayout.addView(loca)
        val statIcon = loca.findViewById<ImageView>(R.id.stat_icon)
        val statLbl = loca.findViewById<TextView>(R.id.stat_label)
        val statValue = loca.findViewById<TextView>(R.id.stat_value)
        statIcon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.location, second)
        statLbl.text = getString(R.string.location)
        statValue.text = if (loc.isNotEmpty()) loc else getString(R.string.nc)

        val map: View = BuildItems().mapImage(context!!, ns)
        locLayout.addView(map)
        val mapReceiver = map.findViewById<ImageView>(R.id.map_receiver)
        val nfLocation = map.findViewById<TextView>(R.id.nf_location)

        map.setOnClickListener { openMapActivity(item.id!!) }

        if (loc.isNotEmpty()) {
            val pos = if (item.latitude != null && item.longitude != null) LatLng(item.latitude!!, item.longitude!!)
            else null
            val url = ApiStreams.getStaticMap(loc, 300, pos)
            Glide.with(context!!)
                    .load(url)
                    .into(mapReceiver)
            nfLocation.visibility = View.GONE
        }


        for (i in 0 until 10) {

            val child: View = BuildItems().statItem(context!!, ns)
            statGridLayout.addView(child)
            val statIcon = child.findViewById<ImageView>(R.id.stat_icon)
            val statLbl = child.findViewById<TextView>(R.id.stat_label)
            val statValue = child.findViewById<TextView>(R.id.stat_value)
            when (i) {
                0 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.home, second)
                    statLbl.text = getString(R.string.type)
                    statValue.text = if (item.type != null) resources.getStringArray(R.array.type_ind)[item.type!!] else getString(R.string.nc)
                }
                1 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.surface, second)
                    statLbl.text = getString(R.string.surface)
                    statValue.text = if (item.surface != null) item.surface!!.toString() + " m²" else getString(R.string.nc)
                }
                2 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.room, second)
                    statLbl.text = getString(R.string.room_number)
                    statValue.text = if (item.room != null) item.room!!.toString() else getString(R.string.nc)
                }
                3 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.bed, second)
                    statLbl.text = getString(R.string.bed_number)
                    statValue.text = if (item.bed != null) item.bed!!.toString() else getString(R.string.nc)
                }
                4 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.bath, second)
                    statLbl.text = getString(R.string.bath_number)
                    statValue.text = if (item.bath != null) item.bath!!.toString() else getString(R.string.nc)
                }
                5 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.kitchen, second)
                    statLbl.text = getString(R.string.kitchen_number)
                    statValue.text = if (item.kitchen != null) item.kitchen!!.toString() else getString(R.string.nc)
                }
                6 -> {
                    statLbl.text = getString(R.string.price)
                    val p = if(item.price != null)
                        if(Data.currency == 1) Utils.convertDollarToEuro(item.price!!) else item.price
                    else null
                    setPriceState(p, item.surface, statIcon, statValue)
                }
                7 -> {
                    val status = if (item.sold) resources.getStringArray(R.array.status_ind)[1] else resources.getStringArray(R.array.status_ind)[0]
                    statLbl.text = getString(R.string.status)
                    when (item.sold) {
                        false -> {
                            statIcon.background = SetImageColor.changeDrawableColor(view!!.context, R.drawable.check_circle, grey)
                            val sb = StringBuilder()
                                    .append(status)
                                    .append(" since \r\n")
                                    .append(item.marketDate)
                            statValue.text = sb.toString()
                        }
                        true -> {
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
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.perm_identity, second)
                    statLbl.text = getString(R.string.agent)
                    statValue.text = if (item.agentName != null) item.agentName else getString(R.string.nc)
                }
                9 -> {
                    statIcon.background = SetImageColor.changeDrawableColor(context!!, R.drawable.poi, second)
                    statLbl.text = getString(R.string.points_of_interest)
                    val poi: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false, false)
                    val sb = StringBuilder()
                    poi[0] = item.poiSchool
                    poi[1] = item.poiShops
                    poi[2] = item.poiPark
                    poi[3] = item.poiSubway
                    poi[4] = item.poiBus
                    poi[5] = item.poiTrain
                    poi[6] = item.poiHospital
                    poi[7] = item.poiAirport
                    for ((i, p) in poi.withIndex())
                        if (p)
                            sb.append(resources.getStringArray(R.array.poi_ind)[i]).append("\r\n")


                    statValue.text = sb.toString()
                }
            }
        }

        if(Data.isEdit && item.photos!=null)
            openPhotos(Data.photoNum!!, item.photos!!)
    }

    private fun setPriceState(price: Long?, surface: Int?, icon: ImageView, value: TextView) {
        val symb = if (Data.currency == 1) R.drawable.euro else R.drawable.dollar
        icon.background = SetImageColor.changeDrawableColor(activity!!, symb, ContextCompat.getColor(view!!.context, R.color.colorSecondary))
        val sb = StringBuilder()
                .append(Utils.convertedHighPrice(this.context!!, price))
                .append("\r\n")
                .append(Utils.getPPMFormat(this.context!!, price, surface))
        value.text = sb.toString()
    }

    private fun openPhotos(pos: Int, photos: List<Photos>) {
        this.dialog = ViewDialogBigPhotos()
        this.dialog!!.showDialog(activity!!, pos, photos)
    }

    private fun openMapActivity(id: Long) {
        val intent = Intent(activity, MapsActivity::class.java)
        intent.putExtra(Code.RE_ID, id)
        startActivity(intent)
        activity!!.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(this.dialog != null)
            this.dialog!!.dialog.dismiss()
    }

    companion object {
        var itemViewModel : ItemViewModel? = null

        /**
         * @param mainActivity MainActivity
         * @return new RealEstateDetails()
         */
        fun newInstance(ivm: ItemViewModel): RealEstateDetails {
            val fragment = RealEstateDetails()
            itemViewModel = ivm
            return fragment
        }
    }
}