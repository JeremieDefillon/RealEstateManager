package com.gz.jey.realestatemanager.controllers.activities

import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.Observer
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
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.dialog.ToastMessage
import com.gz.jey.realestatemanager.controllers.dialog.ViewDialogAmortizationTable
import com.gz.jey.realestatemanager.injection.Injection
import com.gz.jey.realestatemanager.injection.ItemViewModel
import com.gz.jey.realestatemanager.models.Amortizations
import com.gz.jey.realestatemanager.models.Code
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.views.AmortizationsAdapter
import kotlinx.android.synthetic.main.activity_loan_simulator.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class LoanSimulator : AppCompatActivity(), AmortizationsAdapter.Listener {

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
        toolbar!!.setNavigationOnClickListener { backToMainActivity() }
    }


    override fun onClickDeleteButton(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * INIT ACTIVITY
     */
    private fun init() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        val itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)

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

           val it0 = Amortizations(1, monthlyFee, interestFee0, capRefund0, capFee0)
           amortizations.add(it0)

           for(i in 1 until months){
               val interestFee = getInterestCost(amortizations[i-1].capital_fee)
               val capRefund = monthlyFee - interestFee
               val capFee = (amortizations[i-1].capital_fee - capRefund)

               val it = if(i==months-1 && capFee >0.0f)
                   Amortizations(i+1, monthlyFee+capFee, interestFee, capRefund+capFee, 0.00f)
               else
                   Amortizations(i+1, monthlyFee, interestFee, capRefund, capFee)

               amortizations.add(it)
           }

           amortization_btn.visibility = View.VISIBLE
       }else {
          ToastMessage().notifyMessage(this,Code.ERROR_CALCULATE)
          amortization_btn.visibility = View.GONE
       }
    }

    private fun openAmortization(){
        ViewDialogAmortizationTable().showDialog(this, amortizations)
    }

    // FORMULE
    private fun getMonthlyFee(capital : Float , rate : Float, years : Int) : Float{
        val K = capital
        val T = (rate/100f)/12f
        val d = years * 12
        val va = ( K * T ) / ( 1 - Math.pow(( 1 + T.toDouble() ), -d.toDouble()).toFloat() )

        return (va*100)/100.toFloat()
    }

    private fun getInterestCost(capital : Float) : Float{
        val va = (capital * (rate!!/100)) / 12f

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR

        return (va*100)/100.toFloat()
    }

    private fun showKeyboard(v : View){
        v.requestFocus()
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(v, 0)
    }

    private fun closeKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}