package com.gz.jey.realestatemanager.views

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.RealEstateViewModel
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.Utils
import java.lang.ref.WeakReference

class RealEstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var type: TextView = itemView.findViewById(R.id.real_estate_type)
    private var district: TextView = itemView.findViewById(R.id.real_estate_district)
    private var price: TextView = itemView.findViewById(R.id.real_estate_price)
    private var photo: ImageView = itemView.findViewById(R.id.real_estate_photo)
    private var llButton: LinearLayout = itemView.findViewById(R.id.real_estate_button)

    // FOR DATA
    private var callbackWeakRef: WeakReference<RealEstateAdapter.Listener>? = null

    fun updateWithRealEstate(item: RealEstate, mainPhoto:Photos?, context : Context, callback: RealEstateAdapter.Listener) {
        val res = itemView.context.resources
        this.callbackWeakRef = WeakReference(callback)
        this.type.text =  res.getStringArray(R.array.type_ind)[item.type!!]
        this.district.text = item.district
        this.price.text = if (Data.currency==1) Utils.convertedHighPrice(item.price.toString()) + res.getString(R.string.euro_symbol)
                            else  res.getString(R.string.dollar_symbol)+Utils.convertedHighPrice(item.price.toString())

        if(mainPhoto!=null){
            val imgLink = mainPhoto.image
            Glide.with(context)
                    .load(imgLink)
                    .into(photo)
        }else{
            this.photo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_pict))
        }

        if(item.isSelected){
            llButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
        }else{
            llButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
        }
    }

    override fun onClick(view: View) {
        callbackWeakRef!!.get()?.onClickDeleteButton(adapterPosition)
    }
}