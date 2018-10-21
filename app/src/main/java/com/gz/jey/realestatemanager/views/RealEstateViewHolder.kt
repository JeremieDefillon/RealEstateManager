package com.gz.jey.realestatemanager.views

import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Surface
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
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


    fun updateWithRealEstate(item: RealEstate, img: Photos?, callback: RealEstateAdapter.Listener) {
        val res = itemView.context.resources
        this.callbackWeakRef = WeakReference(callback)
        this.type.text = if(item.type != null) res.getStringArray(R.array.type_ind)[item.type!!] else ""
        this.district.text = item.district
        this.nr.text = if(item.room != null)"T" + item.room else "T?"
        this.surface.text = if(item.surface != null) item.surface.toString() + "m2" else "surface : NC"
        this.ppm2.text = if(item.surface!=null && item.price!=null) getPPM(item.price!!, item.surface!!).toString() + res.getString(R.string.euro_symbol) else "NC"
        val currency = 1
        this.price.text = if (currency==1) Utils.convertedHighPrice(currency,item.price.toString()) + res.getString(R.string.euro_symbol)
                            else  res.getString(R.string.dollar_symbol)+Utils.convertedHighPrice(currency, item.price.toString())

        if(img!=null)
            Glide.with(itemView.context)
                .load(Uri.parse(img.image))
                .into(this.photo)
        else{
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