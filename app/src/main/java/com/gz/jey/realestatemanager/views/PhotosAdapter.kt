package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Photos
import java.util.ArrayList

class PhotosAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<PhotosViewHolder>() {

    // FOR DATA
    private lateinit var context : Context
    private var photos: List<Photos>? = null

    // CALLBACK
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.photos = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.photos_item, parent, false)

        return PhotosViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PhotosViewHolder, position: Int) {
        val photo = this.photos!![position]
        viewHolder.updateWithPhotos(photo, this.context, this.callback)
    }

    override fun getItemCount(): Int {
        return this.photos!!.size
    }

    fun getAllPhotos(): List<Photos> {
        return this.photos!!
    }

    fun getPhotos(position: Int): Photos {
        return this.photos!![position]
    }

    fun updateData(photos : List<Photos>) {
        this.photos = photos
        this.notifyDataSetChanged()
    }
}