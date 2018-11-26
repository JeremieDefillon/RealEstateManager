package com.gz.jey.realestatemanager.controllers.dialog

import android.app.Activity
import android.app.Dialog
import android.support.design.widget.TextInputEditText
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.AddOrEditActivity
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.Utils

class ViewDialogSetPhotos {

    private lateinit var titleCanvas: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var inputChoice: GridLayout
    private lateinit var photoIndex: TextInputEditText

    private lateinit var cancelBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var finishBtn: Button
    private lateinit var checkMain: CheckBox
    private lateinit var image: ImageView

    private val list: ArrayList<String> = ArrayList()
    private var photos: ArrayList<Photos> = ArrayList()
    private var modif: ArrayList<Int> = ArrayList()

    private val rdb: ArrayList<RadioButton> = ArrayList()
    private var counter = 0
    private var main : Int? = null


    fun showDialog(activity: AddOrEditActivity, photos: ArrayList<Photos>, modif : ArrayList<Int>) {
        this.photos.addAll(photos)
        this.modif.addAll(modif)
        for ((i,p) in this.photos.withIndex()){
            if(p.main){
                main = i
                break
            }
        }
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_set_photos)

        titleCanvas = dialog.findViewById(R.id.title)
        inputChoice = dialog.findViewById(R.id.grid_layout)
        scrollView = dialog.findViewById(R.id.scroll_view)
        photoIndex = dialog.findViewById(R.id.photo_index)
        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        nextBtn = dialog.findViewById(R.id.next_btn)
        finishBtn = dialog.findViewById(R.id.finish_btn)
        checkMain = dialog.findViewById(R.id.check_main)

        image = dialog.findViewById(R.id.image)

        photoIndex.inputType = InputType.TYPE_CLASS_NUMBER

        cancelBtn.setOnClickListener { dialog.dismiss() }

        val editLbl = activity.getString(R.string.insert)
        val titleLbl = activity.getString(R.string.legend)
        if(counter>=modif.size-1)
            nextBtn.visibility = View.GONE

        list.addAll(activity.resources.getStringArray(R.array.photos_ind))

        nextBtn.setOnClickListener {
            var res : Int? = null
            for (i in 0 until list.size) {
                if (rdb[i].isChecked) {
                    res = i
                    break
                }
            }

            photos[modif[counter]].legend = res
            photos[modif[counter]].main = checkMain.isChecked
            counter++
            initPhoto(activity)
        }

        finishBtn.setOnClickListener {
            var res : Int? = null
            for (i in 0 until list.size) {
                if (rdb[i].isChecked) {
                    res = i
                    break
                }
            }

            if (main!=null) {
                for (p in photos)
                    p.main = false

                photos[main!!].main = true
            }

            photos[modif[counter]].legend = res
            photos[modif[counter]].main = checkMain.isChecked
            photos[modif[counter]].num = if(photoIndex.text.toString().isNotEmpty()) photoIndex.text.toString().toInt() else null
            activity.photosManager.saveLegends(this.photos)
            dialog.dismiss()
        }

        checkMain.setOnClickListener {
            if(checkMain.isChecked)
                main = modif[counter]
        }

        titleCanvas.text = "$editLbl $titleLbl"
        initPhoto(activity)
        dialog.show()
    }

    private fun initPhoto(act : AddOrEditActivity) {
        if(counter>=modif.size-1)
            nextBtn.visibility = View.GONE

        image.visibility = View.VISIBLE
        Glide.with(act)
                .load(photos[modif[counter]].image)
                .into(image)

        setCheckList(act)
    }

    private fun setCheckList(activity: Activity) {
        when (Utils.isLandscape(activity)) {
            true -> inputChoice.columnCount = Math.ceil(list.size / 5.0).toInt()
            false -> inputChoice.columnCount = Math.ceil(list.size / 10.0).toInt()
        }

        checkMain.isChecked = counter==main
        rdb.clear()
        inputChoice.removeAllViews()
        for (i in 0 until list.size) {
            val checkBtn: View = LayoutInflater.from(activity).inflate(R.layout.radio_btn, null)
            val resourceId = activity.resources.getIdentifier("check_$i", "id", activity.packageName)

            checkBtn.id = resourceId
            inputChoice.addView(checkBtn)
            rdb.add(inputChoice.getChildAt(i) as RadioButton)
            rdb[i].text = list[i]
            rdb[i].setOnClickListener {
                for (c in 0 until rdb.size)
                    if (c != i) rdb[c].isChecked = false
            }
        }

        if(photos[modif[counter]].legend!=null)
            rdb[photos[modif[counter]].legend!!].isChecked = true

        val indexValue = if(photos[modif[counter]].num!=null) photos[modif[counter]].num.toString() else ""
        photoIndex.text = SpannableStringBuilder(indexValue)
    }

}