package com.openclassrooms.realestatemanager.ui.search

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import java.text.SimpleDateFormat
import java.util.*
import android.R
import android.view.MenuItem
import android.view.MotionEvent

import android.widget.ArrayAdapter

class SearchActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var  activitySearchBinding: ActivitySearchBinding
    private var mUpOfSaleDateMinDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private var mUpOfSaleDateMaxDialog: DatePickerDialog? = null
    private val estateType: String? = null
    private val city: String? = null
    private  val minRooms = 0
    private  val maxRooms = 0
    private  val minSurface = 0
    private  val maxSurface = 0
    private  val minPrice = 0.0
    private  val maxPrice = 0.0
    private  val minUpOfSaleDate: Long = 0
    private  val maxUpOfSaleDate: Long = 0
    private  val photoSearch = false
    private  val schoolsSearch = false
    private  val parkSearch = false
    private  val restaurantSearch = false
    private  val storeSearch = false
    private  val availableSearch = false
    private val fabIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view: View = activitySearchBinding.root
        setContentView(view)
        this.configureUpButton()
        dropDownAdapters()
        this.setDateField()
        this.setToolbar()

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    }

    private fun setToolbar() {
        toolbar = activitySearchBinding.includedToolbarAdd.simpleToolbar
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.title = "Search Estate"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureUpButton() {

    }

    /**
     * For adapter dropDown
     * @param resId
     * @return
     */
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?>? {
        return ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
            resources.getStringArray(resId)
        )
    }
    /**
     * For adapter dropdown Estates Type
     */
    fun dropDownAdapters() {
        activitySearchBinding.etEstate.setAdapter(factoryAdapter(com.openclassrooms.realestatemanager.R.array.ESTATES))
        activitySearchBinding.etEstate.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    activitySearchBinding.etEstate.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))
    }

    /**
     * For date picker
     */
    private fun setDateField() {
        activitySearchBinding.etUpOfSaleDateMini.setOnClickListener(this)
        activitySearchBinding.etUpOfSaleDateMaxi.setOnClickListener(this)
        //For up of sale date min
        val newCalendar = Calendar.getInstance()
        mUpOfSaleDateMinDialog = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                activitySearchBinding.etUpOfSaleDateMini.setText(mDateFormat!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        //For up of sale date max
        mUpOfSaleDateMaxDialog = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                activitySearchBinding.etUpOfSaleDateMaxi.setText(mDateFormat!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
    }


    override fun onClick(v: View?) {
        if (v == activitySearchBinding.etUpOfSaleDateMini) {
            mUpOfSaleDateMinDialog?.show()
            mUpOfSaleDateMinDialog?.datePicker?.maxDate = (Calendar.getInstance().timeInMillis)
        } else if (v == activitySearchBinding.etUpOfSaleDateMaxi) {
            mUpOfSaleDateMaxDialog?.show()
            mUpOfSaleDateMaxDialog?.datePicker?.maxDate = Calendar.getInstance().timeInMillis
        }
    }
}