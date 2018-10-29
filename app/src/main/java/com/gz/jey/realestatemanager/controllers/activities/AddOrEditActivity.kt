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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.*
import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.RealEstateViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.PointsOfInterest
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter
import java.util.*
import kotlin.collections.ArrayList

class AddOrEditActivity : AppCompatActivity(), PhotosAdapter.Listener {
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
    lateinit var realEstateViewModel: RealEstateViewModel
    lateinit var database: RealEstateManagerDatabase
    private var enableSave = false
    var oblArray: ArrayList<Int> = arrayListOf(0, 2, 11, 12, 15)
    private val poiList: ArrayList<PointsOfInterest> = ArrayList()
    private val photosList: ArrayList<Photos> = ArrayList()
    private val checks: ArrayList<ImageView?> = ArrayList()
    private val values: ArrayList<TextView?> = ArrayList()
    private val marks: ArrayList<TextView?> = ArrayList()
    private val results: ArrayList<String> = ArrayList()

    private var photoSelected: Photos? = null
    // FOR REALESTATE SELECTOR
    var insert: Int? = null
    var realEstate: RealEstate? = null
    var poi: List<PointsOfInterest>? = null
    var photos: List<Photos>? = null

    private var landscape = false

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras["IS_EDIT"]
        this.setContentView(R.layout.activity_add_or_edit)
        init()
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {
        this.landscape = Utils.isLandscape(this)
        this.configureToolBar()
        this.setIcon()
        this.setItems()
        this.setViewModel()
        this.setAddOrEdit()
    }

    private fun setItems() {
        val inflater: LayoutInflater = LayoutInflater.from(this)
        container = findViewById(R.id.container)
        for (i in 0 until 19) {
            when (i) {
                9 -> {
                    val child: View = inflater.inflate(R.layout.add_edit_big_fields, null)
                    container.addView(child)
                    marks.add(null)
                    checks.add(null)
                    values.add(child.findViewById(R.id.value_description))
                    results.add("")
                }
                13 -> {
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
                    if (i == 12) {
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
        this.realEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel::class.java)

    }

    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    private fun setAddOrEdit() {
        this.realEstate = RealEstate()
        if (intent.getBooleanExtra(Code.IS_EDIT, false)) {
            toolbar!!.title = "Edit Real Estate"
            this.realEstateViewModel.getRealEstate(intent.getLongExtra(Code.RE_ID, 0)).observe(this, Observer<RealEstate> { re ->
                Log.d("EDIT RE => ", re.toString())
                if (re != null) {
                    this.realEstate = re
                    for (index in 0 until 19)
                        getDtbValue(index)
                } else {
                    ToastMessage().notifyMessage(this, Code.ERROR_NOT_FOUND)
                    for (index in 0 until 19)
                        getDtbValue(index)
                }
            })
        } else {
            toolbar!!.title = "Add Real Estate"
            for (index in 0 until 19)
                getDtbValue(index)
        }
    }

    private fun openDialog(index: Int) {
        insert = index
        val res: ArrayList<String> = ArrayList()
        res.add(results[index])
        when (index) {
            0 -> ViewDialogInputText().showDialog(this, Code.DISTRICT, res)
            1 -> ViewDialogInputAddress().showDialog(this, this, Code.ADDRESS, res)
            2 -> ViewDialogMultiChoice().showDialog(this, Code.TYPE, res)
            3 -> ViewDialogInputText().showDialog(this, Code.SURFACE, res)
            4 -> ViewDialogInputText().showDialog(this, Code.ROOM_NUM, res)
            5 -> ViewDialogInputText().showDialog(this, Code.BED_NUM, res)
            6 -> ViewDialogInputText().showDialog(this, Code.BATH_NUM, res)
            7 -> ViewDialogInputText().showDialog(this, Code.KITCHEN_NUM, res)
            8 -> ViewDialogInputText().showDialog(this, Code.DESCRIPTION, res)
            //9 -> { HUGE FIELDS }
            10 -> ViewDialogMultiChoice().showDialog(this, Code.CURRENCY, res)
            11 -> ViewDialogInputText().showDialog(this, Code.PRICE, res)
            12 -> ViewDialogPhotoPicker().showDialog(this)
            //13 -> { RECYCLER VIEW}
            14 -> {
                res.clear()
                for (p in poiList)
                    res.add(p.toString())
                ViewDialogMultiChoice().showDialog(this, Code.POI, res)
            }
            15 -> ViewDialogMultiChoice().showDialog(this, Code.STATUS, res)
            16 -> ViewDialogDatePicker().showDialog(this, Code.SALE_DATE, res)
            17 -> ViewDialogDatePicker().showDialog(this, Code.SOLD_DATE, res)
            18 -> ViewDialogInputText().showDialog(this, Code.AGENT, res)
        }
    }

    private fun saveRealEstate() {
        enableSave = false
        setSave(enableSave)
        //results[18] = "Jey"
        val re = RealEstate(realEstate!!.id,
                if (results[0].isNotEmpty()) results[0] else null,
                if (results[1].isNotEmpty()) results[1] else null,
                if (results[2].isNotEmpty()) results[2].toInt() else null,
                if (results[3].isNotEmpty()) results[3].toInt() else null,
                if (results[4].isNotEmpty()) results[4].toInt() else null,
                if (results[5].isNotEmpty()) results[5].toInt() else null,
                if (results[6].isNotEmpty()) results[6].toInt() else null,
                if (results[7].isNotEmpty()) results[7].toInt() else null,
                if (results[8].isNotEmpty()) results[8] else null,
                if (results[10].isNotEmpty()) results[10].toInt() else null,
                if (results[11].isNotEmpty()) results[11].toLong() else null,
                if (results[15].isNotEmpty()) results[15].toInt() else null,
                if (results[16].isNotEmpty()) results[16] else null,
                if (results[17].isNotEmpty()) results[17] else null,
                if (results[18].isNotEmpty()) results[18] else null,
                false,
                if (photosList.isNotEmpty()) photosList else null,
                if (poiList.isNotEmpty()) poiList else null
        )

        Log.d("RE SAVE", re.toString())

        if (realEstate!!.id != null) realEstateViewModel.updateRealEstate(re)
        else realEstateViewModel.createRealEstate(re)

        loadMainActivity()
    }

    private fun getDtbValue(code: Int) {
        val resArray: ArrayList<String> = ArrayList()
        insert = code
        if (code != 9 && code != 12 && code != 14) {
            val result = when (code) {
                0 -> if (realEstate!!.district != null) realEstate!!.district.toString() else ""
                1 -> if (realEstate!!.address != null) realEstate!!.address.toString() else ""
                2 -> if (realEstate!!.type != null) realEstate!!.type.toString() else ""
                3 -> if (realEstate!!.surface != null) realEstate!!.surface.toString() else ""
                4 -> if (realEstate!!.room != null) realEstate!!.room.toString() else ""
                5 -> if (realEstate!!.bed != null) realEstate!!.bed.toString() else ""
                6 -> if (realEstate!!.bath != null) realEstate!!.bath.toString() else ""
                7 -> if (realEstate!!.kitchen != null) realEstate!!.kitchen.toString() else ""
                8 -> if (realEstate!!.description != null) realEstate!!.description.toString() else ""
                10 -> if (realEstate!!.currency != null) realEstate!!.currency.toString() else ""
                11 -> if (realEstate!!.price != null) realEstate!!.price.toString() else ""
                15 -> if (realEstate!!.status != null) realEstate!!.status.toString() else ""
                16 -> if (realEstate!!.marketDate != null) realEstate!!.marketDate.toString() else ""
                17 -> if (realEstate!!.soldDate != null) realEstate!!.soldDate.toString() else ""
                18 -> if (realEstate!!.agentName != null) realEstate!!.agentName.toString() else ""
                else -> return
            }
            // Log.d("GET DTB "+code.toString(), result)
            results[code] = result
            resArray.add(result)
            insertEditedValue(code, resArray)
        } else if (code == 12) {
            if (realEstate!!.id != null) {
                photosList.clear()
                photosList.addAll(realEstate!!.photos as ArrayList<Photos>)
                setEditedPhoto()
            }
        } else if (code == 14) {
            if (realEstate!!.id != null) {
                poiList.clear()
                if(realEstate!!.poi != null)
                    poiList.addAll(realEstate!!.poi as ArrayList<PointsOfInterest>)
                resArray.clear()
                for (p in poiList)
                    resArray.add(p.value.toString())
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
            Log.d("ARRAY " + code, array[0])
            Log.d("RESULTS " + code, results[code])
            results[code] = array[0]
            when (code) {
                2 -> {
                    values[2]!!.text = resources.getStringArray(R.array.type_ind)[array[0].toInt()]
                }
                3 -> {
                    values[3]!!.text = if (results[code].isNotEmpty()) Utils.getSurfaceFormat(this, results[code].toInt()) else ""
                }
                8 -> {
                    values[9]!!.text = results[code]
                    if (values[9]!!.text.isNotEmpty()) validate(8, true) else validate(8, false)
                }
                10 -> {
                    results[10] = if (results[10].isNotEmpty()) results[10] else "0"
                    values[10]!!.text = Utils.getCurrencyFormat(this, results[10].toInt())
                }
                11 -> {
                    results[11] = if (results[11].isNotEmpty()) results[11] else ""
                    values[11]!!.text = Utils.convertedHighPrice(this, results[10].toInt(), results[11].toLong())
                }
                12 -> {
                }
                14 -> {
                    poiList.clear()
                    var str = ""
                    for (i in 0 until array.size) {
                        val poi = PointsOfInterest(null, array[i].toInt())
                        poiList.add(poi)
                        val sent = resources.getStringArray(R.array.poi_ind)[array[i].toInt()]
                        val coma = if (i == (array.size - 1)) "" else ","
                        str += "$sent$coma"
                    }
                    values[14]!!.text = shortCutStr(str)
                }
                15 -> {
                    results[code] = if (results[15].isNotEmpty()) results[15] else "0"
                    values[15]!!.text = resources.getStringArray(R.array.status_ind)[results[15].toInt()]
                }
                else -> values[code]!!.text = shortCutStr(results[code])
            }
            if (values[code]!!.text.isNotEmpty()) validate(code, true) else validate(code, false)
        } else {
            results[code] = ""
            when (code) {
                8 -> {
                    values[9]!!.text = ""
                    validate(8, false)
                }
                12 -> {
                }
                14 -> {
                    poiList.clear()
                    values[14]!!.text = ""
                    validate(14, false)
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

        if (photosList.isNotEmpty()) validate(12, true) else validate(12, false)
    }

    private fun getC(c: Int): Int {
        return ContextCompat.getColor(this, c)
    }

    private fun validate(ind: Int, bool: Boolean) {
        val icon = if (bool) validIcon else unvalidIcon
        checks[ind]!!.background = icon

        when (results[15]) {
            "0" -> {
                oblArray.add(16)
                if (oblArray.contains(17))
                    oblArray.remove(17)
                marks[16]!!.setTextColor(getC(R.color.colorPrimary))
                marks[17]!!.setTextColor(getC(R.color.colorTransparent))
            }
            "1" -> {
                oblArray.add(17)
                if (oblArray.contains(16))
                    oblArray.remove(16)
                marks[16]!!.setTextColor(getC(R.color.colorTransparent))
                marks[17]!!.setTextColor(getC(R.color.colorPrimary))
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

    fun setSave(bool: Boolean) {
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

    /////////////////////////////////////////
    ///// LOCATION //////////////////////////
    /////////////////////////////////////////


    // FOR PERMISSIONS
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34
    var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    var mLastKnownLocation: LatLng? = null

    /**
     * GET DEVICE LOCATION
     */
    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        mFusedLocationProviderClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        Log.d(TAG, " TASK DEVICE LOCATION SUCCESS")
                        mLastKnownLocation = (LatLng(task.result!!.latitude, task.result!!.longitude))

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

}