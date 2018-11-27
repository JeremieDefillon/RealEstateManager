package com.gz.jey.realestatemanager.controllers.fragments

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.utils.SetImageColor
import kotlinx.android.synthetic.main.legends_manager.*

class LegendsManager : Fragment() {

    private var mView: View? = null
    private var screenX = 0
    private var screenY = 0

    // FOR DATA
    var act: AddOrEditActivity? = null

    private var counter = 0
    private var main : Int? = null

    // FOR DESIGN
    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.legends_manager, container, false)
        act = activity as AddOrEditActivity
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenX = size.x
        screenY = size.y
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
        cancel_btn.setOnClickListener { act!!.setFragment(1) }
        val icon = SetImageColor.changeDrawableColor(this.context!!, R.drawable.circle_button_background, ContextCompat.getColor(this.context!!, R.color.colorSecondary))
        prev_btn.background = icon
        next_btn.background = icon

        if(counter>=act!!.photosSelected.size-1)
            next_btn.visibility = View.GONE

        for ((i,p) in act!!.photosList.withIndex()){
            if(p.main){
                main = i
                break
            }
        }

        ArrayAdapter.createFromResource(
                this.context,
                R.array.photos_ind,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            legend_btn.adapter = adapter
        }

        ArrayAdapter.createFromResource(
                this.context,
                R.array.nums_ind,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            num_btn.adapter = adapter
        }

        legend_btn.setSelection(0)
        num_btn.setSelection(0)

        finish_btn.setOnClickListener {
            saveItem()

            act!!.savePhotos()
            act!!.setFragment(1)
        }

        check_main.setOnClickListener {
            for (p in act!!.photosList)
                p.main = false

            if(check_main.isChecked)
                main = counter
        }

        setPic()
    }

    private fun setPic(){
        next_btn.setOnClickListener {
            saveItem()

            counter++
            setPic()
        }

        prev_btn.setOnClickListener {
            saveItem()

            counter--
            setPic()
        }

        if(counter<act!!.photosSelected.size-1){
            next_btn.visibility = View.VISIBLE
        }else{
            next_btn.visibility = View.GONE
        }

        if(counter!=0){
            prev_btn.visibility = View.VISIBLE
        }else{
            prev_btn.visibility = View.GONE
        }

        if(counter<act!!.photosSelected.size){
            photo.visibility = View.VISIBLE
            Glide.with(this)
                    .load(act!!.photosList[act!!.photosSelected[counter]].image)
                    .into(photo)

            setCheckList()
        }
    }

    private fun setCheckList() {
        legend_btn.setSelection(act!!.photosList[act!!.photosSelected[counter]].legend)
        num_btn.setSelection(act!!.photosList[act!!.photosSelected[counter]].num)

        check_main.isChecked = (act!!.photosList[act!!.photosSelected[counter]].main || (counter==main))
    }

    private fun saveItem(){
        act!!.photosList[act!!.photosSelected[counter]].legend = legend_btn.selectedItemPosition
        act!!.photosList[act!!.photosSelected[counter]].num = num_btn.selectedItemPosition
        act!!.photosList[act!!.photosSelected[counter]].main = (main == counter)
    }

    companion object {
        /**
         * @param addOrEditActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(addOrEditActivity: AddOrEditActivity): LegendsManager {
            val fragment = LegendsManager()
            fragment.act = addOrEditActivity
            return fragment
        }
    }
}