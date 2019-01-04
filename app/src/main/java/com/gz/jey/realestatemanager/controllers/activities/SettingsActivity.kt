package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogConfirmAction
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import android.provider.MediaStore
import kotlin.collections.ArrayList


class SettingsActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    private lateinit var dollarIcon: Drawable
    private lateinit var euroIcon: Drawable

    // FOR DATA
    private lateinit var itemViewModel: ItemViewModel

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_settings)
        Data.lang = if(Locale.getDefault().language =="fr") 1 else 0
        initActivity()
    }

    /**
     * INIT ACTIVITY
     */
    private fun initActivity() {
        this.configureToolBar()
        this.setViewModel()
        this.setIcons()
        dollar_btn.setCompoundDrawables(null, null, dollarIcon, null)
        euro_btn.setCompoundDrawables(null, null, euroIcon, null)
        dollar_btn.setOnClickListener { setCurrency(0) }
        euro_btn.setOnClickListener { setCurrency(1) }
        notif_switch.isChecked = Data.enableNotif
        notif_switch.setOnCheckedChangeListener { _, isChecked ->
            Data.enableNotif = isChecked
        }
        setCurrency(Data.currency)
        insert_real_estates.setOnClickListener { ViewDialogConfirmAction().showDialog(this, Code.INSERT_RE) }
        delete_real_estates.setOnClickListener { ViewDialogConfirmAction().showDialog(this, Code.DELETE_RE) }
    }

    /**
     * CONFIGURE TOOLBAR
     */
    private fun configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar)
        toolbar!!.title = "Settings"
        setSupportActionBar(toolbar)
        invalidateOptionsMenu()
        Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { backToMainActivity() }
    }

    private fun setViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
    }

    /**
     * CONFIGURE ICONS
     */
    private fun setIcons() {
        dollarIcon = SetImageColor.changeDrawableColor(this, R.drawable.dollar, ContextCompat.getColor(this, R.color.colorSecondaryDark))
        euroIcon = SetImageColor.changeDrawableColor(this, R.drawable.euro, ContextCompat.getColor(this, R.color.colorSecondaryDark))
    }

    // ACTIONS

    private fun setCurrency(c: Int) {
        Data.currency = c
        dollar_btn.isChecked = Data.currency == 0
        euro_btn.isChecked = Data.currency == 1
    }

    fun deleteRE() {
        itemViewModel.getAllRealEstate()
                .observe(this, Observer<List<RealEstate>> { re ->
                    deleteRealEstateList(re!!)
                })
    }

    fun insertRE() {
        val data = getAssetJsonData()
        if (data != null) {
            val js = getGson(data)
            Log.d("JSON", js.toString())
            val files = getFilePaths()


            for (re in js) {

                for (ph in re.photos!!){
                    val maybe = if(ph.num != 0) "_"+ph.num else ""
                    val pa = re.id.toString() + "_" + ph.legend+maybe+".jpg"

                    for (p in files){
                        val f = File(p)
                        val imageName = f.name
                        if(imageName == pa){
                            ph.image = f.absolutePath
                            break
                        }
                    }
                }

                itemViewModel.createRealEstate(re)
            }
            backToMainActivity()
        } else {
            Log.d("JSON", "NULL")
        }
    }


    private fun deleteRealEstateList(re: List<RealEstate>) {
        for (r in re) {
            itemViewModel.deleteRealEstate(r.id!!)
        }
    }

    private fun getAssetJsonData(): String? {
        val fileName = "all_re.json"
        val json: String?
        json = try {
            val inputStream: InputStream = assets.open(fileName)
            val inputStreamReader = InputStreamReader(inputStream)
            val br = BufferedReader(inputStreamReader)
            val line = br.readText()
            br.close()
            line
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
            null
        }

        return json
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Code.SETTINGS, true)
        startActivity(intent)
        finish()
    }

    private fun getGson(data: String): List<RealEstate> {
        val gson = Gson()
        val listType = object : TypeToken<List<RealEstate>>() {}.type
        return gson.fromJson(data, listType)
    }

    private fun getFilePaths(): ArrayList<String> {

        val u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        var c: Cursor? = null
        val dirList = TreeSet<String>()
        val resultIAV = ArrayList<String>()

        var directories: Array<String?> = arrayOfNulls(0)
        if (u != null) {
            Log.d("URI ", u.toString())
            c = managedQuery(u, projection, null, null, null)
        }

        if (c != null && c.moveToFirst()) {
            do {
                var tempDir = c.getString(0)
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"))
                try {
                    dirList.add(tempDir)
                } catch (e: Exception) {
                    Log.e("ERROR DIR LIST", e.toString())
                }

            } while (c.moveToNext())
            directories = arrayOfNulls(dirList.size)
            dirList.toArray(directories)

        }

        for (i in 0 until dirList.size) {
            val imageDir = File(directories[i])
            var imageList: Array<File>? = imageDir.listFiles() ?: continue
            for (imagePath in imageList!!) {

                try {

                    if (imagePath.isDirectory) {
                        imageList = imagePath.listFiles()

                    }
                    if (imagePath.name.contains(".jpg") || imagePath.name.contains(".JPG")
                            || imagePath.name.contains(".jpeg") || imagePath.name.contains(".JPEG")
                            || imagePath.name.contains(".png") || imagePath.name.contains(".PNG")
                            || imagePath.name.contains(".gif") || imagePath.name.contains(".GIF")
                            || imagePath.name.contains(".bmp") || imagePath.name.contains(".BMP")) {


                        val path = imagePath.absolutePath
                        resultIAV.add(path)

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("ERROR IMG PATH", e.toString())
                }
                //  }
            }
        }

        return resultIAV
    }

}