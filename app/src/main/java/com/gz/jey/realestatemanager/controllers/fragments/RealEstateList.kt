package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.views.RealEstateAdapter

class RealEstateList : Fragment(), RealEstateAdapter.Listener{


    private var mView : View? = null

    var mainActivity: MainActivity? = null
    private var adapter: RealEstateAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var infoText : TextView? = null

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
        initRealEstateList()
    }

    override fun onClickDeleteButton(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun configureRecyclerView() {
        this.adapter = RealEstateAdapter(this)
        this.recyclerView!!.adapter = this.adapter
        this.recyclerView!!.layoutManager = LinearLayoutManager(this.context)
        ItemClickSupport.addTo(recyclerView, R.layout.real_estate_item)
                .setOnItemClickListener { _, position, _ -> this.updateRealEstate(this.adapter!!.getRealEstate(position), this.adapter!!.getAllRealEstate())}
    }

    fun initRealEstateList(){
        Log.d("RE LIST", "OK")
        getRealEstates()
    }

    private fun getRealEstates() {
        mainActivity!!.realEstateViewModel.getAllRealEstate().observe(this, Observer<List<RealEstate>>{ re -> updateRealEstateList(re!!)})
    }

    private fun updateRealEstate(realEstate: RealEstate, realEstates: List<RealEstate>) {
        for (r in realEstates){
            if(r == realEstate)
                r.isSelected = !realEstate.isSelected
            else
                r.isSelected = false
            mainActivity!!.realEstateViewModel.updateRealEstate(r)
        }

        if(realEstate.isSelected){
            mainActivity!!.setRE(realEstate)
        }else{
            mainActivity!!.unsetRE()
        }
        mainActivity!!.realEstateViewModel.updateRealEstate(realEstate)
    }

    private fun updateRealEstateList(items: List<RealEstate>) {
        Log.d("RE LIST", "CALLED")
        if(items.isEmpty())
            infoText!!.text = "No Real Estate found !"
        else
            infoText!!.visibility = View.GONE

        val photos : ArrayList<Photos?> = ArrayList()
        for (i in items){
            mainActivity!!.realEstateViewModel.getAllPhotos(i.id!!).observe(this, Observer<List<Photos>>{
                p -> if (p!!.isNotEmpty()) photos.add(p[0]) else photos.add(null)
            })
        }
        this.adapter!!.updateData(items, photos as List<Photos>)
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
        fun newInstance(mainActivity : MainActivity): RealEstateList {
            val fragment = RealEstateList()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}