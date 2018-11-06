package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.util.Log
import android.view.KeyEvent
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.adapters.PlacesAdapter
import com.gz.jey.realestatemanager.utils.SetImageColor
import org.json.JSONObject




class ViewDialogInputAddress {

    private val TAG = "INPUT ADDRESS"

    private lateinit var titleCanvas: TextView
    lateinit var checkAC : ImageView
    private lateinit var inputAddress: AutoCompleteTextView
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    private val list: ArrayList<String> = ArrayList()
    private lateinit var resultFullAddress: String
    private lateinit var resultPlaceId: String
    private lateinit var resultDistrict: String

    private lateinit var validIcon : Drawable
    private lateinit var unvalidIcon : Drawable
    private lateinit var placesAdapter: PlacesAdapter




    fun showDialog(gdc : GeoDataClient, activity: AddOrEditActivity, context: Context, code : Int, actualValue: ArrayList<String>) {

        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_input_localisation)

        titleCanvas = dialog.findViewById(R.id.title)
        checkAC = dialog.findViewById(R.id.check)
        inputAddress = dialog.findViewById(R.id.input_address)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        resultFullAddress = if(actualValue.size>0) actualValue[0] else ""
        resultPlaceId = if(actualValue.size>1) actualValue[1] else ""
        resultDistrict = if(actualValue.size>2) actualValue[2] else ""

        unvalidIcon = SetImageColor.changeDrawableColor(context , R.drawable.check_circle, ContextCompat.getColor(context, R.color.colorGrey))
        validIcon = SetImageColor.changeDrawableColor(context , R.drawable.check_circle, ContextCompat.getColor(context, R.color.colorSecondary))

        checkAC.background = unvalidIcon

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        val titleLbl = context.getString(R.string.address)

        val typeFilter = AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build()

        val bounds = if(activity.mLastKnownLocation!=null)
            LatLngBounds(LatLng(activity.mLastKnownLocation!!.latitude-1, activity.mLastKnownLocation!!.longitude-1), LatLng(activity.mLastKnownLocation!!.latitude+1, activity.mLastKnownLocation!!.longitude+1))
            else LatLngBounds(LatLng(-40.00, -168.00), LatLng(71.00, 136.00))

        Log.d("FILTER" , typeFilter.toString())
        Log.d("GEOD" , gdc.toString())
        Log.d("BOUNDS" , bounds.toString())
        placesAdapter = PlacesAdapter(context, activity.mGoogleApiClient, bounds, typeFilter)
        inputAddress.setAdapter(placesAdapter)

        inputAddress.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            // This is your listview's selected item
            val item = parent.getItemAtPosition(position) as AutocompletePrediction
            Log.d("DIALOG ADDRESS", item.toString())
            resultFullAddress = item.getFullText(null).toString()
            resultPlaceId = item.placeId.toString()
            resultDistrict = item.getSecondaryText(null).toString()
            checkValidate()
        }

        inputAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                checkValidate()
            }
        })

        val editable = SpannableStringBuilder(resultFullAddress)
        inputAddress.text = editable

        editBtn.setOnClickListener {
            val res : ArrayList<String> = ArrayList()
            res.add(inputAddress.text.toString())
            activity.insertEditedValue(code, res)
            dialog.dismiss()
        }
        titleCanvas.text = "$editLbl $titleLbl"
        dialog.show()
    }

    private fun checkValidate(){
        if(resultFullAddress == inputAddress.text.toString() && resultFullAddress.isNotEmpty())
            checkAC.background = validIcon
        else
            checkAC.background = unvalidIcon
    }
}