package com.gz.jey.realestatemanager.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gz.jey.realestatemanager.R

class BuildCardView{
    fun photos(context : Context, size : Int) : CardView{

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






}