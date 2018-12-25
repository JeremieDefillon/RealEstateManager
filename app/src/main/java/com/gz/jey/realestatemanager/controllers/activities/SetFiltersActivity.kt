package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.LiveData
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
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogDatePicker
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogInputText
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogMultiChoice
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Filters
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_filters.*
import java.util.*
import kotlin.collections.ArrayList

class SetFiltersActivity : AppCompatActivity() {

    // FOR DESIGN
    private var toolMenu: Menu? = null
    var toolbar: Toolbar? = null

    // FOR ICONS
    private lateinit var addIcon: Drawable

    // FOR DATA
    private lateinit var itemViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    var filters: Filters? = null

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
        Data.lang = if(Locale.getDefault().language =="fr") 1 else 0
        init()
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {
        this.landscape = Utils.isLandscape(this)
        this.configureToolBar()
        this.setViewModel()
        this.setIcon()
        this.setItems()
        this.setPanel()
    }

    /**
     * TO SET ITEMS
     */
    private fun setItems() {
        add_type_image.background = addIcon
        add_poi_image.background = addIcon

        type_value.setOnClickListener { openDialog(0) }
        add_type_button.setOnClickListener { openDialog(0) }
        poi_value.setOnClickListener { openDialog(1) }
        add_poi_button.setOnClickListener { openDialog(1) }
        min_rooms_button.setOnClickListener { openDialog(2) }
        max_rooms_button.setOnClickListener { openDialog(3) }
        locality_button.setOnClickListener { openDialog(4) }
        //distanceBtn.setOnClickListener { openDialog(5) }
        status_button.setOnClickListener { openDialog(6) }
        date_button.setOnClickListener { openDialog(7) }
        min_price_button.setOnClickListener { openDialog(8) }
        max_price_button.setOnClickListener { openDialog(9) }
        min_surface_button.setOnClickListener { openDialog(10) }
        max_surface_button.setOnClickListener { openDialog(11) }
        min_photos_button.setOnClickListener { openDialog(12) }
        reset_filters.setOnClickListener { resetAllFilters() }
        send_filters.setOnClickListener { setSearchFilters() }
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.add_edit_menu, toolMenu)
        for (i in 0 until 4)
            toolMenu!!.getItem(i).isVisible = false
        return true
    }

    /**
     * CONFIGURE TOOLBAR
     */
    private fun configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar)
        this.toolbar!!.title = getString(R.string.search_filters)
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

    /**
     * TO SET VIEW MODEL
     */
    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }

    /**
     * TO SET PANEL
     */
    private fun setPanel() {
        itemViewModel.getFilters(Code.FILTERS_DATA).observeOnce(Observer { fi ->
            if (fi != null) getFilters(fi)
            else createFilters()
        })
    }

    /**
     * @param fi Filters
     * TO GET FILTERS
     */
    private fun getFilters(fi: Filters) {
        Log.d("GET FILTERS", fi.toString())
        this.filters = fi
        getDtbValue()
    }

    /**
     * TO CREATE FILTERS
     */
    private fun createFilters() {
        this.filters = Filters(Code.FILTERS_DATA, null, null, null, null, null, null, null, null, null, null, null, null, null)
        itemViewModel.createFilters(this.filters!!)
        ToastMessage().notifyMessage(this, Code.ERROR_NOT_FOUND)
    }

    /**
     * TO GET DATABASE VALUES
     */
    private fun getDtbValue() {
        if (filters!!.id != null) {
            if (filters!!.type != null) {
                val res: ArrayList<String> = ArrayList()
                for (t in filters!!.type!!)
                    res.add(t.toString())
                insertMultiValues(Code.FILTER_TYPE, res)
            }
            if (filters!!.poi != null) {
                val res: ArrayList<String> = ArrayList()
                for (p in filters!!.poi!!)
                    res.add(p.toString())
                insertMultiValues(Code.FILTER_POI, res)
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
            if (filters!!.date != null) {
                Log.d("FILTER DATE", filters!!.date.toString())
                insertStandardValue(Code.FILTER_DATE, filters!!.date.toString())
            }
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

    /**
     * @param index Int
     * TO OPEN DIALOG
     */
    private fun openDialog(index: Int) {
        val res: ArrayList<String> = ArrayList()
        insert = index
        when (index) {
            0 -> {
                if (filters!!.type != null)
                    for (t in filters!!.type!!)
                        res.add(t.toString())
                ViewDialogMultiChoice().showDialog(this, Code.FILTER_TYPE, res)
            }
            1 -> {
                if (filters!!.poi != null)
                    for (t in filters!!.poi!!)
                        res.add(t.toString())
                ViewDialogMultiChoice().showDialog(this, Code.FILTER_POI, res)
            }
            2 -> {
                if (filters!!.minRoom != null)
                    res.add(filters!!.minRoom.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MIN_ROOMS, res[0])
            }
            3 -> {
                if (filters!!.maxRoom != null)
                    res.add(filters!!.maxRoom.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MAX_ROOMS, res[0])
            }
            4 -> {
                if (filters!!.locality != null)
                    res.add(filters!!.locality.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_LOCALITY, res[0])
            }
            5 -> {
                if (filters!!.distance != null)
                    res.add(filters!!.distance.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_DISTANCE, res[0])
            }
            6 -> {
                if (filters!!.status != null)
                    res.add(filters!!.status.toString())
                else
                    res.add("")
                ViewDialogMultiChoice().showDialog(this, Code.FILTER_STATUS, res)
            }
            7 -> {
                if (filters!!.date != null) {
                    res.add(filters!!.date.toString())
                }else
                    res.add("")
                ViewDialogDatePicker().showDialog(this, Code.FILTER_DATE)
            }
            8 -> {
                if (filters!!.minPrice != null)
                    res.add(filters!!.minPrice.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MIN_PRICE, res[0])
            }
            9 -> {
                if (filters!!.maxPrice != null)
                    res.add(filters!!.maxPrice.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MAX_PRICE, res[0])
            }
            10 -> {
                if (filters!!.minSurface != null)
                    res.add(filters!!.minSurface.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MIN_SURFACE, res[0])
            }
            11 -> {
                if (filters!!.maxSurface != null)
                    res.add(filters!!.maxSurface.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_MAX_SURFACE, res[0])
            }
            12 -> {
                if (filters!!.minPhoto != null)
                    res.add(filters!!.minPhoto.toString())
                else
                    res.add("")
                ViewDialogInputText().showDialog(this, Code.FILTER_PHOTO, res[0])
            }
        }
    }

    /**
     * TO SET SEARCH FILTERS
     */
    private fun setSearchFilters() {
        Log.d("FILTERS SAVE", filters.toString())

        if (filters!!.id != null) {
            Log.d("FILTER ID", filters!!.id.toString())
            itemViewModel.updateFilters(filters!!)
            backToMainActivity(true)
        } else {
            itemViewModel.createFilters(filters!!)
            Log.d("FILTER ID", "NEW ID")
            backToMainActivity(true)
        }
    }

    /**
     * @param setFilter Boolean
     * TO GO BACK TO MAIN ACTIVITY
     */
    private fun backToMainActivity(setFilter: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Code.FILTERS, setFilter)
        startActivity(intent)
        finish()
    }

    /**
     * TO RESET ALL FILTERS
     */
    private fun resetAllFilters() {
        filters!!.type = null
        type_value.visibility = View.GONE
        add_type_button.visibility = View.VISIBLE
        filters!!.poi = null
        poi_value.visibility = View.GONE
        add_poi_button.visibility = View.VISIBLE

        filters!!.minRoom = null
        min_rooms_value.text = ""
        filters!!.maxRoom = null
        max_rooms_value.text = ""
        filters!!.locality = null
        locality_value.text = ""
        filters!!.distance = null
        //distances_value.text = ""
        filters!!.status = null
        status_value.text = ""
        filters!!.date = null
        date_value.text = ""
        filters!!.minPrice = null
        min_price_value.text = ""
        filters!!.maxPrice = null
        max_price_value.text = ""
        filters!!.minSurface = null
        min_surface_value.text = ""
        filters!!.maxSurface = null
        max_surface_value.text = ""
        filters!!.minPhoto = null
        min_photos_value.text = ""
    }

    /**
     * @param code String
     * @param res String
     * TO INSERT STANDARD VALUE
     */
    fun insertStandardValue(code: String, res: String) {
        Log.d("RESULTS $code", res)
        when (code) {
            Code.FILTER_MIN_ROOMS -> {
                filters!!.minRoom = if (res.isNotEmpty()) res.toInt() else null
                min_rooms_value.text = res
            }
            Code.FILTER_MAX_ROOMS -> {
                filters!!.maxRoom = if (res.isNotEmpty()) res.toInt() else null
                max_rooms_value.text = res
            }
            Code.FILTER_LOCALITY -> {
                filters!!.locality = if(res.isNotEmpty()) res else null
                locality_value.text = res
            }
            Code.FILTER_DISTANCE -> {
                filters!!.distance = if (res.isNotEmpty()) res.toInt() else null
                //val dist = results[5]
                //distances_value.text = "$dist km"
            }
            Code.FILTER_STATUS -> {
                filters!!.status = if (res.isNotEmpty()) res.toInt() else null
                status_value.text = if (res.isNotEmpty()) resources.getStringArray(R.array.status_ind)[res.toInt()] else ""
            }
            Code.FILTER_DATE -> {
                filters!!.date = res
                val date = if (Data.lang==1) Utils.getDateFr(filters!!.date!!) else Utils.getDateEn(filters!!.date!!)
                date_value.text = date
            }
            Code.FILTER_MIN_PRICE -> {
                try {
                    filters!!.minPrice = if (res.isNotEmpty()) res.toLong() else null
                    val l = if (res.isNotEmpty()) res.toLong() else null
                    min_price_value.text = Utils.convertedHighPrice(this, l)
                } catch (e: Exception) {
                    Log.e("ERROR MIN PRICE", e.toString())
                }
            }
            Code.FILTER_MAX_PRICE -> {
                try {
                    filters!!.maxPrice = if (res.isNotEmpty()) res.toLong() else null
                    val l = if (res.isNotEmpty()) res.toLong() else null
                    max_price_value.text = Utils.convertedHighPrice(this, l)
                } catch (e: Exception) {
                    Log.e("ERROR MAX PRICE", e.toString())
                }
            }
            Code.FILTER_MIN_SURFACE -> {
                filters!!.minSurface = if (res.isNotEmpty()) res.toInt() else null
                min_surface_value.text = if (res.isNotEmpty()) Utils.getSurfaceFormat(this, filters!!.minSurface) else ""
            }
            Code.FILTER_MAX_SURFACE -> {
                filters!!.maxSurface = if (res.isNotEmpty()) res.toInt() else null
                max_surface_value.text = if (res.isNotEmpty()) Utils.getSurfaceFormat(this, filters!!.maxSurface) else ""
            }
            Code.FILTER_PHOTO -> {
                filters!!.minPhoto = if (res.isNotEmpty()) res.toInt() else null
                min_photos_value.text = res
            }
        }
    }

    /**
     * @param code String
     * @param res ArrayList<String>
     * TO INSERT MULTIPLE VALUES
     */
    fun insertMultiValues(code: String, res: ArrayList<String>) {
        Log.d("FILTERS", code + " " + res.toString())
        when (code) {
            Code.FILTER_TYPE -> {
                if (res.isNotEmpty()) {
                    filters!!.type = getIntList(res)
                    type_value.text = setSeveralValue(code, res)
                    type_value.visibility = View.VISIBLE
                    add_type_button.visibility = View.GONE
                } else {
                    filters!!.type = null
                    type_value.visibility = View.GONE
                    add_type_button.visibility = View.VISIBLE
                }
            }
            Code.FILTER_POI -> {
                if (res.isNotEmpty()) {
                    filters!!.poi = getIntList(res)
                    poi_value.text = setSeveralValue(code, res)
                    poi_value.visibility = View.VISIBLE
                    add_poi_button.visibility = View.GONE
                } else {
                    filters!!.poi = null
                    poi_value.visibility = View.GONE
                    add_poi_button.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * @param code String
     * @param array ArrayList<String>
     * TO SET SEVERAL VALUE
     */
    private fun setSeveralValue(code: String, array: ArrayList<String>): String {
        val res = when (code) {
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

    /**
     * @param r ArrayList<String>
     * TO GET INT LIST
     * @return List<Int>
     */
    private fun getIntList(r: ArrayList<String>): List<Int>? {
        return if (r.isNotEmpty()) {
            val l: ArrayList<Int> = ArrayList()
            for (i in r)
                l.add(i.toInt())
            l
        } else
            null
    }

    /**
     * TO SET ICON
     */
    private fun setIcon() {
        addIcon = SetImageColor.changeDrawableColor(this, R.drawable.add_box, getC(R.color.colorSecondary))
    }

    /**
     * @param c Int
     * @return Int
     */
    private fun getC(c: Int): Int {
        return ContextCompat.getColor(this, c)
    }

    /**
     * @param observer Observer<T>
     * TO OBSERVE ONCE LIVE DATA
     */
    private fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}