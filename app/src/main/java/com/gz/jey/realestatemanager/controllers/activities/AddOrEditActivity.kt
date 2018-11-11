package com.gz.jey.realestatemanager.controllers.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.google.gson.JsonObject
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.api.ApiStreams
import com.gz.jey.realestatemanager.controllers.dialog.*
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.retrofit.GeoCode
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import java.util.*
import kotlin.collections.ArrayList

class AddOrEditActivity : AppCompatActivity(), PhotosAdapter.Listener, OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickDeleteButton(position: Int) {}
    // FRAGMENTS
    val TAG = "AddOrEditActivity"
    // FOR DESIGN
    private var toolMenu: Menu? = null
    var toolbar: Toolbar? = null
    lateinit var container: LinearLayout
    private var saveItem: MenuItem? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotosAdapter
    private lateinit var removePhoto: Button
    // FOR ICONS
    private lateinit var enableSaveIcon: Drawable
    private lateinit var disableSaveIcon: Drawable
    private lateinit var dollarIcon: Drawable
    private lateinit var euroIcon: Drawable
    private lateinit var unvalidIcon: Drawable
    private lateinit var validIcon: Drawable
    private lateinit var editIcon: Drawable
    private lateinit var addIcon: Drawable
    private lateinit var removeIcon: Drawable
    // FOR DATA
    lateinit var itemViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    private var enableSave = false
    var oblArray: ArrayList<Int> = arrayListOf(0, 1, 9, 10, 11, 14)
    private var resDist : String? = null
    private var location : LatLng? = null
    private var disposable : Disposable? = null
    private val poiList: ArrayList<Int> = ArrayList()
    private val photosList: ArrayList<Photos> = ArrayList()
    private val checks: ArrayList<ImageView?> = ArrayList()
    private val values: ArrayList<TextView?> = ArrayList()
    private val marks: ArrayList<TextView?> = ArrayList()
    private val results: ArrayList<String> = ArrayList()

    private val address : JsonObject? = null

    // FOR POSITION
    lateinit var mGoogleApiClient: GoogleApiClient
    private var mGeoDataClient : GeoDataClient? = null
    private var mPlaceDetectionClient : PlaceDetectionClient? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private var photoSelected: Photos? = null
    // FOR REALESTATE SELECTOR
    var insert: Int? = null
    var realEstate: RealEstate? = null
    var poi: List<Int>? = null
    var photos: List<Photos>? = null

    private var landscape = false

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_or_edit)
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
        init()
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {
        this.landscape = Utils.isLandscape(this)
        this.configureToolBar()
        this.setLocalisationData()
        this.setIcon()
        this.setItems()
        this.setViewModel()
        this.setAddOrEdit()
    }

    /**
     * SET LOCALISATION DATA
     */
    private fun setLocalisationData(){
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(applicationContext)
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(applicationContext)
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        getDeviceLocation()
    }


    private fun setItems() {
        val inflater: LayoutInflater = LayoutInflater.from(this)
        container = findViewById(R.id.container)
        for (i in 0 until 18) {
            when (i) {
                8 -> {
                    val child: View = inflater.inflate(R.layout.add_edit_big_fields, null)
                    container.addView(child)
                    marks.add(null)
                    checks.add(null)
                    values.add(child.findViewById(R.id.value_description))
                    results.add("")
                }
                12 -> {
                    val child: View = inflater.inflate(R.layout.horizontal_recycler_view, null)
                    recyclerView = child.findViewById(R.id.photos_recycler_view)
                    configureRecyclerView()
                    container.addView(child)
                    marks.add(null)
                    checks.add(null)
                    values.add(null)
                    results.add("")
                }
                else -> {
                    val child: View = inflater.inflate(R.layout.add_edit_item, null)
                    container.addView(child)
                    marks.add(child.findViewById(R.id.mark))
                    values.add(child.findViewById(R.id.value))
                    checks.add(child.findViewById(R.id.check))
                    results.add("")
                    val label = child.findViewById<TextView>(R.id.label)
                    val remove = child.findViewById<Button>(R.id.remove)
                    val edit = child.findViewById<Button>(R.id.edit)

                    label.text = resources.getStringArray(R.array.add_edit_ind)[i]
                    remove.background = removeIcon
                    remove.visibility = View.GONE
                    edit.background = editIcon
                    checks[i]!!.background = unvalidIcon

                    if (oblArray.contains(i)) marks[i]!!.setTextColor(getC(R.color.colorPrimary))
                    else marks[i]!!.setTextColor(getC(R.color.colorTransparent))

                    edit.setOnClickListener { openDialog(i) }
                    if (i == 11) {
                        removePhoto = remove
                        removePhoto.setOnClickListener {
                            if (photoSelected != null)
                                photosList.remove(photoSelected!!)

                            removePhoto.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun configureRecyclerView() {
        this.adapter = PhotosAdapter(this)
        val llm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = llm
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.add_edit_menu, toolMenu)
        saveItem = menu.getItem(0)
        setSave(enableSave)
        return true
    }

    /**
     * @param item MenuItem
     * @return boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        // Handle item selection
        return when (id) {
            R.id.save -> {
                if (enableSave)
                    this.saveRealEstate()
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
        setSupportActionBar(toolbar)
        Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener {
            //setLoading(true, true)
            this.loadMainActivity()
        }
    }

    // -------------------
    // DATA
    // -------------------

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }

    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    private fun setAddOrEdit() {
        this.realEstate = RealEstate()
        if (intent.getBooleanExtra(Code.IS_EDIT, false)) {
            toolbar!!.title = "Edit Real Estate"
            this.itemViewModel.getRealEstate(intent.getLongExtra(Code.RE_ID, 0)).observe(this, Observer<RealEstate> { re ->
                Log.d("EDIT RE => ", re.toString())
                if (re != null) {
                    this.realEstate = re
                    for (index in 0 until 18)
                        getDtbValue(index)
                } else {
                    ToastMessage().notifyMessage(this, Code.ERROR_NOT_FOUND)
                    for (index in 0 until 18)
                        getDtbValue(index)
                }
            })
        } else {
            toolbar!!.title = "Add Real Estate"
            for (index in 0 until 18)
                getDtbValue(index)
        }
    }

    private fun openDialog(index: Int) {
        insert = index
        val res: ArrayList<String> = ArrayList()
        res.add(results[index])
        when (index) {
            0 -> ViewDialogInputAddress().showDialog(mGeoDataClient!!, this, this, Code.ADDRESS, res)
            1 -> ViewDialogMultiChoice().showDialog(this, Code.TYPE, res)
            2 -> ViewDialogInputText().showDialog(this, Code.SURFACE, res)
            3 -> ViewDialogInputText().showDialog(this, Code.ROOM_NUM, res)
            4 -> ViewDialogInputText().showDialog(this, Code.BED_NUM, res)
            5 -> ViewDialogInputText().showDialog(this, Code.BATH_NUM, res)
            6 -> ViewDialogInputText().showDialog(this, Code.KITCHEN_NUM, res)
            7 -> ViewDialogInputText().showDialog(this, Code.DESCRIPTION, res)
            //8 -> { HUGE FIELDS }
            9 -> ViewDialogMultiChoice().showDialog(this, Code.CURRENCY, res)
            10 -> ViewDialogInputText().showDialog(this, Code.PRICE, res)
            11 -> ViewDialogPhotoPicker().showDialog(this)
            //12 -> { RECYCLER VIEW}
            13 -> {
                res.clear()
                for (p in poiList)
                    res.add(p.toString())
                ViewDialogMultiChoice().showDialog(this, Code.POI, res)
            }
            14 -> ViewDialogMultiChoice().showDialog(this, Code.STATUS, res)
            15 -> ViewDialogDatePicker().showDialog(this, Code.SALE_DATE, res)
            16 -> ViewDialogDatePicker().showDialog(this, Code.SOLD_DATE, res)
            17 -> ViewDialogInputText().showDialog(this, Code.AGENT, res)
        }
    }

    private fun saveRealEstate() {
        enableSave = false
        setSave(enableSave)
        //results[18] = "Jey"
        val re = RealEstate(realEstate!!.id,
                if (resDist!!.isNotEmpty()) resDist else null,
                if (results[0].isNotEmpty()) results[0] else null,
                if (location!=null) location!!.latitude else null,
                if (location!=null) location!!.longitude else null,
                if (results[1].isNotEmpty()) results[1].toInt() else null,
                if (results[2].isNotEmpty()) results[2].toInt() else null,
                if (results[3].isNotEmpty()) results[3].toInt() else null,
                if (results[4].isNotEmpty()) results[4].toInt() else null,
                if (results[5].isNotEmpty()) results[5].toInt() else null,
                if (results[6].isNotEmpty()) results[6].toInt() else null,
                if (results[7].isNotEmpty()) results[7] else null,
                if (results[9].isNotEmpty()) results[9].toInt() else null,
                if (results[10].isNotEmpty()) results[10].toLong() else null,
                if (results[14].isNotEmpty()) results[14].toInt() else null,
                if (results[15].isNotEmpty()) results[15] else null,
                if (results[16].isNotEmpty()) results[16] else null,
                if (results[17].isNotEmpty()) results[17] else null,
                poiList.contains(0),
                poiList.contains(1),
                poiList.contains(2),
                poiList.contains(3),
                poiList.contains(4),
                poiList.contains(5),
                poiList.contains(6),
                poiList.contains(7),
                false,
                if (photosList.isNotEmpty()) photosList else null
        )

        Log.d("RE SAVE", re.toString())

        if (re.id != null) itemViewModel.updateRealEstate(re)
        else itemViewModel.createRealEstate(re)
        loadMainActivity()
    }

    private fun getDtbValue(code: Int) {
        val resArray: ArrayList<String> = ArrayList()
        insert = code
        if (code != 8 && code != 11 && code != 13) {
            val result = when (code) {
                0 -> if (realEstate!!.address != null) realEstate!!.address.toString() else ""
                1 -> if (realEstate!!.type != null) realEstate!!.type.toString() else ""
                2 -> if (realEstate!!.surface != null) realEstate!!.surface.toString() else ""
                3 -> if (realEstate!!.room != null) realEstate!!.room.toString() else ""
                4 -> if (realEstate!!.bed != null) realEstate!!.bed.toString() else ""
                5 -> if (realEstate!!.bath != null) realEstate!!.bath.toString() else ""
                6 -> if (realEstate!!.kitchen != null) realEstate!!.kitchen.toString() else ""
                7 -> if (realEstate!!.description != null) realEstate!!.description.toString() else ""
                9 -> if (realEstate!!.currency != null) realEstate!!.currency.toString() else ""
                10 -> if (realEstate!!.price != null) realEstate!!.price.toString() else ""
                14 -> if (realEstate!!.status != null) realEstate!!.status.toString() else ""
                15 -> if (realEstate!!.marketDate != null) realEstate!!.marketDate.toString() else ""
                16 -> if (realEstate!!.soldDate != null) realEstate!!.soldDate.toString() else ""
                17 -> if (realEstate!!.agentName != null) realEstate!!.agentName.toString() else ""
                else -> return
            }

            // Log.d("GET DTB "+code.toString(), result)
            results[code] = result
            resArray.add(result)
            if(code==0){
                resDist = if (realEstate!!.district != null) realEstate!!.district.toString() else ""
                resArray.add(resDist!!)
            }
            insertEditedValue(code, resArray)
        } else if (code == 11) {
            if (realEstate!!.id != null) {
                photosList.clear()
                photosList.addAll(realEstate!!.photos as ArrayList<Photos>)
                setEditedPhoto()
            }
        } else if (code == 13) {
            poiList.clear()
            if (realEstate!!.id != null) {
                for (i in 0 until 8){
                    when(i){
                        0 -> if(realEstate!!.poiSchool) poiList.add(0)
                        1 -> if(realEstate!!.poiShops) poiList.add(1)
                        2 -> if(realEstate!!.poiPark) poiList.add(2)
                        3 -> if(realEstate!!.poiSubway) poiList.add(3)
                        4 -> if(realEstate!!.poiBus) poiList.add(4)
                        5 -> if(realEstate!!.poiTrain) poiList.add(5)
                        6 -> if(realEstate!!.poiHospital) poiList.add(6)
                        7 -> if(realEstate!!.poiAirport) poiList.add(7)
                    }
                }

                Log.d("POI LIST ", poiList.toString())
                resArray.clear()
                for (p in poiList)
                    resArray.add(p.toString())
                insertEditedValue(code, resArray)
            }
        } else {
            return
        }
    }

    private fun loadMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setIcon() {
        enableSaveIcon = SetImageColor.changeDrawableColor(this, R.drawable.save, getC(R.color.colorWhite))
        disableSaveIcon = SetImageColor.changeDrawableColor(this, R.drawable.save, getC(R.color.colorGrey))
        dollarIcon = SetImageColor.changeDrawableColor(this, R.drawable.dollar, getC(R.color.colorSecondary))
        euroIcon = SetImageColor.changeDrawableColor(this, R.drawable.euro, getC(R.color.colorSecondary))
        unvalidIcon = SetImageColor.changeDrawableColor(this, R.drawable.close, getC(R.color.colorGrey))
        validIcon = SetImageColor.changeDrawableColor(this, R.drawable.check_circle, getC(R.color.colorSecondary))
        editIcon = SetImageColor.changeDrawableColor(this, R.drawable.edit, getC(R.color.colorSecondary))
        addIcon = SetImageColor.changeDrawableColor(this, R.drawable.add_box, getC(R.color.colorSecondary))
        removeIcon = SetImageColor.changeDrawableColor(this, R.drawable.minate_box, getC(R.color.colorError))
    }

    fun insertEditedValue(code: Int, array: ArrayList<String>) {
        if (array.isNotEmpty() && array[0].isNotEmpty()) {
            Log.d("ARRAY $code", array[0])
            Log.d("RESULTS $code", results[code])
            results[code] = array[0]
            when (code) {
                0 -> {
                    disposable = ApiStreams.streamGeoCode(array[0])
                            .subscribeWith(object : DisposableObserver<GeoCode>() {
                                override fun onNext(gc: GeoCode) {
                                    setAddress(gc)
                                }

                                override fun onError(e: Throwable) {
                                    Log.e("GC RX", e.toString())
                                }

                                override fun onComplete() {}
                            })
                }
                1 -> {
                    values[1]!!.text = resources.getStringArray(R.array.type_ind)[array[0].toInt()]
                }
                2 -> {
                    values[2]!!.text = if (results[code].isNotEmpty()) Utils.getSurfaceFormat(this, results[code].toInt()) else ""
                }
                7 -> {
                    values[8]!!.text = results[code]
                    if (values[8]!!.text.isNotEmpty()) validate(code, true) else validate(code, false)
                }
                9 -> {
                    results[code] = if (results[code].isNotEmpty()) results[code] else "0"
                    values[code]!!.text = Utils.getCurrencyFormat(this, results[code].toInt())
                }
                10 -> {
                    results[code] = if (results[code].isNotEmpty()) results[code] else ""
                    values[code]!!.text = Utils.convertedHighPrice(this, results[9].toInt(), results[code].toLong())
                }
                11 -> {
                }
                13 -> {
                    poiList.clear()
                    for (a in array)
                        poiList.add(a.toInt())
                    var str = ""
                    for (i in poiList) {
                            val sent = resources.getStringArray(R.array.poi_ind)[i]
                            val coma = if (i == (poiList.size - 1)) "" else ","
                            str += "$sent$coma"
                    }
                    values[code]!!.text = shortCutStr(str)
                }
                14 -> {
                    results[code] = if (results[code].isNotEmpty()) results[code] else "0"
                    values[code]!!.text = resources.getStringArray(R.array.status_ind)[results[code].toInt()]
                }
                else -> values[code]!!.text = shortCutStr(results[code])
            }
            if (values[code]!!.text.isNotEmpty()) validate(code, true) else validate(code, false)
        } else {
            results[code] = ""
            when (code) {
                0 -> {
                    resDist = ""
                    values[0]!!.text = ""
                    validate(code, false)
                }
                7 -> {
                    values[8]!!.text = ""
                    validate(code, false)
                }
                11 -> {
                }
                13 -> {
                    poiList.clear()
                    values[code]!!.text = ""
                    validate(code, false)
                }
                else -> {
                    values[code]!!.text = ""
                    validate(code, false)
                }
            }
        }
    }

    fun savePhoto(uri: String, legend: Int) {
        val photo = Photos(null, uri, legend)
        photosList.add(photo)
        setEditedPhoto()
    }

    private fun setEditedPhoto() {
        if (photosList.size > 0) {
            recyclerView.visibility = View.VISIBLE
            for (p in photosList)
                Log.d("Photo", p.image.toString())
            this.adapter.updateData(photosList as List<Photos>)
        } else
            recyclerView.visibility = View.GONE

        if (photosList.isNotEmpty()) validate(11, true) else validate(11, false)
    }

    private fun getC(c: Int): Int {
        return ContextCompat.getColor(this, c)
    }

    private fun validate(ind: Int, bool: Boolean) {
        val icon = if (bool) validIcon else unvalidIcon
        checks[ind]!!.background = icon

        when (results[14]) {
            "0" -> {
                oblArray.add(15)
                if (oblArray.contains(16))
                    oblArray.remove(16)
                marks[15]!!.setTextColor(getC(R.color.colorPrimary))
                marks[16]!!.setTextColor(getC(R.color.colorTransparent))
            }
            "1" -> {
                oblArray.add(16)
                if (oblArray.contains(15))
                    oblArray.remove(15)
                marks[15]!!.setTextColor(getC(R.color.colorTransparent))
                marks[16]!!.setTextColor(getC(R.color.colorPrimary))
            }
        }

        var c = 0
        for (i in oblArray) {
            if (checks[i]!!.background == unvalidIcon) {
                c++
                break
            }
        }
        enableSave = c == 0
        setSave(enableSave)
    }

    private fun setSave(bool: Boolean) {
        if (saveItem != null) {
            if (bool) saveItem!!.icon = enableSaveIcon
            else saveItem!!.icon = disableSaveIcon
        }
    }

    private fun shortCutStr(str: String): String {
        val max = if (landscape) 75 else 24
        return if (str.length > max) str.substring(0, max - 3) + " ..."
        else str
    }

    private fun setAddress(gc : GeoCode){
        Log.d("ADDEDIT GC" , gc.toString())
        if(gc.results!=null && gc.results.isNotEmpty()){
            values[0]!!.text = gc.results[0].formatted_address
            for (ac in gc.results[0].address_components!!){
                if(ac.types!!.contains("locality"))
                    resDist = ac.long_name
            }

            location = LatLng(gc.results[0].geometry!!.location!!.lat!!, gc.results[0].geometry!!.location!!.lng!!)
        }
    }

    /////////////////////////////////////////
    ///// LOCATION //////////////////////////
    /////////////////////////////////////////

    // FOR PERMISSIONS
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34
    var mLocationPermissionGranted: Boolean = false

    var mLastKnownLocation: LatLng? = null

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