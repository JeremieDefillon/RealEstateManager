package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity: AppCompatActivity() {

    private var textViewMain: TextView? = null
    private var textViewQuantity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1st ERROR => this textViewMain appear null into debugger (the resources id isn't correct)
        // this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        // FIXED VERSION
        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)

        this.configureTextViewMain()
        this.configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        this.textViewMain!!.textSize = 15f
        this.textViewMain!!.text = "Le premier bien immobilier enregistré vaut "
    }



    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        this.textViewQuantity!!.textSize = 20f
        // 2nd ERROR => Quantity is expected as a string and not an int
        // this.textViewQuantity.setText(quantity);
        // FIXED VERSION using "String.valueOf(quantity) in Java or toString() in Kotlin"
        this.textViewQuantity!!.text = quantity.toString()
    }
}