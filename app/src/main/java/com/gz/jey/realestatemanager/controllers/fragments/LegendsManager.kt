package com.gz.jey.realestatemanager.controllers.fragments

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.SetImageColor
import kotlinx.android.synthetic.main.legends_manager.*

class LegendsManager : Fragment() {

    private var mView: View? = null
    private var screenX = 0
    private var screenY = 0

    // FOR DATA
    private lateinit var mListener: LegendsManagerListener

    private var counter = 0
    private var main: Int? = null

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

    /**
     * @param context Context
     * On ATTACH CONTEXT TO LISTENER
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LegendsManager.LegendsManagerListener)
            mListener = context
    }

    /**
     * INIT THE FRAGMENT
     */
    private fun init() {
        cancel_btn.setOnClickListener { mListener.setFragment(1) }
        val icon = SetImageColor.changeDrawableColor(this.context!!, R.drawable.circle_button_background, ContextCompat.getColor(this.context!!, R.color.colorSecondary))
        prev_btn.background = icon
        next_btn.background = icon

        if (Data.photoNum != null)
            counter = Data.photoNum!!
        else {
            for ((i, p) in photosList.withIndex())
                if (p.selected) {
                    counter = i
                    break
                }
        }

        if (counter >= photosList.size - 1)
            next_btn.visibility = View.GONE

        for ((i, p) in photosList.withIndex())
            if (p.main) {
                main = i
                break
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
            for ((i, p) in photosList.withIndex()) {
                p.selected = false
                if (i == photosList.size - 1) {
                    saveItem()
                    mListener.savePhotos()
                    mListener.setFragment(1)
                }
            }
        }

        check_main.setOnClickListener {
            for (p in photosList)
                p.main = false

            main = null
            if (check_main.isChecked)
                main = counter
        }

        setPic()
    }

    /**
     * TO SET THE PICTURE
     */
    private fun setPic() {
        Data.photoNum = counter
        var next: Int? = null
        var prev: Int? = null

        Log.d("PHOTOS IN LEGEND", photosList.toString())

        val cNext = counter + 1
        if (cNext < photosList.size - 1) {
            for (i in cNext until photosList.size)
                if (photosList[i].selected) {
                    next = i
                    break
                }
        }

        val cPrev = counter - 1
        if (cPrev >= 0) {
            for (i in cPrev downTo 0)
                if (photosList[i].selected) {
                    prev = i
                    break
                }
        }

        if (next != null) {
            next_btn.visibility = View.VISIBLE
            next_btn.setOnClickListener {
                saveItem()
                counter = next
                setPic()
            }
        } else {
            next_btn.visibility = View.GONE
        }

        if (prev != null) {
            prev_btn.visibility = View.VISIBLE
            prev_btn.setOnClickListener {
                saveItem()
                counter = prev
                setPic()
            }
        } else {
            prev_btn.visibility = View.GONE
        }

        if (counter < photosList.size) {
            photo.visibility = View.VISIBLE
            Glide.with(this)
                    .load(photosList[counter].image)
                    .into(photo)

            setCheckList()
        }
    }

    /**
     * TO SET CHECKLIST
     */
    private fun setCheckList() {
        legend_btn.setSelection(photosList[counter].legend)
        num_btn.setSelection(photosList[counter].num)

        check_main.isChecked = (photosList[counter].main || (counter == main))
    }

    /**
     * TO SAVE ITEM
     */
    private fun saveItem() {
        photosList[counter].legend = legend_btn.selectedItemPosition
        photosList[counter].num = num_btn.selectedItemPosition
        photosList[counter].main = (main == counter)
    }

    companion object {
        var photosList: ArrayList<Photos> = ArrayList()

        /**
         * @param pl ArrayList<Photos>
         * @return LegendsManager
         */
        fun newInstance(pl: ArrayList<Photos>): LegendsManager {
            val fragment = LegendsManager()
            photosList = pl
            return fragment
        }
    }

    /**
     * INTERFACE FOR LEGENDS MANAGER LISTENER
     */
    interface LegendsManagerListener {
        fun changeToolBarMenu(index: Int)
        fun setSave(b: Boolean)
        fun setFragment(index: Int)
        fun savePhotos()
    }
}