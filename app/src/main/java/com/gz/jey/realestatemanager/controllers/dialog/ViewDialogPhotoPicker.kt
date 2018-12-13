package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.utils.SetImageColor

class ViewDialogPhotoPicker {

    private lateinit var act : AddOrEditActivity
    private lateinit var titleCanvas : TextView
    private lateinit var cancelBtn : Button
    private lateinit var galleryBtn : LinearLayout
    private lateinit var cameraBtn : LinearLayout
    private lateinit var galleryImg : ImageView
    private lateinit var cameraImg : ImageView
    private lateinit var galleryDraw : Drawable
    private lateinit var cameraDraw : Drawable

    private lateinit var uri : Uri

    fun showDialog(activity: Activity) {
        act = activity as AddOrEditActivity
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_photo_picker)

        titleCanvas = dialog.findViewById(R.id.title)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        galleryBtn = dialog.findViewById(R.id.gallery_btn)
        cameraBtn = dialog.findViewById(R.id.camera_btn)
        galleryImg = dialog.findViewById(R.id.gallery_img)
        cameraImg = dialog.findViewById(R.id.camera_img)

        galleryDraw = SetImageColor.changeDrawableColor(activity.baseContext, R.drawable.gallery, ContextCompat.getColor(activity.baseContext, R.color.colorSecondary))
        cameraDraw = SetImageColor.changeDrawableColor(activity.baseContext, R.drawable.camera, ContextCompat.getColor(activity.baseContext, R.color.colorSecondary))
        galleryImg.background = galleryDraw
        cameraImg.background = cameraDraw

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        galleryBtn.setOnClickListener {
            act.photosManager.openGallery()
            dialog.dismiss()
        }
        cameraBtn.setOnClickListener {
            act.photosManager.openCamera()
            dialog.dismiss()
        }

        titleCanvas.text = activity.getString(R.string.pick_up_photo)
        dialog.show()
    }
}