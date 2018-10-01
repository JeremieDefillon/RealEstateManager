package com.gz.jey.realestatemanager.controllers.fragments

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity

class InputEditor : Fragment(){

    private var mView : View? = null

    var mainActivity: MainActivity? = null

    // FOR DESIGN

    // + VALUES INPUT
    var save : Button? = null
    var title : TextView? = null
    var overview : TextView? = null
    var input : TextInputEditText? = null

    // FOR DATA

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.input_editor, container, false)
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
        save = view.findViewById(R.id.save_button)
        title = view.findViewById(R.id.title)
        overview = view.findViewById(R.id.overview)
        input =  view.findViewById(R.id.input)

        save!!.setOnClickListener{saveValue()}
    }

    private fun saveValue(){
        if(input!!.text.isNotEmpty()){
            mainActivity!!.setRealEstate!!.saveValue(input!!.text.toString())
        }else{
            Log.e("INPUT EDIT", "EMPTY")
        }
    }

    companion object {
        /**
         * @param mainActivity MainActivity
         * @return new RestaurantsFragment()
         */
        fun newInstance(mainActivity : MainActivity): InputEditor {
            val fragment = InputEditor()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}