@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.gz.jey.realestatemanager.controllers.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.SetImageColor
import com.gz.jey.realestatemanager.utils.Utils

class ViewDialogBigPhotos{
    private lateinit var titleCanvas: TextView
    private lateinit var photo: ImageView
    private lateinit var prevBtn: FrameLayout
    private lateinit var nextBtn: FrameLayout
    private lateinit var prevIc: ImageView
    private lateinit var nextIc: ImageView
    private lateinit var closeBtn: ImageView
    lateinit var dialog : Dialog

    /**
     * @param activity Activity
     * @param pos Int
     * @param photos List<Photos>
     * TO SHOW DIALOG
     */
    fun showDialog(activity: Activity, pos : Int, photos : List<Photos>) {
        Data.isEdit = true
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_big_photo)

        titleCanvas = dialog.findViewById(R.id.title)
        photo = dialog.findViewById(R.id.photo)
        closeBtn = dialog.findViewById(R.id.close_btn)
        prevBtn = dialog.findViewById(R.id.prev_btn)
        nextBtn = dialog.findViewById(R.id.next_btn)
        prevIc = dialog.findViewById(R.id.prev_icon)
        nextIc = dialog.findViewById(R.id.next_icon)
        val icon = SetImageColor.changeDrawableColor(activity, R.drawable.circle_button_background, ContextCompat.getColor(activity, R.color.colorSecondary))
        prevBtn.background = icon
        nextBtn.background = icon

        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val coefBk = if(Utils.isLandscape(activity)) 0.06f else 0.08f
        val coefIc = 0.9f

        val btnSize = (size.x*coefBk).toInt()

        prevBtn.minimumWidth = btnSize
        prevBtn.minimumHeight = btnSize
        nextBtn.minimumWidth = btnSize
        nextBtn.minimumHeight = btnSize

        prevIc.minimumWidth = (btnSize*coefIc).toInt()
        prevIc.minimumHeight = (btnSize*coefIc).toInt()
        nextIc.minimumWidth = (btnSize*coefIc).toInt()
        nextIc.minimumHeight = (btnSize*coefIc).toInt()

        photo.minimumWidth = size.x
        photo.minimumHeight = if(Utils.isLandscape(activity)) size.y else (size.y*0.4f).toInt()
        closeBtn.setOnClickListener {
            dialog.dismiss()
            Data.isEdit = false
        }

        init(activity, pos, photos)
        dialog.show()
    }

    /**
     * @param activity Activity
     * @param pos Int
     * @param photos List<Photos>
     * TO INIT DIALOG
     */
    @SuppressLint("SetTextI18n")
    private fun init(activity: Activity, pos : Int, photos : List<Photos>){
        Data.photoNum = pos
        if(pos==0) prevBtn.visibility = View.GONE
        else prevBtn.visibility = View.VISIBLE

        if(pos==photos.size-1) nextBtn.visibility = View.GONE
        else nextBtn.visibility = View.VISIBLE

        val tit = photos[pos].legend
        val p = activity.resources.getStringArray(R.array.photos_ind)[tit]
        val n =  if(photos[pos].num !=0) photos[pos].num else ""

        titleCanvas.text = "$p $n"

        Glide.with(activity)
                .load(photos[pos].image)
                .into(photo)

        prevBtn.setOnClickListener { init(activity, pos-1, photos) }
        nextBtn.setOnClickListener { init(activity, pos+1, photos) }
    }
}