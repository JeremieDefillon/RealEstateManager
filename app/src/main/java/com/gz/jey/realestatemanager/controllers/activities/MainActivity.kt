package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.fragments.RealEstateDetails
import com.gz.jey.realestatemanager.controllers.fragments.RealEstateList
import com.gz.jey.realestatemanager.database.RealEstateManagerDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.RealEstateViewModel
import com.gz.jey.realestatemanager.models.*
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // FRAGMENTS
    private val TAG = "MainActivity"
    var lastFragment: Fragment? = null
    var realEstateList: RealEstateList? = null
    var realEstateDetails: RealEstateDetails? = null

    // FOR DESIGN
    private lateinit var enableEditIcon: Drawable
    private lateinit var disableEditIcon: Drawable
    private var toolMenu: Menu? = null
    var fragmentContainer: FrameLayout? = null
    private var drawerLayout: DrawerLayout? = null
    var toolbar: Toolbar? = null
    private var navigationView: NavigationView? = null
    lateinit var editItem: MenuItem
    var loading: FrameLayout? = null
    private var loadingContent: TextView? = null

    // FOR DATA
    lateinit var realEstateViewModel: RealEstateViewModel
    lateinit var database: RealEstateManagerDatabase
    private var settings : Settings? = null
    private var existMenu: Boolean = false
    var tabLand: Boolean = false


    // FOR REALESTATE SELECTOR
    var realEstate: RealEstate? = null
    var poi: ArrayList<PointsOfInterest>? = null
    var photos: ArrayList<Photos>? = null

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        tabLand = (findViewById<View>(R.id.fragment_details) != null)
        setLang()
        setIcon()
        //loading = findViewById(R.id.loading)
        //loadingContent = findViewById(R.id.content_loading)
        //loadingContent!!.text = getString(R.string.initActivity)
        //setLoading(true, true)
        fragmentContainer = findViewById(R.id.fragment_container)

        //Log.d("ENABLE NOTIF ???", Data.enableNotif.toString())

        initActivity()
        if (savedInstanceState == null) {
            val extras = intent.extras

            // if(Data.enableNotif && !fromNotif)
            // setNotification()
        }
    }

    private fun setLang() {
        // Data.lang = if(Locale.getDefault().displayLanguage=="fr") 1 else 0
    }

    private fun setIcon() {
        enableEditIcon = SetImageColor.changeDrawableColor(this, R.drawable.edit, ContextCompat.getColor(this, R.color.colorWhite))
        disableEditIcon = SetImageColor.changeDrawableColor(this, R.drawable.edit, ContextCompat.getColor(this, R.color.colorGrey))
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        //saveDatas()
        this.configureToolBar()
        this.setDrawerLayout()
        this.setViewModel()
        this.realEstateList = RealEstateList.newInstance(this)
        this.realEstateDetails = RealEstateDetails.newInstance(this)
        this.setFragment(0)
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.menu_toolbar, toolMenu)
        for (i in 0 until menu.size())
            menu.getItem(i).isVisible = existMenu

        editItem = menu.getItem(1)
        if(realEstate!=null) setEdit(true)
        else setEdit(false)
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
            R.id.add -> {
                realEstate = null
                addOrEditActivity(false)
                true
            }
            R.id.edit -> {
                if (realEstate!=null) {
                    addOrEditActivity(true)
                } else {
                    ToastMessage().notifyMessage(this, Code.UNEDITABLE)
                }
                true
            }
            R.id.search_on -> {
                filtersActivity()
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
        invalidateOptionsMenu()
    }

    // -------------------
    // DATA
    // -------------------

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.realEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel::class.java)
        this.realEstateViewModel.getSettings().observe(this, Observer<Settings>{ s -> initSettings(s)})
    }

    private fun initSettings(set : Settings?){
        if(set != null) {
            settings = set
            realEstateViewModel.updateSettings(settings!!)
        }else{
            val lang = if(Locale.getDefault().language == "fr") 1 else 0
            settings = Settings(null,0,lang,null,false,true)
            realEstateViewModel.createSettings(settings!!)
        }
    }

    /**
     * CONFIGURE DRAWER LAYOUT
     */
    private fun setDrawerLayout() {
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
    }

    /**
     * CONFIGURE NAVIGATION VIEW
     */
    private fun setNavigationView() {
        navigationView = findViewById(R.id.activity_main_nav_view)
        navigationView!!.menu.clear()
        menuInflater.inflate(R.menu.menu_nav_drawer, navigationView!!.menu)

        navigationView!!.setNavigationItemSelectedListener(this)
    }

    /**
     * @param item MenuItem
     * @return true
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle Navigation Item Click
        item.isChecked = true
        when (item.itemId) {
            R.id.restaurant_menu -> {

            }
            R.id.settings -> setFragment(5)
        }
        this.drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    fun setFragment(index: Int) {
        changeToolBarMenu(0)
        //loadingContent!!.text = getString(R.string.loadingView)
        //hideKeyboard()
        // if (fromNotif) {
        //   fromNotif=false
        //  execRequest(CODE_DETAILS)
        //}else {

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (tabLand) {
            invalidateOptionsMenu()
            changeToolBarMenu(1)
            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, this.realEstateList)
                    .commit()

            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_details, this.realEstateDetails)
                    .commit()
        }else{
            var fragment: Fragment? = null
            invalidateOptionsMenu()
            // Data.tab = index
            when (index) {
                0 -> {
                    changeToolBarMenu(1)
                    fragment = this.realEstateList
                }

                1 -> {
                    changeToolBarMenu(2)
                    fragment = this.realEstateDetails
                }
            }

            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    private fun addOrEditActivity(isEdit: Boolean) {
        val intent = Intent(this, AddOrEditActivity::class.java)
        intent.putExtra(Code.IS_EDIT, isEdit)
        intent.putExtra(Code.RE_ID, realEstate?.id)
        startActivity(intent)
        finish()
    }

    private fun filtersActivity() {
        val intent = Intent(this, SetFilters::class.java)
        startActivity(intent)
        finish()
    }

    private fun changeToolBarMenu(em: Int) {
        when (em) {
            0 -> {
                existMenu = false
                invalidateOptionsMenu()
                Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                toolbar!!.setNavigationOnClickListener {
                    //setLoading(true, true)
                    setFragment(0)
                }
            }
            1 -> {
                existMenu = true
                invalidateOptionsMenu()
                Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.menu)
                setDrawerLayout()
            }
        }
    }

    fun setRE(re: RealEstate) {
        this.realEstate = re
        setEdit(true)
    }

    fun unsetRE() {
        setEdit(false)
        if (realEstate != null) {
            realEstate!!.isSelected = false
            realEstateViewModel.updateRealEstate(realEstate!!)
        }
        realEstate = null
        poi = null
        photos = null
    }

    private fun setEdit(bool: Boolean) {
        if (bool) editItem.icon = enableEditIcon
        else editItem.icon = disableEditIcon
    }

}