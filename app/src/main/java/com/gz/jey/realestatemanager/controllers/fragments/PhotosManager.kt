package com.gz.jey.realestatemanager.controllers.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogConfirmAction
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogNoResults
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogPhotoPicker
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.TempRealEstate
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.BuildItems
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
    private lateinit var mListener: PhotosManagerListener
    private val cardViews: ArrayList<CardView> = ArrayList()
    var photosList : ArrayList<Photos> = ArrayList()
    var mainPhoto : Photos? = null
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


    /**
     * @param context Context
     * On ATTACH CONTEXT TO LISTENER
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PhotosManagerListener)
            mListener = context
    }


    /**
     * TO INIT FRAGMENT
     */
    private fun init() {
        //Log.d("Photos []", tempRE!!.photos.toString())
        photosList.clear()
        val max =
                if (tempRE!!.photos != null)
                    tempRE!!.photos!!.size
                else
                    0
        Log.d("Photos [$max]", tempRE!!.photos.toString())
        if (max > 0) {
            mainPhoto = tempRE!!.mainPhoto
            for (i in 0 until max) {
                photosList.add(tempRE!!.photos!![i])
                if (i >= max - 1)
                    setAllPhotos()
            }
        } else {
            mainPhoto = null
            ViewDialogNoResults().showDialog(activity!!, Code.NO_PHOTO)
        }
    }


    /**
     * TO SET ALL PHOTOS
     */
    private fun setAllPhotos() {
        val numPic = if (Utils.isLandscape(context!!)) 4 else 3
        photos_grid.columnCount = numPic
        val size = screenX / numPic

        photos_grid.removeAllViews()
        cardViews.clear()

        if (photosList.isNotEmpty()) {
            mListener.setSave(true)
            for ((i, p) in photosList.withIndex()) {
                val c = BuildItems().photosCardView(context!!, size)
                c.setOnClickListener { selectPhoto(i) }
                val img = c.findViewById<ImageView>(R.id.photo)
                val t = c.findViewById<TextView>(R.id.legend)
                val m = c.findViewById<TextView>(R.id.main)

                val sb = StringBuilder()
                if (p.legend != 0) sb.append(resources.getStringArray(R.array.photos_ind)[p.legend])
                else sb.append("?")
                if (p.num != 0) sb.append(" " + p.num)

                t.text = sb.toString()

                if (p.image!!.isNotEmpty()) {
                    Glide.with(context!!)
                            .load(p.image)
                            .into(img)
                }

                if (tempRE!!.mainPhoto==p) m.visibility = View.VISIBLE
                else m.visibility = View.GONE

                photos_grid.addView(c)
                cardViews.add(c)
                checkSelect(i)
            }
        } else {
            ViewDialogNoResults().showDialog(activity!!, Code.NO_PHOTO)
        }
    }


    /**
     * @param i Int
     * TO SELECT PHOTO
     */
    private fun selectPhoto(i: Int) {
        photosList[i].selected = !photosList[i].selected
        checkSelect(i)
    }

    /**
     * @param i Int
     * TO CHECK SELECTED
     */
    private fun checkSelect(i: Int) {
        Log.d("PH $i SELECTED", photosList[i].selected.toString())
        photos_grid.getChildAt(i).findViewById<ImageView>(R.id.selector_check).visibility =
                if (photosList[i].selected)
                    View.VISIBLE
                else
                    View.GONE

        var c = 0

        for (s in photosList)
            c += if (s.selected) 1 else 0

        if (c > 0)
            mListener.changeToolBarMenu(2)
        else
            mListener.changeToolBarMenu(1)
    }

    /**
     * TO ADD PHOTOS
     */
    fun addPhotos() {
        ViewDialogPhotoPicker().showDialog(activity!!)
    }

    /**
     * TO DELETE PHOTOS
     */
    fun deletePhotos() {
        ViewDialogConfirmAction().showDialog(activity!!, Code.DELETE_PHOTOS)
    }

    /**
     * TO CONFIRM DELETING
     */
    fun confirmDelete() {
        val toDel: ArrayList<Photos> = ArrayList()
        for (p in photosList)
            if (p.selected)
                toDel.add(p)

        for (p in toDel)
            photosList.remove(p)

        setAllPhotos()
        mListener.savePhotos()
    }

    /**
     * TO ADD NEW PHOTO
     */
    private fun addNewPhoto(uri: String) {
        Log.d("Photo", uri)
        justApplied = true
        val ph = Photos(null, null, uri,0,0,true)
        photosList.add(ph)
        setAllPhotos()
        mListener.savePhotos()
    }

    /**
     * TO OPEN UP GALLERY
     */
    fun openGallery() {
        checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL)
    }

    /**
     * GALLERY
     */
    @SuppressLint("CheckResult")
    private fun gallery() {
        val rxImagePicker: ZhihuImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)

        rxImagePicker.openGalleryAsNormal(activity!!, ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                .maxSelectable(10)
                .countable(true)
                .spanCount(4)
                .theme(R.style.Zhihu_Dracula)
                .build())
                .subscribe { p -> addNewPhoto(p.uri.toString()) }
    }

    /**
     * TO OPEN UP CAMERA
     */
    fun openCamera() {
        checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA)
    }

    /**
     * CAMERA
     */
    @SuppressLint("CheckResult")
    private fun camera() {
        val rxImagePicker: ZhihuImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)

        rxImagePicker.openCamera(activity!!)
                .subscribe { p -> addNewPhoto(p.uri.toString()) }
    }

    // AUTHORIZATIONS
    /**
     * TO CHECK PERMISSION AND REQUEST
     * @param requestCode Int
     */
    private fun checkPermissionAndRequest(requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode)
        } else {
            onPermissionGrant(requestCode)
        }
    }

    /**
     * ON REQUEST PERMISSION RESULT
     * @param requestCode Int
     * @param permissions Array<String>
     * @param grantResults IntArray
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrant(requestCode)
        } else {
            Toast.makeText(activity!!, "Please allow the Permission first.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * ON PERMISSION GRANTED
     * @param requestCode Int
     */
    private fun onPermissionGrant(requestCode: Int) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA -> camera()
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL -> gallery()
            else -> openGallery()
        }
    }

    companion object {
        var tempRE: TempRealEstate? = null
        private var photosList : ArrayList<Photos> = ArrayList()
        /**
         * @param tempRealEstate TempRealEstate
         * @param pl ArrayList<Photos>
         * @return new RealEstateList()
         */
        fun newInstance(tempRealEstate: TempRealEstate, pl : ArrayList<Photos>): PhotosManager {
            val fragment = PhotosManager()
            tempRE = tempRealEstate
            photosList = pl
            return fragment
        }

        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL = 100
    }

    /**
     * INTERFACE FOR PHOTOS MANAGER LISTENER
     */
    interface PhotosManagerListener {
        fun changeToolBarMenu(index : Int)
        fun setSave(b : Boolean)
        fun savePhotos()
    }
}