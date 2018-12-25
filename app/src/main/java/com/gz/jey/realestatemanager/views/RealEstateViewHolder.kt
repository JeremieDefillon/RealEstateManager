package com.gz.jey.realestatemanager.views

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.RealEstate
import com.gz.jey.realestatemanager.utils.Utils
import java.lang.ref.WeakReference

class RealEstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var type: TextView = itemView.findViewById(R.id.type)
    private var district: TextView = itemView.findViewById(R.id.district)
    private var price: TextView = itemView.findViewById(R.id.price)
    private var photo: ImageView = itemView.findViewById(R.id.photo)
    private var itemButton: FrameLayout = itemView.findViewById(R.id.button)

    // FOR DATA
    private var callbackWeakRef: WeakReference<RealEstateAdapter.Listener>? = null


    fun updateWithRealEstate(context : Context, item: RealEstate, callback: RealEstateAdapter.Listener) {
        this.callbackWeakRef = WeakReference(callback)
        this.type.text = getFirstLine(context, item.type, item.room, item.surface)
        val p = if(item.price != null)
            if(Data.currency == 1) Utils.convertDollarToEuro(item.price!!) else item.price
        else null
        this.district.text = getSecondLine(context, item.locality, p, item.surface)
        this.price.text = Utils.convertedHighPrice(context, p)

        val npic = ContextCompat.getDrawable(itemView.context, R.drawable.no_pict)
        Glide.with(itemView.context)
                .load(npic)
                .into(this.photo)

        if(item.photos != null && item.photos!!.isNotEmpty()) {
            for(p in item.photos!!){
                if(p.main){
                    Glide.with(itemView.context)
                            .load(p.image!!)
                            .into(this.photo)
                    break
                }
            }
        }

        if(item.selected>0){
            itemButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
        }else{
            itemButton.setBackgroundColor(ContextCompat.getColor(itemView.context ,R.color.colorWhite))
            price.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorSecondary))
        }
    }

    private fun getFirstLine(context: Context, type : Int?, room : Int?, surface: Int?) : String{
        val res = itemView.context.resources
        val sb = StringBuilder()
        if(type!=null) sb.append(res.getStringArray(R.array.type_ind)[type]).append("  ")
        if(room!=null) sb.append(Utils.getRoomNumFormat(context, room)).append("  ")
        if(room!=null) sb.append(Utils.getSurfaceFormat(context, surface)).append("  ")
        return if(sb.isNotEmpty()) sb.substring(0, sb.toString().length-1)
        else ""
    }

    private fun getSecondLine(context: Context, locality : String, price : Long?, surface: Int?): String{
        val sb = StringBuilder()
        if(locality.isNotEmpty()) sb.append("($locality)").append("  ")
        sb.append(Utils.getPPMFormat(context, price, surface)).append("  ")
        return if(sb.isNotEmpty()) sb.substring(0, sb.toString().length-1)
        else ""
    }

    override fun onClick(view: View) {
        callbackWeakRef!!.get()?.onClickDeleteButton(adapterPosition)
    }
}