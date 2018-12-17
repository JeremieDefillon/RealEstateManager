package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.fragments.RealEstateDetails
import com.gz.jey.realestatemanager.controllers.fragments.RealEstateList
import com.gz.jey.realestatemanager.database.ItemDatabase
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RealEstateList.RealEstateListListener {

    // FRAGMENTS
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

    // FOR DATA
    lateinit var itemViewModel: ItemViewModel
    lateinit var database: ItemDatabase
    private var existMenu: Boolean = false


    // FOR REALESTATE SELECTOR
    var realEstate: RealEstate? = null
    var poi: ArrayList<Int>? = null
    var photos: ArrayList<Photos>? = null

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        Data.lang = if(Locale.getDefault().language =="fr") 1 else 0
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                Data.fragNum = getInt(Code.FRAG_NUM)
                Data.reID = getLong(Code.RE_ID)
                Data.isEdit = getBoolean(Code.IS_EDIT)
                Data.photoNum = getInt(Code.PHOTO_NUM)
            }
        } else {
            Data.fragNum = intent.getIntExtra(Code.FRAG_NUM, 0)
            Data.isEdit = intent.getBooleanExtra(Code.IS_EDIT, false)
            Data.reID = if(intent.hasExtra(Code.RE_ID))intent.getLongExtra(Code.RE_ID, 1)else null
            Data.isEdit = false
            Data.photoNum = null
        }
        initActivity()
    }

    /**
     * @param outState Bundle
     * to save instance state
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(Code.FRAG_NUM, Data.fragNum)
        outState?.putLong(Code.RE_ID, Data.reID!!)
        outState?.putBoolean(Code.IS_EDIT, Data.isEdit)
        if(Data.photoNum != null)
            outState?.putInt(Code.PHOTO_NUM, Data.photoNum!!)
        super.onSaveInstanceState(outState)
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        initDatas()
        Data.tabMode = (findViewById<View>(R.id.fragment_details) != null)
        setIcon()
        fragmentContainer = findViewById(R.id.fragment_container)
        //saveDatas()
        this.configureToolBar()
        this.setNavigationView()
        this.setDrawerLayout()
        this.setViewModel()
        setFragment(Data.fragNum)
    }

    /**
     * TO SET ICON
     */
    private fun setIcon() {
        enableEditIcon = SetImageColor.changeDrawableColor(this, R.drawable.edit, ContextCompat.getColor(this, R.color.colorWhite))
        disableEditIcon = SetImageColor.changeDrawableColor(this, R.drawable.edit, ContextCompat.getColor(this, R.color.colorGrey))
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
     * @return true
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
                Log.d("EDIT ",realEstate.toString())
                if (realEstate != null) {
                    addOrEditActivity(true)
                } else {
                    ToastMessage().notifyMessage(this, Code.UNEDITABLE)
                }
                true
            }
            R.id.search_on -> {
                openFiltersActivity()
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

    /**
     * SET VIEW MODEL
     */
    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }

    /**
     * LOAD ALL THE SAVED DATAS FROM PREFERENCES
     */
    private fun initDatas(){
        if(intent.hasExtra(Code.SETTINGS) && intent.getBooleanExtra(Code.SETTINGS, false)){
            saveDatas()
        }else{
            intent.putExtra(Code.SETTINGS, false)
            Data.currency = getPreferences(Context.MODE_PRIVATE).getInt("CURRENCY", 0)
            Data.enableNotif = getPreferences(Context.MODE_PRIVATE).getBoolean("NOTIF", true)
            saveDatas()
        }
    }

    /**
     * SAVE ALL THE DATAS INTO PREFERENCES
     */
    private fun saveDatas(){
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("CURRENCY", Data.currency)
        editor.putBoolean("NOTIF", Data.enableNotif)
        editor.apply()
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
            R.id.filters -> openFiltersActivity()
            R.id.loan -> openLoanSimulator()
            R.id.settings -> openSettingsActivity()
            R.id.power_off -> {
                finish()
                System.exit(0)
            }
        }
        this.drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * @param index Int
     * CHANGE FRAGMENT
     */
    override fun setFragment(index: Int) {
        Data.fragNum = index
        changeToolBarMenu(0)

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (Data.tabMode) {
            this.realEstateList = RealEstateList.newInstance(itemViewModel, realEstate)
            this.realEstateDetails = RealEstateDetails.newInstance(itemViewModel)
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
                    this.realEstateList = RealEstateList.newInstance(itemViewModel, realEstate)
                    changeToolBarMenu(1)
                    fragment = this.realEstateList
                }

                1 -> {
                    this.realEstateDetails = RealEstateDetails.newInstance(itemViewModel)
                    changeToolBarMenu(2)
                    fragment = this.realEstateDetails
                }
            }

            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    /**
     * @param isEdit Boolean
     * To Launch Add & Edit Activity
     */
    fun addOrEditActivity(isEdit: Boolean) {
        val intent = Intent(this, AddOrEditActivity::class.java)
        intent.putExtra(Code.IS_EDIT, isEdit)
        intent.putExtra(Code.RE_ID, realEstate?.id)
        startActivity(intent)
        finish()
    }

    /**
     * To Launch Filters Activity
     */
    fun openFiltersActivity() {
        val intent = Intent(this, SetFiltersActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * To Launch Loan Simulator Activity
     */
    private fun openLoanSimulator() {
        val intent = Intent(this, LoanSimulatorActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * To Launch Settings Activity
     */
    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @param em Int
     * To change Toolbar Menu
     */
    private fun changeToolBarMenu(em : Int) {
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

    /**
     * @param realEstate RealEstate
     * To Set the real estate
     */
    override fun setRE(realEstate: RealEstate) {
        this.realEstate = realEstate
        Log.d("MA RE", this.realEstate.toString())
        setEdit(true)
    }

    /**
     * To init the details or real estate inside details fragment
     */
    override fun initDetails() {
        realEstateDetails!!.init()
    }


    /**
     * @param bool Boolean
     * To set or unset edit icon
     */
    override fun setEdit(bool: Boolean) {
        if(toolMenu != null){
            if (bool) editItem.icon = enableEditIcon
            else editItem.icon = disableEditIcon
        }
    }

}