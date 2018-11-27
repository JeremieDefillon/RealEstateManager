package com.gz.jey.realestatemanager.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.models.Data

class BuildItems{

    fun photosCardView(context : Context, size : Int) : CardView{
        val dim = (size*0.8f).toInt()
        val marge = (size*0.1f).toInt()

        val cardParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        cardParams.setMargins(marge,marge,marge,marge)
        cardParams.height = dim
        cardParams.width = dim

        val cardView = CardView(context)
        cardView.id = R.id.card_view
        cardView.radius = size*0.05f
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        cardView.layoutParams = cardParams
        cardView.cardElevation = marge.toFloat()

        val photoParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        val photo = ImageView(context)
        photo.id = R.id.photo
        photo.layoutParams = photoParams
        photo.adjustViewBounds = true
        photo.cropToPadding = false
        photo.scaleType = ImageView.ScaleType.FIT_XY

        val mainParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        mainParams.setMargins((size*0.02f).toInt(),(size*0.02f).toInt(),(size*0.02f).toInt(),(size*0.02f).toInt())
        val main = TextView(context)
        main.layoutParams = mainParams
        main.id = R.id.main
        main.textSize = (size*0.10f) / context.resources.displayMetrics.density
        main.text = context.getText(R.string.main)
        main.background = ContextCompat.getDrawable(context, R.drawable.main_border)
        main.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
        main.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
        main.visibility = View.GONE
        main.gravity = Gravity.TOP or Gravity.START

        val cbParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        cbParams.gravity = Gravity.TOP or Gravity.END
        val checkbox = CheckBox(context)
        checkbox.layoutParams = cbParams
        checkbox.id = R.id.selector_check
        checkbox.isClickable = false
        checkbox.gravity = Gravity.TOP or Gravity.END

        val legendParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        legendParams.setMargins(0,(dim*0.7f).toInt(),0,0)
        legendParams.height = (dim*0.3f).toInt()
        legendParams.width = dim
        legendParams.gravity = Gravity.BOTTOM

        val legendFrame = FrameLayout(context)
        legendFrame.layoutParams = legendParams

        val quote = TextView(context)
        quote.id = R.id.legend
        quote.textSize = (size*0.10f)/ context.resources.displayMetrics.density
        quote.gravity = Gravity.CENTER
        quote.textAlignment = View.TEXT_ALIGNMENT_CENTER
        quote.setPadding(0,0,0,0)
        quote.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlackAlpha))
        quote.setTextColor(Color.WHITE)
        quote.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        legendFrame.addView(quote)
        cardView.addView(photo)
        cardView.addView(main)
        cardView.addView(checkbox)
        cardView.addView(legendFrame)
        return cardView
    }

    fun reItem(context : Context, width : Int, height : Int) : View{
        val marge = (height*0.05f).toInt()
        val itemParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        itemParams.setMargins(0,0,0,0)
        itemParams.height = height

        val itemView = LinearLayout(context)
        itemView.id = R.id.button
        itemView.layoutParams = itemParams
        itemView.setPadding(0,1,0,1)
        itemView.orientation = LinearLayout.HORIZONTAL
        itemView.background = ContextCompat.getDrawable(context, R.drawable.item_border)

        val photoParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        photoParams.setMargins(0,marge,0,marge)
        photoParams.height = height
        photoParams.width = (width*0.25f).toInt()

        val photo = ImageView(context)
        photo.id = R.id.photo
        photo.layoutParams = photoParams
        photo.adjustViewBounds = true
        photo.cropToPadding = false
        photo.scaleType = ImageView.ScaleType.FIT_XY

        val textsParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        textsParams.setMargins(1,(height*0.05f).toInt(),(width*0.01f).toInt(),(height*0.05f).toInt())

        val textsLayout = LinearLayout(context)
        textsLayout.layoutParams = textsParams
        textsLayout.orientation = LinearLayout.VERTICAL

        val textParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        textParams.height = (height*0.3f).toInt()
        textParams.width = (width*0.7f).toInt()
        textParams.gravity = Gravity.CENTER or Gravity.START

        val type = TextView(context)
        type.layoutParams = textParams
        type.id = R.id.type
        type.textSize = (height*0.2f)/ context.resources.displayMetrics.density
        type.gravity = Gravity.CENTER
        type.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        type.setPadding((width*0.1f).toInt(),0,0,0)
        type.setTextColor(Color.BLACK)
        type.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        val district = TextView(context)
        district.layoutParams = textParams
        district.id = R.id.district
        district.textSize = (height*0.2f)/ context.resources.displayMetrics.density
        district.gravity = Gravity.CENTER
        district.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        district.setPadding((width*0.1f).toInt(),0,0,0)
        district.setTextColor(Color.BLACK)
        district.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        val price = TextView(context)
        price.layoutParams = textParams
        price.id = R.id.price
        price.textSize = (height*0.2f)/ context.resources.displayMetrics.density
        price.gravity = Gravity.CENTER
        price.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        price.setPadding((width*0.1f).toInt(),0,0,0)
        price.setTextColor(context.resources.getColor(R.color.colorSecondary))
        price.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        textsLayout.addView(type)
        textsLayout.addView(district)
        textsLayout.addView(price)
        itemView.addView(photo)
        itemView.addView(textsLayout)
        return itemView
    }

    fun statItem(context : Context, size : Int) : View{
        val itemParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        itemParams.setMargins(1,1,1,1)
        itemParams.gravity = Gravity.CENTER

        val itemView = LinearLayout(context)
        itemView.layoutParams = itemParams
        itemView.orientation = LinearLayout.VERTICAL
        itemView.gravity = Gravity.CENTER

        val labelView = LinearLayout(context)
        labelView.layoutParams = itemParams
        labelView.orientation = LinearLayout.HORIZONTAL

        val iconParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        iconParams.setMargins(1,1,1,1)
        iconParams.height = (size*0.2f).toInt()
        iconParams.width = iconParams.height

        val icon = ImageView(context)
        icon.id = R.id.stat_icon
        icon.layoutParams = iconParams
        icon.adjustViewBounds = true
        icon.cropToPadding = false
        icon.scaleType = ImageView.ScaleType.FIT_XY

        val labelParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        labelParams.setMargins((size*0.001f).toInt(),1,1,1)
        labelParams.width = (size*0.8f).toInt()
        labelParams.gravity = Gravity.CENTER or Gravity.START

        val label = TextView(context)
        label.layoutParams = labelParams
        label.id = R.id.stat_label
        label.textSize = (size*0.1f)/ context.resources.displayMetrics.density
        label.gravity = Gravity.CENTER
        label.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        label.setPadding(0,0,0,0)
        label.setTextColor(Color.BLACK)
        label.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        val valueParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        valueParams.setMargins((size*0.3f).toInt(),0,0,(size*0.2f).toInt())
        valueParams.width = size
        valueParams.gravity = Gravity.CENTER or Gravity.START

        val value = TextView(context)
        value.layoutParams = valueParams
        value.id = R.id.stat_value
        value.textSize = (size*0.1f)/ context.resources.displayMetrics.density
        value.gravity = Gravity.CENTER
        value.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        value.setPadding(0,0,0,0)
        value.setTextColor(Color.BLACK)
        value.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

        labelView.addView(icon)
        labelView.addView(label)
        itemView.addView(labelView)
        itemView.addView(value)
        return itemView
    }

    fun mapImage(context : Context, size : Int) : View{
        val marge = (size*0.1f).toInt()
        val itemParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        itemParams.width = (size*1.2f).toInt()
        itemParams.height = (size*1.2f).toInt()
        itemParams.setMargins(marge*2,marge,marge,marge)
        itemParams.gravity = Gravity.CENTER

        val itemView = FrameLayout(context)
        itemView.layoutParams = itemParams

        val iconParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        iconParams.setMargins(1,1,1,1)
        iconParams.height = (size*1.2f).toInt()
        iconParams.width = iconParams.height

        val icon = ImageView(context)
        icon.id = R.id.map_receiver
        icon.background = ContextCompat.getDrawable(context, R.drawable.layout_border)
        icon.setPadding(1,1,1,1)
        icon.layoutParams = iconParams
        icon.adjustViewBounds = true
        icon.cropToPadding = false
        icon.scaleType = ImageView.ScaleType.FIT_XY

        val labelParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        labelParams.setMargins((size*0.05f).toInt(),1,1,1)
        labelParams.height = (size*0.2f).toInt()
        labelParams.width = (size*0.8f).toInt()
        labelParams.gravity = Gravity.CENTER or Gravity.START

        val label = TextView(context)
        label.layoutParams = labelParams
        label.id = R.id.nf_location
        label.textSize = (size*0.1f)/ context.resources.displayMetrics.density
        label.text = context.getString(R.string.nf_on_map)
        label.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
        label.gravity = Gravity.CENTER
        label.textAlignment = View.TEXT_ALIGNMENT_CENTER
        label.setPadding(0,0,0,0)
        label.setTextColor(Color.BLACK)
        label.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

        itemView.addView(icon)
        itemView.addView(label)
        return itemView

    }
}