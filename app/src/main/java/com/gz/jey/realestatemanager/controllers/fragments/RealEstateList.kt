package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.arch.persistence.db.SimpleSQLiteQuery
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogNoResults
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.utils.BuildRequestSQL
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.RealEstateAdapter

class RealEstateList : Fragment(), RealEstateAdapter.Listener {
    private var mView: View? = null

    var mainActivity: MainActivity? = null
    private var adapter: RealEstateAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var infoText: TextView? = null
    private var itemPos: Int? = null
    private var slct: Int = 0
    private var maxClick = 1
    var position : Int? = null


    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.real_estate_list, container, false)
        mainActivity = activity as MainActivity
        maxClick = if (Data.tabMode) 1 else 2
        return mView
    }

    /**
     * CALLED WHEN VIEW CREATED
     * @param view View
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.infoText = view.findViewById(R.id.re_text)
        this.recyclerView = view.findViewById(R.id.real_estate_recycler_view)
        configureRecyclerView()
        init()
    }

    override fun onClickDeleteButton(position: Int) {
        // DELETE
    }

    private fun configureRecyclerView() {
        this.adapter = RealEstateAdapter(this)
        this.recyclerView!!.adapter = this.adapter
        this.recyclerView!!.layoutManager = LinearLayoutManager(this.context)
        ItemClickSupport.addTo(recyclerView, R.layout.real_estate_item)
                .setOnItemClickListener { _, position, _ ->
                    itemPos = position
                    this.updateRealEstate(this.adapter!!.getRealEstate(position), position, this.adapter!!.getAllRealEstate())
                }
    }

    private fun init() {
        Log.d("RE LIST", "OK")
        getRealEstates()
    }

    private fun getRealEstates() {
        if (mainActivity!!.intent.hasExtra(Code.FILTERS) && mainActivity!!.intent.getBooleanExtra(Code.FILTERS, false))
            mainActivity!!.itemViewModel.getFilters(Code.FILTERS_DATA)
                    .observe(this, Observer<Filters> { fi -> setFilters(fi!!) })
        else
            mainActivity!!.itemViewModel.getAllRealEstate()
                    .observe(this, Observer<List<RealEstate>> { re -> updateRealEstateList(re!!) })
    }

    private fun updateRealEstate(realEstate: RealEstate, position: Int, allRE: List<RealEstate>) {
        this.position = position
        updateRealEstateList(allRE)
        if (mainActivity!!.realEstate == realEstate && !Data.tabMode) {
            slct = 2
        } else {
            mainActivity!!.setRE(realEstate)
            slct++
        }
        Log.d("SLC", slct.toString() + " / " + maxClick)

        if (slct == maxClick) {
            mainActivity!!.setRE(realEstate)
            slct = 0
            if (Data.tabMode) {
                mainActivity!!.realEstateDetails!!.init()
            } else {
                this.position = null
                updateRealEstateList(allRE)
                mainActivity!!.setFragment(1)
            }
        }
    }

    private fun setFilters(fi : Filters) {
        val query : SimpleSQLiteQuery? = BuildRequestSQL().setBuild(fi)
        if(query != null){
            mainActivity!!.itemViewModel.getFilteredRealEstate(query)
                .observe(this, Observer<List<RealEstate>> { re ->
                if(re!=null && re.isNotEmpty()) {
                    if (re.size == 1 && re[0].id == null){
                        infoText!!.text = getString(R.string.filters_failed)
                        infoText!!.visibility = View.VISIBLE
                        ViewDialogNoResults().showDialog(mainActivity!!, Code.FILTERS_FAILED)
                    }else{
                        updateRealEstateList(re)
                    }
                }else {
                    infoText!!.text = getString(R.string.filters_failed)
                    infoText!!.visibility = View.VISIBLE
                    ViewDialogNoResults().showDialog(mainActivity!!, Code.FILTERS_FAILED)
                }
            })
        }else{
            mainActivity!!.intent.putExtra(Code.FILTERS, false)
            getRealEstates()
        }
    }

    private fun updateRealEstateList(items: List<RealEstate>) {

        for((i,p) in items.withIndex()){
            Log.d("LOC - $i", p.latitude.toString() + " " + p.longitude.toString())
        }

        if (items.isNotEmpty()) {
            infoText!!.visibility = View.GONE
        }else{
            infoText!!.visibility = View.VISIBLE
            infoText!!.text = getString(R.string.no_results)
            ViewDialogNoResults().showDialog(mainActivity!!, Code.NO_RESULTS)
        }
        val num = if(Data.tabMode)
            if (Utils.isLandscape(context!!)) 9 else 14
        else
            if (Utils.isLandscape(context!!)) 5 else 10

        val div = if(Data.tabMode)
            if (Utils.isLandscape(context!!)) 0.35f else 0.4f
        else
            if (Utils.isLandscape(context!!)) 0.65f else 1f

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x*div).toInt()
        val height= size.y/num

        this.adapter!!.updateData(items, position, width, height)

        if(mainActivity!!.intent.hasExtra(Code.FROM_MAP) && mainActivity!!.intent.getBooleanExtra(Code.FROM_MAP, false)){
            mainActivity!!.intent.putExtra(Code.FROM_MAP, false)
            val id = mainActivity!!.intent.getLongExtra(Code.RE_ID, 0)
            itemPos = id.toInt()-1
            this.updateRealEstate(this.adapter!!.getRealEstate(itemPos!!), itemPos!!, this.adapter!!.getAllRealEstate())
        }else{
            if(Data.tabMode && items.isNotEmpty() && this.position == null){
                itemPos = 0
                this.updateRealEstate(this.adapter!!.getRealEstate(0), 0, this.adapter!!.getAllRealEstate())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mainActivity = null
    }

    companion object {
        /**
         * @param mainActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(mainActivity: MainActivity): RealEstateList {
            val fragment = RealEstateList()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}