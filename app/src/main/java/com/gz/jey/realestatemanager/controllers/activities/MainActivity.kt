package com.gz.jey.realestatemanager.controllers.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
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
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.fragments.InputEditor
import com.gz.jey.realestatemanager.controllers.fragments.RealEstateList
import com.gz.jey.realestatemanager.controllers.fragments.SetRealEstate
import java.util.*

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // FRAGMENTS
    private val TAG = "MainActivity"
    var lastFragment: Fragment? = null
    var setRealEstate: SetRealEstate? = null
    var realEstateList: RealEstateList? = null

    // FOR PERMISSIONS

    // TASK CODE

    // FOR POSITION
    // FOR DESIGN
    private var toolMenu : Menu? = null
    var fragmentContainer : FrameLayout? = null
    private var drawerLayout: DrawerLayout? = null
    var toolbar: Toolbar? = null
    private var navigationView: NavigationView? = null
    var loading : FrameLayout? = null
    private var loadingContent : TextView? = null

    // FOR DATA
    var addOrEdit : Int? = null
    private var changeMenu = false
    private var existMenu : ArrayList<Boolean>? = null

    // FOR REALESTATE SELECTOR
    var reID: String? = null

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        setLang()
        //loading = findViewById(R.id.loading)
        //loadingContent = findViewById(R.id.content_loading)
        //loadingContent!!.text = getString(R.string.initActivity)
        //setLoading(true, true)
        fragmentContainer = findViewById(R.id.fragmentContainer)
        existMenu = arrayListOf(true, true, true, false)

        //Log.d("ENABLE NOTIF ???", Data.enableNotif.toString())

        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras != null){
               // fromNotif = extras.getBoolean("NotiClick")
                reID = extras.getString("RestaurantId")
            }

           // if(Data.enableNotif && !fromNotif)
             //   setNotification()

            initActivity()
        }
    }

    private fun setLang(){
       // Data.lang = if(Locale.getDefault().displayLanguage=="fr") 1 else 0
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        //saveDatas()
        this.configureToolBar()
        this.setDrawerLayout()
    }

    /**
     * @param menu Menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolMenu = menu
        // Inflate the menu and add it to the Toolbar
        menuInflater.inflate(R.menu.menu_toolbar, toolMenu)
        if(changeMenu){
            for (i in 0 until menu.size())
                menu.getItem(i).isVisible = existMenu!![i]
            changeMenu = false
        }

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
                addOrEdit = null
                addFragment(0)
                true
            }
            R.id.edit -> {
                if(addOrEdit!=null){
                    addFragment(0)
                    true
                }else{
                    Log.e("EDIT FAILED", "NULL ITEM")
                    true
                }
            }
            R.id.search_on -> {
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
    private fun setFragment(index: Int){
        changeToolBarMenu(1)
        //loadingContent!!.text = getString(R.string.loadingView)
        //hideKeyboard()
       // if (fromNotif) {
         //   fromNotif=false
          //  execRequest(CODE_DETAILS)
        //}else {
            var fragment: Fragment? = null
            invalidateOptionsMenu()
           // Data.tab = index
            when (index) {
                0 -> {
                    this.realEstateList = RealEstateList.newInstance(this)
                    fragment = this.realEstateList
                }

            }


            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()

    }

    private fun addFragment(index: Int){
        changeToolBarMenu(0)
        var fragment: Fragment? = null
        invalidateOptionsMenu()
        // Data.tab = index
        when (index) {
            0 -> {
                this.setRealEstate = SetRealEstate.newInstance(this)
                fragment = this.setRealEstate
            }

        }
        lastFragment = fragment
        this.supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }

    fun openInputEditor(){
        val fragment: Fragment = InputEditor.newInstance(this)
        lastFragment = fragment
        this.supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }

    fun changeToolBarMenu(em : Int){
        when(em){
            0-> {
                existMenu = arrayListOf(false,false,false,false)
                invalidateOptionsMenu()
                Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                toolbar!!.setNavigationOnClickListener {
                    //setLoading(true, true)
                    val fragment: Fragment = lastFragment!!
                    this.supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()

                    changeToolBarMenu(1)
                }
            }
            1-> {
                existMenu = arrayListOf(true,true,true,false)
                invalidateOptionsMenu()
                Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.menu)
                setDrawerLayout()
            }
            2-> {
                existMenu = arrayListOf(false,false,false,true)
                invalidateOptionsMenu()
                Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                toolbar!!.setNavigationOnClickListener {
                    //setLoading(true, true)
                    val fragment: Fragment = lastFragment!!
                    this.supportFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                    changeToolBarMenu(1)
                }
            }
        }

        changeMenu = true
    }

}