package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Window
import android.widget.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.utils.Location
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.views.PlacesAdapter

class ViewDialogInputAddress {
    private lateinit var titleCanvas: TextView
    private lateinit var checkAC : ImageView
    private lateinit var inputAddress: AutoCompleteTextView
    private lateinit var cancelBtn: Button
    private lateinit var editBtn: Button

    private val list: ArrayList<String> = ArrayList()
    private lateinit var result: String

    private lateinit var validIcon : Drawable
    private lateinit var unvalidIcon : Drawable

    private var mGeoDataClient : GeoDataClient? = null
    private lateinit var placesAdapter: PlacesAdapter


    fun showDialog(activity: AddOrEditActivity, context: Context, actualValue: ArrayList<String>) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_input_localisation)

        titleCanvas = dialog.findViewById(R.id.title)
        checkAC = dialog.findViewById(R.id.check)
        inputAddress = dialog.findViewById(R.id.input_address)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        editBtn = dialog.findViewById(R.id.edit_btn)
        result = if(actualValue.isNotEmpty()) actualValue[0] else ""

        unvalidIcon = SetImageColor.changeDrawableColor(context , R.drawable.check_circle, ContextCompat.getColor(context, R.color.colorGrey))
        validIcon = SetImageColor.changeDrawableColor(context , R.drawable.check_circle, ContextCompat.getColor(context, R.color.colorSecondary))

        checkAC.background = unvalidIcon

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        val titleLbl = context.getString(R.string.address)

        val typeFilter = AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build()
        mGeoDataClient = Places.getGeoDataClient(context.applicationContext)

        val bounds = if(activity.mLastKnownLocation!=null)
            LatLngBounds(LatLng(activity.mLastKnownLocation!!.latitude-0.01, activity.mLastKnownLocation!!.longitude-0.01), LatLng(activity.mLastKnownLocation!!.latitude+0.01, activity.mLastKnownLocation!!.longitude+0.01))
            else null
        placesAdapter = PlacesAdapter(context, android.R.layout.simple_list_item_1, mGeoDataClient!!, typeFilter, bounds)
        inputAddress.setAdapter(placesAdapter)

        inputAddress.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            // This is your listview's selected item
            val item = parent.getItemAtPosition(position) as AutocompletePrediction
            Log.d("DIALOG ADDRESS", item.toString())
        }

        editBtn.setOnClickListener {
            val res : ArrayList<String> = ArrayList()
            res.add(inputAddress.text.toString())
            activity.insertEditedValue(res)
            dialog.dismiss()
        }
        titleCanvas.text = "$editLbl $titleLbl"
        dialog.show()
    }
}