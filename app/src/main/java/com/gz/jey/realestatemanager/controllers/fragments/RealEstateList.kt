package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.arch.persistence.db.SimpleSQLiteQuery
import android.content.Context
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
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogNoResults
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.BuildRequestSQL
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.RealEstateAdapter

class RealEstateList : Fragment(), RealEstateAdapter.Listener {
    private var mView: View? = null

    private lateinit var mListener: RealEstateListListener
    private var adapter: RealEstateAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var infoText: TextView? = null
    var position: Int? = null
    var width: Int? = null
    var height: Int? = null

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.real_estate_list, container, false)
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RealEstateListListener)
            mListener = context
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
                    this.updateRealEstateList(position, this.adapter!!.getAllRealEstate())
                }
    }

    private fun init() {
        Log.d("RE LIST", "OK")
        val num = if (Data.tabMode)
            if (Utils.isLandscape(context!!)) 9 else 14
        else
            if (Utils.isLandscape(context!!)) 5 else 10

        val div = if (Data.tabMode)
            if (Utils.isLandscape(context!!)) 0.35f else 0.4f
        else
            if (Utils.isLandscape(context!!)) 0.65f else 1f

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = (size.x * div).toInt()
        height = size.y / num
        getRealEstates()
    }

    private fun getRealEstates() {
        if (activity!!.intent.hasExtra(Code.FILTERS) && activity!!.intent.getBooleanExtra(Code.FILTERS, false))
            itemViewModel!!.getFilters(Code.FILTERS_DATA)
                    .observe(this, Observer<Filters> { fi -> setFilters(fi!!) })
        else {
            itemViewModel!!.getAllRealEstate()
                    .observe(this, Observer<List<RealEstate>> { re -> checkIfSpecialStatement(re!!) })
        }
    }

    private fun checkIfSpecialStatement(items: List<RealEstate>){
        if (activity!!.intent.hasExtra(Code.FROM_MAP) && activity!!.intent.getBooleanExtra(Code.FROM_MAP, false)) {
            activity!!.intent.putExtra(Code.FROM_MAP, false)
            val id = activity!!.intent.getLongExtra(Code.RE_ID, 0)
            Data.reID = id
            this.position = this.getPositionByID(Data.reID!!, items)
            if (this.position != null) {
                this.updateRealEstateList(this.position!!, items)
            }
        } else {
            if (items.isNotEmpty()) {
                if (Data.reID != null) {
                    Log.d("DATA ID", Data.reID.toString())
                    this.position = this.getPositionByID(Data.reID!!, items)
                    Log.d("RE POS RE LIST", this.position.toString())
                    if (this.position == null)
                        this.position = 0
                    this.updateRealEstateList(this.position!!, items)

                } else {
                    Data.reID = items[0].id
                    this.position = 0
                    this.updateRealEstateList(this.position!!, items)
                }
            }
        }
    }

    private fun selectRealEstate(position: Int, items: List<RealEstate>) : Int{
        this.position = position
        Data.reID = items[position].id
        mListener.setRE(items[position])
        if (Data.tabMode) {
            for (r in items) {
                if (r == items[position])
                    r.selected = 2
                else
                    r.selected = 0
            }
        } else {
            for (r in items) {
                if (r == items[position])
                    r.selected += 1
                else
                    r.selected = 0
            }
        }

        return if (items[position].selected == 2) {
            items[position].selected = 1
            if (Data.tabMode) {
                2
            } else {
                this.position = null
                1
            }
        }else
            0

    }

    private fun setFilters(fi: Filters) {
        val query: SimpleSQLiteQuery? = BuildRequestSQL().setBuild(fi)
        if (query != null) {
            itemViewModel!!.getFilteredRealEstate(query)
                    .observe(this, Observer<List<RealEstate>> { re ->
                        if (re != null && re.isNotEmpty()) {
                            this.position = 0
                            updateRealEstateList(this.position!!, re)
                        } else {
                            Log.d("RE SIZE", "0")
                            infoText!!.text = getString(R.string.filters_failed)
                            infoText!!.visibility = View.VISIBLE
                            ViewDialogNoResults().showDialog(activity!!, Code.FILTERS_FAILED)
                        }
                    })
        } else {
            activity!!.intent.putExtra(Code.FILTERS, false)
            getRealEstates()
        }
    }

    private fun updateRealEstateList(position: Int, items: List<RealEstate>) {
        if (items.isNotEmpty()) {
            infoText!!.visibility = View.GONE
            val act = selectRealEstate(position, items)
            mListener.setEdit(true)
            when(act){
                0 -> {}
                1 -> {mListener.setFragment(1)}
                2 -> {mListener.initDetails()}
            }
        } else {
            infoText!!.visibility = View.VISIBLE
            infoText!!.text = getString(R.string.no_results)
            ViewDialogNoResults().showDialog(activity!!, Code.NO_RESULTS)
        }

        this.adapter!!.updateData(items, width!!, height!!)
    }

    private fun getPositionByID(id: Long, items: List<RealEstate>): Int? {
        val re = findRE(id, items)
        return items.indexOf(re)
    }

    private fun findRE(id: Long, items: List<RealEstate>) : RealEstate?{
        Log.d("ID $id", items.toString())
        return items.find { realEstate: RealEstate -> realEstate.id == id}
    }

    companion object {
        var itemViewModel: ItemViewModel? = null
        /**
         * @param ivm ItemViewMode
         * @param re RealEstate
         * @return RealEstateList()
         */
        fun newInstance(ivm: ItemViewModel, re: RealEstate?): RealEstateList {
            val fragment = RealEstateList()
            itemViewModel = ivm
            if(re!=null) {
                Data.reID = re.id
            }
            return fragment
        }
    }

    interface RealEstateListListener {
        fun initDetails()
        fun setRE(realEstate: RealEstate)
        fun setFragment(index : Int)
        fun setEdit(bool : Boolean)
    }
}