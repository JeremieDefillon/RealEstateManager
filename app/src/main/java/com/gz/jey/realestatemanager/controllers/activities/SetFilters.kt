package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogDatePicker
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputText
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMultiChoice
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.PhotosAdapter
import java.util.*
import kotlin.collections.ArrayList

class SetFilters : AppCompatActivity(), PhotosAdapter.Listener {
    override fun onClickDeleteButton(position: Int) {}
    // FRAGMENTS
    val TAG = "SetFilters"
    // FOR DESIGN
    private var toolMenu: Menu? = null
    var toolbar: Toolbar? = null
    lateinit var container: LinearLayout
    private lateinit var typeValue: TextView
    private lateinit var addTypeBtn: FrameLayout
    private lateinit var addTypeImg: ImageView
    private lateinit var poiValue: TextView
    private lateinit var addPoiBtn: FrameLayout
    private lateinit var addPoiImg: ImageView
    private lateinit var minRoomsBtn: LinearLayout
    private lateinit var minRoomsValue: TextView
    private lateinit var maxRoomsBtn: LinearLayout
    private lateinit var maxRoomsValue: TextView
    private lateinit var localityBtn: LinearLayout
    private lateinit var localityValue: TextView
    //private lateinit var distanceBtn: LinearLayout
    private lateinit var distanceValue: TextView
    private lateinit var statusBtn: LinearLayout
    private lateinit var statusValue: TextView
    private lateinit var dateBtn: LinearLayout
    private lateinit var dateValue: TextView
    private lateinit var minPriceBtn: LinearLayout
    private lateinit var minPriceValue: TextView
    private lateinit var maxPriceBtn: LinearLayout
    private lateinit var maxPriceValue: TextView
    private lateinit var minSurfaceBtn: LinearLayout
    private lateinit var minSurfaceValue: TextView
    private lateinit var maxSurfaceBtn: LinearLayout
    private lateinit var maxSurfaceValue: TextView
    private lateinit var minPhotoBtn: LinearLayout
    private lateinit var minPhotoValue: TextView
    private lateinit var resetFilters: Button
    private lateinit var sendFilters: Button

    // FOR ICONS
    private lateinit var addIcon: Drawable

    // FOR DATA
    private lateinit var realEstateViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    var filters: Filters? = null
    var currency: Int = 0

    private val results: ArrayList<String> = ArrayList()

    // FOR REALESTATE SELECTOR
    var insert: Int? = null

    private var landscape = false

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_filters)
        init()
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {
        this.landscape = Utils.isLandscape(this)
        for (i in 0 until 13)
            results.add("")
        this.configureToolBar()
        this.setViewModel()
        this.setIcon()
        this.setItems()
        this.setPanel()
    }

    private fun setItems() {
        typeValue = findViewById(R.id.type_value)
        addTypeBtn = findViewById(R.id.add_type_button)
        addTypeImg = findViewById(R.id.add_type_image)
        poiValue = findViewById(R.id.poi_value)
        addPoiBtn = findViewById(R.id.add_poi_button)
        addPoiImg = findViewById(R.id.add_poi_image)
        minRoomsBtn = findViewById(R.id.min_rooms_button)
        minRoomsValue = findViewById(R.id.min_rooms_value)
        maxRoomsBtn = findViewById(R.id.max_rooms_button)
        maxRoomsValue = findViewById(R.id.max_rooms_value)
        localityBtn = findViewById(R.id.locality_button)
        localityValue = findViewById(R.id.locality_value)
        //distanceBtn = findViewById(R.id.distance_button)
        //distanceValue = findViewById(R.id.distance_value)
        statusBtn = findViewById(R.id.status_button)
        statusValue = findViewById(R.id.status_value)
        dateBtn = findViewById(R.id.date_button)
        dateValue = findViewById(R.id.date_value)
        minPriceBtn = findViewById(R.id.min_price_button)
        minPriceValue = findViewById(R.id.min_price_value)
        maxPriceBtn = findViewById(R.id.max_price_button)
        maxPriceValue = findViewById(R.id.max_price_value)
        minSurfaceBtn = findViewById(R.id.min_surface_button)
        minSurfaceValue = findViewById(R.id.min_surface_value)
        maxSurfaceBtn = findViewById(R.id.max_surface_button)
        maxSurfaceValue = findViewById(R.id.max_surface_value)
        minPhotoBtn = findViewById(R.id.min_photos_button)
        minPhotoValue = findViewById(R.id.min_photos_value)
        resetFilters = findViewById(R.id.reset_filters)
        sendFilters = findViewById(R.id.send_filters)

        addTypeImg.background = addIcon
        addPoiImg.background = addIcon

        typeValue.setOnClickListener { openDialog(0) }
        addTypeBtn.setOnClickListener { openDialog(0) }
        poiValue.setOnClickListener { openDialog(1) }
        addPoiBtn.setOnClickListener { openDialog(1) }
        minRoomsBtn.setOnClickListener { openDialog(2) }
        maxRoomsBtn.setOnClickListener { openDialog(3) }
        localityBtn.setOnClickListener { openDialog(4) }
        //distanceBtn.setOnClickListener { openDialog(5) }
        statusBtn.setOnClickListener { openDialog(6) }
        dateBtn.setOnClickListener { openDialog(7) }
        minPriceBtn.setOnClickListener { openDialog(8) }
        maxPriceBtn.setOnClickListener { openDialog(9) }
        minSurfaceBtn.setOnClickListener { openDialog(10) }
        maxSurfaceBtn.setOnClickListener { openDialog(11) }
        minPhotoBtn.setOnClickListener { openDialog(12) }
        resetFilters.setOnClickListener { resetAllFilters() }
        sendFilters.setOnClickListener { setSearchFilters() }
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.add_edit_menu, toolMenu)
        toolMenu!!.getItem(0).isVisible = false
        return true
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
            this.backToMainActivity(false)
        }
    }

    // -------------------
    // DATA
    // -------------------

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.realEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }


    private fun setPanel() {
        this.toolbar!!.title = "Search with Filters"
        resetAllFilters()
        realEstateViewModel.getFilters(Code.FILTERS_DATA).observe(this, Observer<Filters> { fi ->
            if (fi != null) {
                this.filters = fi
                getDtbValue()
            } else {
                this.filters = Filters()
                ToastMessage().notifyMessage(this, Code.ERROR_NOT_FOUND)
            }
        })
    }

    private fun getDtbValue() {
        val array: ArrayList<String> = ArrayList()
        if (filters!!.id != null) {
            if (filters!!.type != null) {
                //array = arrayOf(filters!!.type!!)
            }
            if (filters!!.poi != null) {
                //filters!!.poi!!.toTypedArray(array)
            }
            if (filters!!.minRoom != null)
                insertStandardValue(Code.FILTER_MIN_ROOMS, filters!!.minRoom.toString())
            if (filters!!.maxRoom != null)
                insertStandardValue(Code.FILTER_MAX_ROOMS, filters!!.maxRoom.toString())
            if (filters!!.locality != null)
                insertStandardValue(Code.FILTER_LOCALITY, filters!!.locality.toString())
            if (filters!!.distance != null)
                insertStandardValue(Code.FILTER_DISTANCE, filters!!.distance.toString())
            if (filters!!.status != null)
                insertStandardValue(Code.FILTER_STATUS, filters!!.status.toString())
            if (filters!!.date != null)
                insertStandardValue(Code.FILTER_DATE, filters!!.date.toString())
            if (filters!!.minPrice != null)
                insertStandardValue(Code.FILTER_MIN_PRICE, filters!!.minPrice.toString())
            if (filters!!.maxPrice != null)
                insertStandardValue(Code.FILTER_MAX_PRICE, filters!!.maxPrice.toString())
            if (filters!!.minSurface != null)
                insertStandardValue(Code.FILTER_MIN_SURFACE, filters!!.minSurface.toString())
            if (filters!!.maxSurface != null)
                insertStandardValue(Code.FILTER_MAX_SURFACE, filters!!.maxSurface.toString())
            if (filters!!.minPhoto != null)
                insertStandardValue(Code.FILTER_PHOTO, filters!!.minPhoto.toString())
        }
    }

    private fun openDialog(index: Int) {
        insert = index
        val res: ArrayList<String> = ArrayList()
        res.add(results[index])
        when (index) {
            0 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_TYPE, res)
            1 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_POI, res)
            2 -> ViewDialogInputText().showDialog(this, Code.FILTER_MIN_ROOMS, res[0])
            3 -> ViewDialogInputText().showDialog(this, Code.FILTER_MAX_ROOMS, res[0])
            4 -> ViewDialogInputText().showDialog(this, Code.FILTER_LOCALITY, res[0])
            5 -> ViewDialogInputText().showDialog(this, Code.FILTER_DISTANCE, res[0])
            6 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_STATUS, res)
            7 -> ViewDialogDatePicker().showDialog(this, Code.FILTER_DATE)
            8 -> ViewDialogInputText().showDialog(this, Code.FILTER_MIN_PRICE, res[0])
            9 -> ViewDialogInputText().showDialog(this, Code.FILTER_MAX_PRICE, res[0])
            10 -> ViewDialogInputText().showDialog(this, Code.FILTER_MIN_SURFACE, res[0])
            11 -> ViewDialogInputText().showDialog(this, Code.FILTER_MAX_SURFACE, res[0])
            12 -> ViewDialogInputText().showDialog(this, Code.FILTER_PHOTO, res[0])
        }
    }

    private fun setSearchFilters() {
        Log.d("FILTERS SAVE", "filters")
        val fi = Filters(
                Code.FILTERS_DATA,
                splitArgs(results[0]),
                splitArgs(results[1]),
                if (results[2].isNotEmpty()) results[2].toInt() else null,
                if (results[3].isNotEmpty()) results[3].toInt() else null,
                if (results[4].isNotEmpty()) results[4] else null,
                if (results[5].isNotEmpty()) results[5].toInt() else null,
                if (results[6].isNotEmpty()) results[6].toInt() else null,
                if (results[7].isNotEmpty()) results[7] else null,
                if (results[8].isNotEmpty()) results[8].toLong() else null,
                if (results[9].isNotEmpty()) results[9].toLong() else null,
                if (results[10].isNotEmpty()) results[10].toInt() else null,
                if (results[11].isNotEmpty()) results[11].toInt() else null,
                if (results[12].isNotEmpty()) results[12].toInt() else null
        )

        Log.d("SQL FILTERS", fi.toString())

        if (filters!!.id != null) {
            Log.d("FILTER ID", filters!!.id.toString())
            realEstateViewModel.updateFilters(fi)
            backToMainActivity(true)
        } else {
            realEstateViewModel.createFilters(fi)
            Log.d("FILTER ID", "NEW ID")
            backToMainActivity(true)
        }

    }

    private fun splitArgs(r: String): List<Int>? {
        return if (r.isNotEmpty()) {
            val f = r.split(',')
            val l: ArrayList<Int> = ArrayList()
            for (i in f)
                l.add(i.toInt())

            l
        } else
            null
    }

    private fun backToMainActivity(setFilter: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Code.FILTERS, setFilter)
        startActivity(intent)
        finish()
    }

    private fun resetAllFilters() {
        val arr: ArrayList<Int> = ArrayList()
        for (i in 0 until 13) {
            insertStandardValue(Code.FILTER_DATE, "")
        }
    }

    fun insertStandardValue(code: String, res : String) {
        if (res.isNotEmpty()) {
            Log.d("RESULTS $code", res)
            when (code) {
                Code.FILTER_MIN_ROOMS -> minRoomsValue.text = results[2]
                Code.FILTER_MAX_ROOMS -> maxRoomsValue.text = results[3]
                Code.FILTER_LOCALITY -> localityValue.text = results[4]
                Code.FILTER_DISTANCE -> {
                    val dist = results[5]
                    distanceValue.text = "$dist km"
                }
                Code.FILTER_STATUS -> statusValue.text = resources.getStringArray(R.array.status_ind)[results[6].toInt()]
                Code.FILTER_DATE -> dateValue.text = results[7]
                Code.FILTER_MIN_PRICE -> {
                    try {
                        val l = results[8].toLong()
                        minPriceValue.text = Utils.convertedHighPrice(this, 0, l)
                    } catch (e: Exception) {
                        Log.e("ERROR MIN PRICE", e.toString())
                    }
                }
                Code.FILTER_MAX_PRICE -> {
                    try {
                        val l = results[9].toLong()
                        maxPriceValue.text = Utils.convertedHighPrice(this, 0, l)
                    } catch (e: Exception) {
                        Log.e("ERROR MAX PRICE", e.toString())
                    }
                }
                Code.FILTER_MIN_SURFACE -> minSurfaceValue.text = Utils.getSurfaceFormat(this, results[10].toInt())
                Code.FILTER_MAX_SURFACE -> maxSurfaceValue.text = Utils.getSurfaceFormat(this, results[11].toInt())
                Code.FILTER_PHOTO -> minPhotoValue.text = results[12]
            }
        } else {
            for (i in 0 until results.size)
                results[i] = ""
            typeValue.text = ""
            typeValue.visibility = View.GONE
            addTypeBtn.visibility = View.VISIBLE
            poiValue.text = ""
            poiValue.visibility = View.GONE
            addPoiBtn.visibility = View.VISIBLE
            minRoomsValue.text = ""
            maxRoomsValue.text = ""
            localityValue.text = ""
            //distanceValue.text = ""
            statusValue.text = ""
            dateValue.text = ""
            minPriceValue.text = ""
            maxPriceValue.text = ""
            minSurfaceValue.text = ""
            maxSurfaceValue.text = ""
            minPhotoValue.text = ""
        }
    }

    fun insertMultiValues(code: String, res: ArrayList<String>){
        when (code) {
            Code.FILTER_LOCALITY -> {
                if (res.isNotEmpty()) {
                    typeValue.text = setSeveralValue(code, res)
                    typeValue.visibility = View.VISIBLE
                    addTypeBtn.visibility = View.GONE
                } else {
                    typeValue.visibility = View.GONE
                    addTypeBtn.visibility = View.VISIBLE
                }
            }
            Code.FILTER_DATE -> {
                if (res.isNotEmpty()) {
                    poiValue.text = setSeveralValue(code, res)
                    poiValue.visibility = View.VISIBLE
                    addPoiBtn.visibility = View.GONE
                } else {
                    poiValue.visibility = View.GONE
                    addPoiBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setSeveralValue(code: String, array: ArrayList<String>): String {
        val res = when (code){
            Code.FILTER_TYPE -> resources.getStringArray(R.array.type_ind)
            Code.FILTER_POI -> resources.getStringArray(R.array.poi_ind)
            else -> null
        }

        val t = StringBuilder()
        for ((i, s) in array.withIndex()) {
            t.append(res!![s.toInt()])
            if (i < array.size - 1)
                t.append("\r\n")
        }
        return t.toString()
    }

    private fun setSeveralResult(array: ArrayList<String>): String {
        val str = StringBuilder()
        for (a in array)
            str.append(a).append(",")

        return str.substring(0, str.length - 1)
    }

    private fun setIcon() {
        addIcon = SetImageColor.changeDrawableColor(this, R.drawable.add_box, getC(R.color.colorSecondary))
    }

    private fun getC(c: Int): Int {
        return ContextCompat.getColor(this, c)
    }
}