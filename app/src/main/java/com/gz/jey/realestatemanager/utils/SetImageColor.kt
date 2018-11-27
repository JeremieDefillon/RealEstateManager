package com.gz.jey.realestatemanager.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import java.io.ByteArrayOutputStream

object SetImageColor{

    /**
     * CHANGE DRAWABLE COLOR
     * @param context Context
     * @param icon Int
     * @param newColor Int
     * @return Drawable
     */
    fun changeDrawableColor(context: Context, icon: Int, newColor: Int): Drawable {
        val mDrawable = ContextCompat.getDrawable(context, icon)?.mutate() as Drawable
        mDrawable.setColorFilter(newColor, PorterDuff.Mode.SRC_IN)
        return mDrawable
    }

    /**
     * CHANGE BITMAP COLOR
     * @param sourceBitmap Bitmap
     * @param color Int
     * @return Bitmap
     */
    fun changeBitmapColor(sourceBitmap: Bitmap, color: Int) : Bitmap {
        val resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.width-10, sourceBitmap.height-10)
        val p = Paint()
        val filter = LightingColorFilter(color, 1)
        p.colorFilter = filter
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0f, 0f, p)

        return resultBitmap
    }


    fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }

    fun drawableToBitmap(image: Drawable): ByteArray {
        val bitmap = (image as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }

}