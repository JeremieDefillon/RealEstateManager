package com.gz.jey.realestatemanager.controllers.activities

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputAddress
import com.gz.jey.realestatemanager.controllers.fragments.AddOrEdit
import com.gz.jey.realestatemanager.controllers.fragments.LegendsManager
import com.gz.jey.realestatemanager.controllers.fragments.PhotosManager
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.TempRealEstate
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*
import kotlin.collections.ArrayList

class AddOrEditActivity : AppCompatActivity(), OnConnectionFailedListener, LocationListener,
        AddOrEdit.AddOrEditListener, PhotosManager.PhotosManagerListener, LegendsManager.LegendsManagerListener {

    override fun openAddressInput(res: ArrayList<String?>) {
        ViewDialogInputAddress().showDialog(mGeoDataClient!!, this, res)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val TAG = "AddOrEditActivity"
        const val RE_TEMP = "RE_TEMP"
        const val REAL_ESTATE = "REAL_ESTATE"
        const val PHOTOS_LIST = "PHOTOS_LIST"
    }
    // FRAGMENTS
    lateinit var addOrEdit: AddOrEdit
    lateinit var photosManager: PhotosManager
    lateinit var legendsManager: LegendsManager

    // FOR DESIGN
    private lateinit var enableSaveIcon: Drawable
    private lateinit var disableSaveIcon: Drawable
    private var toolMenu: Menu? = null
    var fragmentContainer: FrameLayout? = null
    var toolbar: Toolbar? = null
    lateinit var saveItem: MenuItem
    lateinit var addPhotoItem: MenuItem
    lateinit var editPhotoItem: MenuItem
    lateinit var removePhotoItem: MenuItem
    val paramItems: ArrayList<Boolean> = arrayListOf(false, false, false, false)
    var loading: FrameLayout? = null

    // FOR DATA
    lateinit var itemViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    var tabLand: Boolean = false
    var enableSave: Boolean = false
    var errorLoc: Boolean = false

    var photosList: ArrayList<Photos> = ArrayList()

    lateinit var mGoogleApiClient: GoogleApiClient
    var mGeoDataClient: GeoDataClient? = null
    private var mPlaceDetectionClient: PlaceDetectionClient? = null
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
        Data.lang = if(Locale.getDefault().language =="fr") 1 else 0
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                Data.fragNum = getInt(Code.FRAG_NUM)
                Data.photoNum = getInt(Code.PHOTO_NUM)
                Data.reID = getLong(Code.RE_ID)
                Log.d("RE ID", Data.reID.toString())
                Data.isEdit = getBoolean(Code.IS_EDIT)
                photosList = getParcelableArrayList<Photos>(PHOTOS_LIST)
            }
        } else {
            Data.isEdit = intent.getBooleanExtra(Code.IS_EDIT, false)
            Data.reID = intent.getLongExtra(Code.RE_ID, 0)
            Data.photoNum = null
            Log.d("RE ID", Data.reID.toString())
            setLocalisationData()
        }
        initActivity()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(Code.FRAG_NUM, Data.fragNum)
        if(Data.photoNum!=null)
            outState?.putInt(Code.PHOTO_NUM, Data.photoNum!!)
        outState?.putLong(Code.RE_ID, Data.reID!!)
        outState?.putBoolean(Code.IS_EDIT, Data.isEdit)
        outState?.putParcelableArrayList(PHOTOS_LIST, photosList)
        super.onSaveInstanceState(outState)
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        this.configureToolBar()
        tabLand = (findViewById<View>(R.id.fragment_details) != null)
        setIcon()
        fragmentContainer = findViewById(R.id.fragment_container)
        //saveDatas()
        this.setViewModel()
        this.getRealEstate()
    }

    /**
     * SET LOCALISATION DATA
     */
    private fun setLocalisationData() {
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

    private fun setOptionMenu(menu: Menu) {
        removePhotoItem = menu.getItem(0)
        editPhotoItem = menu.getItem(1)
        addPhotoItem = menu.getItem(2)
        saveItem = menu.getItem(3)

        for (i in 0 until 4) {
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
                photosList = photosManager.photosList
                setFragment(2)
                true
            }
            R.id.add_photo -> {
                photosManager.addPhotos()
                true
            }
            R.id.save -> {
                if (enableSave)
                    addOrEdit.saveRealEstate()
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
        if (intent.getBooleanExtra(Code.IS_EDIT, false))
            toolbar!!.title = getString(R.string.edit_re)
        else
            toolbar!!.title = getString(R.string.add_re)
        setSupportActionBar(toolbar)
        invalidateOptionsMenu()
    }

    // -------------------
    // DATA
    // -------------------

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }

    private fun getRealEstate() {
        if (tempRE != null) {
            Log.d("TEMP RE NOT NULL ", tempRE!!.toString())
            itemViewModel.getRealEstate(tempRE!!.id!!)
                    .observe(this, Observer<RealEstate> { r -> updateRealEstate(r) })
        } else {
            if (Data.isEdit)
                itemViewModel.getRealEstate(Data.reID!!)
                        .observe(this, Observer<RealEstate> { r -> updateRealEstate(r) })
            else
                updateRealEstate(null)
        }
    }

    private fun updateRealEstate(realEstate: RealEstate?) {
        val re = realEstate ?: RealEstate(null, "", "", "", "", "", false, null, null, null, null,
                null, null, null, null, "", null, false, "", "", "",
                false, false, false, false, false, false, false, false, null,0)

        tempRE = TempRealEstate(re.id, re.streetNumber, re.street, re.zipCode, re.locality, re.state, re.verified, re.latitude, re.longitude, re.type, re.surface,
                re.room, re.bed, re.bath, re.kitchen, re.description, re.price, re.sold, re.marketDate, re.soldDate, re.agentName,
                re.poiSchool, re.poiShops, re.poiPark, re.poiSubway, re.poiBus, re.poiTrain, re.poiHospital, re.poiAirport, re.photos, re.selected)

        Log.d("TEMP RE UPDATED", tempRE!!.toString())

        this.setFragment(Data.fragNum)
    }

    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    override fun setFragment(index: Int) {
        Data.fragNum = index
        changeToolBarMenu(0)

        var fragment: Fragment? = null
        invalidateOptionsMenu()
        // Data.tab = index
        when (index) {
            0 -> {
                changeToolBarMenu(0)
                this.addOrEdit = AddOrEdit.newInstance(tempRE!!, itemViewModel)
                fragment = this.addOrEdit
            }

            1 -> {
                changeToolBarMenu(1)
                this.photosManager = PhotosManager.newInstance(tempRE!!, itemViewModel, photosList)
                fragment = this.photosManager
            }

            2 -> {
                changeToolBarMenu(3)
                this.legendsManager = LegendsManager.newInstance(tempRE!!, itemViewModel, photosList)
                fragment = this.legendsManager
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

    override fun savedRe(re: RealEstate) {
        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        intent.putExtra(REAL_ESTATE, re)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext,
                987, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        pendingIntent.send()

        backToMainActivity()
    }

    override fun savePhotos() {
        photosList = photosManager.photosList
        val pl = arrayListOf<Photos>()
        pl.addAll(photosList)
        tempRE!!.photos = pl
        Log.d("SAVED PHOTOS", tempRE!!.photos.toString())
    }

    override fun changeToolBarMenu(em: Int) {
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
                paramItems[3] = false
                toolbar!!.setNavigationOnClickListener { setFragment(0) }
            }
            2 -> {
                paramItems[0] = true
                paramItems[1] = true
                paramItems[2] = true
                paramItems[3] = false
                toolbar!!.setNavigationOnClickListener { setFragment(0) }
            }
            3 -> {
                paramItems[0] = false
                paramItems[1] = false
                paramItems[2] = false
                paramItems[3] = false
                toolbar!!.setNavigationOnClickListener { setFragment(1) }
            }
        }
    }

    override fun setSave(bool: Boolean) {
        Log.d("SET SAVE", bool.toString())
        enableSave = bool
        if (toolMenu != null)
            setOptionMenu(toolMenu!!)
    }

    /////////////////////////////////////////
    ///// LOCATION //////////////////////////
    /////////////////////////////////////////

    override fun onLocationChanged(p0: android.location.Location?) {
        if (p0 != null) {
            mLastKnownLocation = (LatLng(p0.latitude, p0.longitude))
            Log.d(TAG, mLastKnownLocation.toString())
        }
    }

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
                        if (mLastKnownLocation != null) {
                            task.result!!.latitude = mLastKnownLocation!!.latitude
                            task.result!!.longitude = mLastKnownLocation!!.longitude
                            Log.d("LOCATION", "OVER")
                        } else {
                            // Prompt the user for permission.
                            getLocationPermission()
                            if (!errorLoc) {
                                errorLoc = true
                                Log.e("ERROR", "LOCATION")
                            }
                        }
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