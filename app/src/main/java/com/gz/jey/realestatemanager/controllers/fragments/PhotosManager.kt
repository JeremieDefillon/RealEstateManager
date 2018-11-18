package com.gz.jey.realestatemanager.controllers.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogNoResults
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogPhotoPicker
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogSetPhotos
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.BuildCardView
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.utils.ZhihuImagePicker
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder
import kotlinx.android.synthetic.main.photos_manager.*


class PhotosManager : Fragment() {

    private var mView: View? = null
    private var screenX = 0
    private var screenY = 0

    // FOR DATA
    lateinit var act: AddOrEditActivity
    val cardViews: ArrayList<CardView> = ArrayList()
    var photosList: ArrayList<Photos> = ArrayList()
    val checkList: ArrayList<CheckBox> = ArrayList()
    val photosSelected: ArrayList<Int> = ArrayList()
    private var justApplied = false

    // FOR DESIGN

    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.photos_manager, container, false)
        act = activity as AddOrEditActivity
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenX = size.x
        screenY = size.y
        return mView
    }

    /**
     * CALLED WHEN VIEW CREATED
     * @param view View
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        photosList.clear()
        val max = if(act.tempRE!!.photos != null) act.tempRE!!.photos!!.size else 0
        if(max>0){
            Log.d("Photos [$max]", act.tempRE!!.photos.toString())
            for (i in 0 until max){
                photosList.add(act.tempRE!!.photos!![i])
                if (i >= max-1)
                    setAllPhotos()
            }
        }else{
            act.setSave(false)
            ViewDialogNoResults().showDialog(act, Code.NO_PHOTO)
        }

        Log.d("Photos [$max]", photosList.toString())
    }

    private fun setAllPhotos() {
        val numPic = if (Utils.isLandscape(context!!)) 4 else 3
        photos_grid.columnCount = numPic
        val size = screenX / numPic

        photos_grid.removeAllViews()
        photosSelected.clear()
        cardViews.clear()
        checkList.clear()

        if (photosList.isNotEmpty()) {
            act.setSave(true)
            for ((i, p) in photosList.withIndex()) {
                val c = BuildCardView().photos(context!!, size)
                c.setOnClickListener { selectPhoto(i) }
                val img = c.findViewById<ImageView>(R.id.photo)
                val t = c.findViewById<TextView>(R.id.legend)
                val ch = c.findViewById<CheckBox>(R.id.selector_check)
                val m = c.findViewById<TextView>(R.id.main)

                checkList.add(ch)

                val sb = StringBuilder()
                if (p.legend != null) sb.append(resources.getStringArray(R.array.photos_ind)[p.legend!!])
                else sb.append("?")
                if (p.num != null) sb.append(" " + p.num!!)
                t.text = sb.toString()

                if (p.image!!.isNotEmpty()) {
                    Glide.with(act)
                            .load(p.image)
                            .into(img)
                }

                if (p.main) m.visibility = View.VISIBLE
                else m.visibility = View.GONE

                photos_grid.addView(c)
                cardViews.add(c)

                if (p.legend == null)
                    selectPhoto(i)
            }
        } else {
            act.setSave(false)
            ViewDialogNoResults().showDialog(act, Code.NO_PHOTO)
        }
    }

    private fun selectPhoto(i: Int) {
        checkList[i].isChecked = !checkList[i].isChecked
        photos_grid.getChildAt(i).findViewById<CheckBox>(R.id.selector_check).isChecked = checkList[i].isChecked
        if (checkList[i].isChecked)
            photosSelected.add(i)
        else
            photosSelected.remove(i)

        if (photosSelected.isNotEmpty())
            act.changeToolBarMenu(2)
        else
            act.changeToolBarMenu(1)

        Log.d("PHOTO SELECT", photosSelected.toString())
    }

    fun addPhotos() {
        ViewDialogPhotoPicker().showDialog(act)
    }

    fun deletePhotos() {
        ViewDialogPhotoPicker().showDialog(act)
    }

    private fun addNewPhoto(uri: String) {
        Log.d("Photo", uri)
        justApplied = true
        val ph = Photos(null, uri, null, null, false)
        photosList.add(ph)
        setAllPhotos()
    }

    fun savePhotos() {
        act.tempRE!!.photos = photosList
        act.setFragment(0)
    }

    fun editLegends() {
        ViewDialogSetPhotos().showDialog(act, photosList, photosSelected)
    }

    fun saveLegends(p: ArrayList<Photos>) {
        photosList.clear()
        photosSelected.clear()
        photosList.addAll(p)
        Log.d("P LIST", photosList.toString())
        setAllPhotos()
        checkMain()
    }

    fun openGallery() {
        checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL)
    }

    @SuppressLint("CheckResult")
    private fun gallery() {
        val rxImagePicker: ZhihuImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)

        rxImagePicker.openGalleryAsNormal(act, ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                .maxSelectable(10)
                .countable(true)
                .spanCount(4)
                .theme(R.style.Zhihu_Dracula)
                .build())
                .subscribe { p -> addNewPhoto(p.uri.toString()) }
    }

    fun openCamera() {
        checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA)
    }

    @SuppressLint("CheckResult")
    private fun camera() {
        val rxImagePicker: ZhihuImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)

        rxImagePicker.openCamera(act)
                .subscribe { p -> addNewPhoto(p.uri.toString()) }
    }

    private fun checkMain() {
        for (c in cardViews)
            c.findViewById<TextView>(R.id.main).visibility = View.GONE

        for ((i, p) in photosList.withIndex())
            if (p.main) {

                cardViews[i].findViewById<TextView>(R.id.main).visibility = View.VISIBLE
                break
            }
    }

    private fun checkPermissionAndRequest(requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode)
        } else {
            onPermissionGrant(requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrant(requestCode)
        } else {
            Toast.makeText(act, "Please allow the Permission first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPermissionGrant(requestCode: Int) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA -> camera()
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL -> gallery()
            else -> openGallery()
        }
    }

    companion object {
        /**
         * @param addOrEditActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(addOrEditActivity: AddOrEditActivity): PhotosManager {
            val fragment = PhotosManager()
            fragment.act = addOrEditActivity
            return fragment
        }

        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL = 100
    }
}