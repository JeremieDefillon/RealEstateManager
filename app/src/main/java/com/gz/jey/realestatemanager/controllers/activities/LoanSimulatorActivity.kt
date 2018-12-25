package com.gz.jey.realestatemanager.controllers.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogAmortizationTable
import com.gz.jey.realestatemanager.models.Amortizations
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.utils.Utils
import com.gz.jey.realestatemanager.views.AmortizationsAdapter
import kotlinx.android.synthetic.main.activity_loan_simulator.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Suppress("DEPRECATION")
class LoanSimulatorActivity : AppCompatActivity(), AmortizationsAdapter.Listener {

    private val amortizations : ArrayList<Amortizations> = ArrayList()
    var toolbar: Toolbar? = null

    private var totalCapital : Float? = null
    private var years : Int? = null
    private var rate : Float? = null
    private var months = 0
    private var monthlyFee = 0f

    /**
     * @param savedInstanceState Bundle
     * CREATE ACTIVITY
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_loan_simulator)
        configureToolBar()
        init()
    }

    /**
     * CONFIGURE TOOLBAR
     */
    private fun configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar)
        toolbar!!.title = getString(R.string.loan_simulator)
        setSupportActionBar(toolbar)
        invalidateOptionsMenu()
        Objects.requireNonNull<ActionBar>(supportActionBar).setHomeAsUpIndicator(R.drawable.back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { Utils.backToMainActivity(this) }
    }

    /**
     * @param position Int
     * ON CLICK DELETE BUTTON
     */
    override fun onClickDeleteButton(position: Int) {
        //Never used
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {

        amortization_btn.visibility = View.GONE
        amortization_btn.setOnClickListener { openAmortization() }
        calculate_btn.setOnClickListener { calculate() }
        val symb = if(Data.currency==1) getString(R.string.euro_symbol) else getString(R.string.dollar_symbol)
        val month = getString(R.string.months)
        monthly_value.hint = "$symb / $month"

        capital_value.inputType = InputType.TYPE_CLASS_NUMBER
        rate_value.keyListener = DigitsKeyListener(true, true)
        years_value.inputType = InputType.TYPE_CLASS_NUMBER

        capital_value.hint = "?"
        capital_symb.text = symb
        rate_value.hint = "?"
        years_value.hint = "?"
        monthly_value.hint = "?"

        capital_btn.setOnClickListener { showKeyboard(capital_value) }
        rate_btn.setOnClickListener { showKeyboard(rate_value) }
        years_btn.setOnClickListener { showKeyboard(years_value) }

        capital_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                totalCapital = if(editable.toString().isNotEmpty()) editable.toString().toFloat()
                else null
            }
        })
        rate_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                rate = if(editable.toString().isNotEmpty()) editable.toString().toFloat()
                else null
            }
        })
        years_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                years = if(editable.toString().isNotEmpty()) editable.toString().toInt()
                else null
            }
        })
    }

    /**
     * TO CALCULATE LOAN'S DETAILS
     */
    private fun calculate(){
        closeKeyboard()
       if(totalCapital!=null && rate!=null && years!=null){
           amortizations.clear()
           monthlyFee = getMonthlyFee(totalCapital!!, rate!!, years!!)
           val symb = if(Data.currency==1) getString(R.string.euro_symbol) else getString(R.string.dollar_symbol)
           val month = getString(R.string.months)
           val editable = SpannableStringBuilder("%.2f".format(monthlyFee) + " $symb / $month")
           monthly_value.text = editable
           months = years!! * 12

           val interestFee0 = getInterestCost(totalCapital!!)
           val capRefund0 = monthlyFee-interestFee0
           val capFee0 = (totalCapital!! - capRefund0)

           var totm = 0f
           var toti = 0f
           var totc = 0f

           val it0 = Amortizations(1, monthlyFee, interestFee0, capRefund0, capFee0)
           amortizations.add(it0)
           totm += monthlyFee
           toti += interestFee0
           totc += capRefund0

           for(i in 1 until months){
               val interestFee = getInterestCost(amortizations[i-1].capital_fee)
               val capRefund = monthlyFee - interestFee
               val capFee = (amortizations[i-1].capital_fee - capRefund)

               val it = Amortizations(i+1, monthlyFee, interestFee, capRefund, capFee)
               amortizations.add(it)

               totm += monthlyFee
               toti += interestFee
               totc += capRefund
           }

           if(totc > totalCapital!!) {
               Log.d("CAPITAL MORE", totc.toString())
               val over = totc-totalCapital!!
               amortizations[months-1].monthly_payed = amortizations[months-1].monthly_payed - over
               amortizations[months-1].capital_refunded = amortizations[months-1].capital_refunded - over
               amortizations[months-1].capital_fee = 0.00f
           }else if(totc < totalCapital!!) {
               Log.d("CAPITAL LESS", totc.toString())
               val miss = totalCapital!!-totc
               amortizations[months-1].monthly_payed = amortizations[months-1].monthly_payed + miss
               amortizations[months-1].capital_refunded = amortizations[months-1].capital_refunded + miss
               amortizations[months-1].capital_fee = 0.00f
           }

           amortization_btn.visibility = View.VISIBLE
       }else {
          ToastMessage().notifyMessage(this,Code.ERROR_CALCULATE)
          amortization_btn.visibility = View.GONE
       }
    }

    /**
     * TO OPEN AMORTIZATION TABLE
     */
    private fun openAmortization(){
        ViewDialogAmortizationTable().showDialog(this, amortizations)
    }

    /**
     * TO GET MONTHLY FEE
     * @param capital Float
     * @param rate Float
     * @param years Int
     * @return Float
     */
    private fun getMonthlyFee(capital : Float , rate : Float, years : Int) : Float{
        val t = (rate/100f)/12f
        val d = years * 12
        val va = ( capital * t ) / ( 1 - Math.pow(( 1 + t.toDouble() ), -d.toDouble()).toFloat() )

        return (va*100)/100.toFloat()
    }

    /**
     * TO GET INTEREST COST
     * @param capital Float
     * @return Float
     */
    private fun getInterestCost(capital : Float) : Float{
        val va = (capital * (rate!!/100)) / 12f

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR

        return (va*100)/100.toFloat()
    }

    /**
     * TO OPEN KEYBOARD
     * @param v View
     */
    private fun showKeyboard(v : View){
        v.requestFocus()
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(v, 0)
    }

    /**
     * TO CLOSE KEYBOARD
     */
    private fun closeKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}