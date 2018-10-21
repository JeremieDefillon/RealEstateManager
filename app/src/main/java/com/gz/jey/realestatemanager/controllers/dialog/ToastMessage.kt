package com.gz.jey.realestatemanager.controllers.dialog

import android.content.Context
import android.widget.Toast
import com.gz.jey.realestatemanager.models.Code

class ToastMessage{
    fun notifyMessage(context: Context, code : Int){
        val msg = when(code){
            Code.UNEDITABLE -> "Can't Edit, please select a realEstate first !"
            Code.UNSAVABLE -> "Can't Save until you've edited all the \"*\" marked fields !"
            Code.ERROR_NOT_FOUND -> "[ERROR] : The content expected couldn't load cause it doesn't exist in the database !"
            else -> ""
        }

        if(msg.isNotEmpty()) {
            context.toast(msg)
        }
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}