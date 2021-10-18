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

import android.widget.ArrayAdapter





private lateinit var  activitySearchBinding: ActivitySearchBinding
private var mUpOfSaleDateMinDialog: DatePickerDialog? = null
private var mDateFormat: SimpleDateFormat? = null
private lateinit var toolbar : androidx.appcompat.widget.Toolbar
private var mUpOfSaleDateMaxDialog: DatePickerDialog? = null
private val estateType: String? = null
private val city: String? = null
private const val minRooms = 0
private const val maxRooms = 0
private const val minSurface = 0
private const val maxSurface = 0
private const val minPrice = 0.0
private const val maxPrice = 0.0
private const val minUpOfSaleDate: Long = 0
private const val maxUpOfSaleDate: Long = 0
private const val photoSearch = false
private const val schoolsSearch = false
private const val parkSearch = false
private const val restaurantSearch = false
private const val storeSearch = false
private const val availableSearch = false
private val fabIntent: Intent? = null

class SearchActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view: View = activitySearchBinding.root
        setContentView(view)
        this.configureUpButton()
        this.setToolbar()

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    }

    private fun setToolbar() {
        toolbar = activitySearchBinding.includedToolbarAdd.simpleToolbar
    }

    private fun configureUpButton() {
        TODO("Not yet implemented")
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