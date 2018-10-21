package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.utils.MyImagePicker
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.qingmei2.rximagepicker.core.RxImagePicker


class ViewDialogPhotoPicker {

    private lateinit var titleCanvas : TextView
    private lateinit var cancelBtn : Button
    private lateinit var galleryBtn : Button
    private lateinit var cameraBtn : Button
    private lateinit var galleryDraw : Drawable
    private lateinit var cameraDraw : Drawable

    private lateinit var uri : Uri

    fun showDialog(activity: AddOrEditActivity) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_photopicker)

        titleCanvas = dialog.findViewById(R.id.title)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        galleryBtn = dialog.findViewById(R.id.gallery_btn)
        cameraBtn = dialog.findViewById(R.id.camera_btn)

        galleryDraw = SetImageColor.changeDrawableColor(activity.baseContext, R.drawable.gallery, ContextCompat.getColor(activity.baseContext, R.color.colorSecondary))
        cameraDraw = SetImageColor.changeDrawableColor(activity.baseContext, R.drawable.camera, ContextCompat.getColor(activity.baseContext, R.color.colorSecondary))
        galleryBtn.background = galleryDraw
        cameraBtn.background = cameraDraw

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        galleryBtn.setOnClickListener {
            RxImagePicker
                    .create(MyImagePicker::class.java)
                    .openGallery(activity)
                    .subscribe { picR ->
                        uri = picR.uri
                        Log.d("URI", uri.toString())
                        Log.d("Path", uri.path)
                        dialog.dismiss()
                        val res : ArrayList<String> = ArrayList()
                        res.add(uri.toString())
                        ViewDialogMultiChoice().showDialog(activity, Code.LEGEND, res)
                    }
        }
        cameraBtn.setOnClickListener {
            RxImagePicker
                    .create(MyImagePicker::class.java)
                    .openCamera(activity)
                    .subscribe { picR ->
                        uri = picR.uri
                        dialog.dismiss()
                        val res : ArrayList<String> = ArrayList()
                        res.add(uri.toString())
                        ViewDialogMultiChoice().showDialog(activity, Code.LEGEND, res)
                    }
        }

        titleCanvas.text = activity.getString(R.string.pick_up_photo)
        dialog.show()
    }
}