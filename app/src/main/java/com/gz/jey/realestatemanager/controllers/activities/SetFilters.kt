package com.gz.jey.realestatemanager.controllers.activities

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
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputText
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMinMax
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMultiChoice
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

class SetFilters : AppCompatActivity(), PhotosAdapter.Listener {
    override fun onClickDeleteButton(position: Int) {}
    // FRAGMENTS
    val TAG = "SetFilters"
    // FOR DESIGN
    private var toolMenu: Menu? = null
    var toolbar: Toolbar? = null
    lateinit var container: LinearLayout
    private lateinit var typeValue: TextView
    private lateinit var surfaceValue: TextView
    private lateinit var poiValue: TextView
    private lateinit var statusValue: TextView
    private lateinit var photosValue: TextView
    private lateinit var priceValue: TextView
    // FOR ICONS
    private lateinit var enableSaveIcon: Drawable
    private lateinit var addIcon: Drawable
    private lateinit var removeIcon: Drawable
    // FOR DATA
    lateinit var realEstateViewModel: RealEstateViewModel
    lateinit var database: RealEstateManagerDatabase
    private var enableSave = false

    private val poiList: ArrayList<PointsOfInterest> = ArrayList()
    private val values: ArrayList<TextView?> = ArrayList()
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
        this.setContentView(R.layout.activity_filters)
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
        this.setPanel()
    }

    private fun setItems() {
        typeValue = findViewById(R.id.type_value)
        surfaceValue = findViewById(R.id.surface_value)
        poiValue = findViewById(R.id.poi_value)
        statusValue = findViewById(R.id.status_value)
        photosValue = findViewById(R.id.photos_value)
        priceValue = findViewById(R.id.price_value)
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.add_edit_menu, toolMenu)
        menu.getItem(0).icon = enableSaveIcon
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
                    this.saveFilters()
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

    private fun setPanel(){

        this.toolbar!!.title = "Set Filters"
    }

    private fun openDialog(index: Int) {
        insert = index
        val res: ArrayList<String> = ArrayList()
        res.add(results[index])
        when (index) {
            0 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_TYPE, res)
            1 -> ViewDialogMinMax().showDialog(this, Code.FILTER_SURFACE, res)
            2 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_POI, res)
            3 -> ViewDialogMultiChoice().showDialog(this, Code.FILTER_SINCE, res)
            4 -> ViewDialogInputText().showDialog(this, Code.FILTER_PHOTO, res)
            5 -> ViewDialogMinMax().showDialog(this, Code.FILTER_PRICE, res)
        }
    }

    private fun saveFilters() {


        Log.d("FILTERS SAVE", "filters")
        loadMainActivity()
    }

    private fun getDtbValue(code: Int) {
        val resArray: ArrayList<String> = ArrayList()
        insert = code
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

    }

    private fun loadMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setIcon() {
        enableSaveIcon = SetImageColor.changeDrawableColor(this, R.drawable.save, getC(R.color.colorWhite))
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
                }
                15 -> {
                    results[code] = if (results[15].isNotEmpty()) results[15] else "0"
                    values[15]!!.text = resources.getStringArray(R.array.status_ind)[results[15].toInt()]
                }
            }
        } else {
            results[code] = ""
            when (code) {
                8 -> {
                    values[9]!!.text = ""
                }
            }
        }
    }

    private fun getC(c: Int): Int {
        return ContextCompat.getColor(this, c)
    }
}