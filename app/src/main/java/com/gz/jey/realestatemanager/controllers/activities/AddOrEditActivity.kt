package com.gz.jey.realestatemanager.controllers.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.fragments.AddOrEdit
import com.gz.jey.realestatemanager.controllers.fragments.PhotosManager
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.TempRealEstate
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.models.sql.Settings
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*
import kotlin.collections.ArrayList

class AddOrEditActivity : AppCompatActivity(), OnConnectionFailedListener {

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val TAG = "AddOrEditActivity"
    // FRAGMENTS
    lateinit var addOrEdit: AddOrEdit
    lateinit var photosManager: PhotosManager

    // FOR DESIGN
    private lateinit var enableSaveIcon: Drawable
    private lateinit var disableSaveIcon: Drawable
    private var toolMenu: Menu? = null
    var fragmentContainer: FrameLayout? = null
    private var drawerLayout: DrawerLayout? = null
    var toolbar: Toolbar? = null
    private var navigationView: NavigationView? = null
    lateinit var saveItem: MenuItem
    lateinit var addPhotoItem: MenuItem
    lateinit var editPhotoItem: MenuItem
    lateinit var removePhotoItem: MenuItem
    val paramItems : ArrayList<Boolean> = arrayListOf(false, false, false, false)
    var loading: FrameLayout? = null
    private var loadingContent: TextView? = null

    // FOR DATA
    lateinit var itemViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    private var settings : Settings? = null
    private var index : Int = 0
    var tabLand: Boolean = false
    var enableSave: Boolean = false

    lateinit var mGoogleApiClient: GoogleApiClient
    var mGeoDataClient : GeoDataClient? = null
    private var mPlaceDetectionClient : PlaceDetectionClient? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    // FOR PERMISSIONS
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34
    var mLocationPermissionGranted: Boolean = false
    var mLastKnownLocation: LatLng? = null

    // FOR REALESTATE SELECTOR
    var tempRE: TempRealEstate? = null

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_or_edit)
        setLocalisationData()
        initActivity()
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        this.configureToolBar()
        tabLand = (findViewById<View>(R.id.fragment_details) != null)
        setLang()
        setIcon()
        fragmentContainer = findViewById(R.id.fragment_container)
        //saveDatas()
        this.setViewModel()
        this.addOrEdit = AddOrEdit.newInstance(this)
        this.photosManager = PhotosManager.newInstance(this)
        this.setFragment(0)
    }

    /**
     * SET LOCALISATION DATA
     */
    private fun setLocalisationData(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(applicationContext)
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(applicationContext)
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        getDeviceLocation()
    }

    private fun setLang() {
        // Data.lang = if(Locale.getDefault().displayLanguage=="fr") 1 else 0
    }

    private fun setIcon() {
        enableSaveIcon = SetImageColor.changeDrawableColor(this, R.drawable.save, ContextCompat.getColor(this, R.color.colorWhite))
        disableSaveIcon = SetImageColor.changeDrawableColor(this, R.drawable.save, ContextCompat.getColor(this, R.color.colorGrey))
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.add_edit_menu, toolMenu)
        setOptionMenu(toolMenu!!)
        return true
    }

    private fun setOptionMenu(menu : Menu){
        removePhotoItem = menu.getItem(0)
        editPhotoItem = menu.getItem(1)
        addPhotoItem = menu.getItem(2)
        saveItem = menu.getItem(3)

        for (i in 0 until 4){
            menu.getItem(i).isVisible = paramItems[i]
        }

        if (enableSave) saveItem.icon = enableSaveIcon
        else saveItem.icon = disableSaveIcon
    }

    /**
     * @param item MenuItem
     * @return boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        // Handle item selection
        return when (id) {
            R.id.remove_photo -> {
                photosManager.deletePhotos()
                true
            }
            R.id.edit_photo -> {
                photosManager.editLegends()
                true
            }
            R.id.add_photo -> {
                photosManager.addPhotos()
                true
            }
            R.id.save -> {
                if (enableSave)
                    when(index){
                        0 -> addOrEdit.saveRealEstate()
                        1 -> photosManager.savePhotos()
                    }
                else
                    ToastMessage().notifyMessage(this, Code.UNSAVABLE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * CONFIGURE TOOLBAR
     */
    private fun configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar)
        if(intent.getBooleanExtra(Code.IS_EDIT, false))
            toolbar!!.title = "Edit Real Estate"
        else
            toolbar!!.title = "Add Real Estate"
        setSupportActionBar(toolbar)
        invalidateOptionsMenu()
    }

    // -------------------
    // DATA
    // -------------------

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
        this.itemViewModel.getSettings(Code.SETTINGS).observe(this, Observer<Settings>{ s -> initSettings(s)})
    }

    private fun initSettings(set : Settings?){
        if(set != null) {
            settings = set
            itemViewModel.updateSettings(settings!!)
        }else{
            val lang = if(Locale.getDefault().language == "fr") 1 else 0
            settings = Settings(null, 0, lang, null, false, true)
            itemViewModel.createSettings(settings!!)
        }
    }



    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    fun setFragment(index: Int) {
        this.index = index
        changeToolBarMenu(0)
        //loadingContent!!.text = getString(R.string.loadingView)
        //hideKeyboard()
        // if (fromNotif) {
        //   fromNotif=false
        //  execRequest(CODE_DETAILS)
        //}else {

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)

            var fragment: Fragment? = null
            invalidateOptionsMenu()
            // Data.tab = index
            when (index) {
                0 -> {
                    changeToolBarMenu(0)
                    fragment = this.addOrEdit
                }

                1 -> {
                    changeToolBarMenu(1)
                    fragment = this.photosManager
                }
            }

            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun changeToolBarMenu(em: Int) {
        invalidateOptionsMenu()
        Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        when (em) {
            0 -> {
                paramItems[0] = false
                paramItems[1] = false
                paramItems[2] = false
                paramItems[3] = true
                toolbar!!.setNavigationOnClickListener { backToMainActivity() }
            }
            1 -> {
                paramItems[0] = false
                paramItems[1] = false
                paramItems[2] = true
                paramItems[3] = true
                toolbar!!.setNavigationOnClickListener { setFragment(0) }
            }
            2 -> {
                paramItems[0] = true
                paramItems[1] = true
                paramItems[2] = true
                paramItems[3] = true
                toolbar!!.setNavigationOnClickListener { setFragment(0) }
            }
        }
    }

    fun setSave(bool: Boolean) {
        Log.d("SET SAVE", bool.toString())
        enableSave = bool
        if(toolMenu!=null)
            setOptionMenu(toolMenu!!)
    }

    /////////////////////////////////////////
    ///// LOCATION //////////////////////////
    /////////////////////////////////////////
    /**
     * GET DEVICE LOCATION
     */
    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        mFusedLocationProviderClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastKnownLocation = (LatLng(task.result!!.latitude, task.result!!.longitude))
                        Log.d(TAG, " TASK DEVICE LOCATION SUCCESS")
                        Log.d(TAG, mLastKnownLocation.toString())

                    } else {
                        Log.e("MAP LOCATION", "Exception: %s", task.exception)
                        // Prompt the user for permission.
                        getLocationPermission()
                    }
                }
    }

    /**
     * GET LOCATION PERMISSION
     */
    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * ON REQUEST PERMISSION RESULT
     * @param requestCode Int
     * @param permissions Array<String>
     * @param grantResults IntArray
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    getDeviceLocation()
                }
            }
        }
    }
}