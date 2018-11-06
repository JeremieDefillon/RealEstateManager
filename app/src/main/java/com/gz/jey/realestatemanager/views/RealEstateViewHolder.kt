package com.gz.jey.realestatemanager.views

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.Utils
import java.lang.ref.WeakReference

class RealEstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var type: TextView = itemView.findViewById(R.id.type)
    private var district: TextView = itemView.findViewById(R.id.district)
    private var price: TextView = itemView.findViewById(R.id.price)
    private var nr: TextView = itemView.findViewById(R.id.nr)
    private var surface: TextView = itemView.findViewById(R.id.surface)
    private var ppm2: TextView = itemView.findViewById(R.id.ppm2)
    private var photo: ImageView = itemView.findViewById(R.id.photo)
    private var llButton: LinearLayout = itemView.findViewById(R.id.button)

    // FOR DATA
    private var callbackWeakRef: WeakReference<RealEstateAdapter.Listener>? = null


    fun updateWithRealEstate(context : Context, item: RealEstate, callback: RealEstateAdapter.Listener) {
        val res = itemView.context.resources
        this.callbackWeakRef = WeakReference(callback)
        this.type.text = if(item.type != null) res.getStringArray(R.array.type_ind)[item.type!!] else ""
        this.district.text = item.district
        this.nr.text = Utils.getRoomNumFormat(context, item.room)
        this.surface.text = Utils.getSurfaceFormat(context, item.surface)
        this.ppm2.text = Utils.getPPMFormat(context, item.currency, item.price, item.surface)
        this.price.text = Utils.convertedHighPrice(context, item.currency, item.price)


        if(item.photos != null && item.photos!!.isNotEmpty()) {
            Glide.with(itemView.context)
                    .load(Uri.parse(item.photos!![0].image))
                    .into(this.photo)
        }else{
            val npic = ContextCompat.getDrawable(itemView.context, R.drawable.no_pict)
            Glide.with(itemView.context)
                    .load(npic)
                    .into(this.photo)
        }


        if(item.isSelected){
            llButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
        }else{
            llButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
        }
    }

    private fun getPPM(price : Int, surface: Int) : Float{
       return (price / surface).toFloat()
    }

    override fun onClick(view: View) {
        callbackWeakRef!!.get()?.onClickDeleteButton(adapterPosition)
    }
}