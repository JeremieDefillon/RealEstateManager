package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Code.FROM_MAP, true)
        intent.putExtra(Code.RE_ID, p0!!.title.toLong())
        startActivity(intent)
        finish()
        return true
    }

    private val DEFAULT_ZOOM = 13f
    private lateinit var mMap: GoogleMap
    var toolbar: Toolbar? = null

    // FOR DATA
    lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initActivity()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)

        val locationButton= (findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0,0,50,50)

        setMap()
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        this.configureToolBar()
        this.setViewModel()
    }

    /**
     * CONFIGURE TOOLBAR
     */
    private fun configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar)
        toolbar!!.title = "Maps"
        setSupportActionBar(toolbar)
        invalidateOptionsMenu()
        Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { backToMainActivity() }
    }

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }


    // ACTIONS
    private fun setMap(){
        this.itemViewModel.getAllRealEstate()
                .observe(this, Observer<List<RealEstate>> { re -> setMarkers(re!!) })

    }

    private fun setMarkers(re: List<RealEstate>){
        val id = intent.getLongExtra(Code.RE_ID, 0)

        for (r in re){
            val thisRe = LatLng(r.latitude!!, r.longitude!!)
            var ico : Bitmap? = null
            if(r.id == id) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisRe, DEFAULT_ZOOM))
                ico = BitmapFactory.decodeResource(resources, R.drawable.home)
                ico = SetImageColor.changeBitmapColor(ico, ContextCompat.getColor(this, R.color.colorError))
            }else{
                ico = BitmapFactory.decodeResource(resources, R.drawable.home)
                ico = SetImageColor.changeBitmapColor(ico, ContextCompat.getColor(this, R.color.colorSecondaryDark))
            }
            mMap.addMarker(MarkerOptions()
                    .position(thisRe)
                    .title(r.id.toString())
                    .icon(BitmapDescriptorFactory.fromBitmap(ico)))
        }
    }


    private fun backToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
