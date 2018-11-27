package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.sql.Photos
import java.lang.ref.WeakReference

class PhotosViewHolder(photoView: View) : RecyclerView.ViewHolder(photoView), View.OnClickListener {

    private var photo: ImageView = photoView.findViewById(R.id.photo)
    private var legend: TextView = photoView.findViewById(R.id.legend)
    private var cb: TextView = photoView.findViewById(R.id.selector_check)

    // FOR DATA
    private var callbackWeakRef: WeakReference<PhotosAdapter.Listener>? = null

    fun updateWithPhotos(photo: Photos, context : Context, callback: PhotosAdapter.Listener) {
        val res = context.resources
        this.cb.visibility = View.GONE
        this.callbackWeakRef = WeakReference(callback)
        val leg = if(photo.legend!=0) res.getStringArray(R.array.photos_ind)[photo.legend] else "?"
        val num = if(photo.num!=0) photo.num.toString() else ""
        this.legend.text = "$leg $num"
        Glide.with(context)
                .load(photo.image)
                .into(this.photo)
    }

    override fun onClick(view: View) {
        callbackWeakRef!!.get()?.onClickDeleteButton(adapterPosition)
    }
}