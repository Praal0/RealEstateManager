package com.openclassrooms.realestatemanager.ui.search

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.models.SearchEstate
import java.text.SimpleDateFormat
import java.util.*


class SearchActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var  activitySearchBinding: ActivitySearchBinding
    private var mUpOfSaleDateMinDialog: DatePickerDialog? = null
    private var mUpOfSaleDateMaxDialog: DatePickerDialog? = null
    private var mUpOfSoldDateMinDialog: DatePickerDialog? = null
    private var mUpOfSoldDateMaxDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private lateinit var  estateSearch: SearchEstate
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    private var estateType: String? = null
    private var city: String? = null
    private var minRooms = 0
    private var maxRooms = 0
    private var minSurface = 0
    private var maxSurface = 0
    private var minPrice = 0.0
    private var maxPrice = 0.0
    private var minUpOfSaleDate: String = ""
    private var maxUpOfSaleDate: String = ""
    private var minSoldDate: String = ""
    private var maxSoldDate: String = ""
    private var photoSearch = false
    private var schoolsSearch = false
    private var parkSearch = false
    private var restaurantSearch = false
    private var storeSearch = false
    private var availableSearch = false
    private lateinit var fabIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view: View = activitySearchBinding.root
        setContentView(view)
        dropDownAdapters()
        this.setDateField()
        this.setDateSoldField()
        this.setToolbar()

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        onClickValidateBtn()
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

    private fun setDateSoldField() {
        activitySearchBinding.etSoldDateMin.setOnClickListener(this)
        activitySearchBinding.etSoldDateMax.setOnClickListener(this)

        //For up of sold date min
        val newCalendar = Calendar.getInstance()
        mUpOfSoldDateMinDialog = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                activitySearchBinding.etSoldDateMin.setText(mDateFormat!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        //For up of sold date max
        mUpOfSoldDateMaxDialog = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                activitySearchBinding.etSoldDateMax.setText(mDateFormat!!.format(newDate.time))
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
        if (v == activitySearchBinding.etSoldDateMin) {
            mUpOfSoldDateMinDialog?.show()
            mUpOfSoldDateMinDialog?.datePicker?.maxDate = (Calendar.getInstance().timeInMillis)
        } else if (v == activitySearchBinding.etSoldDateMax) {
            mUpOfSoldDateMaxDialog?.show()
            mUpOfSoldDateMaxDialog?.datePicker?.maxDate = Calendar.getInstance().timeInMillis
        }
    }

    /**
     * For click on fab validate btn for search
     */
    fun onClickValidateBtn() {
        activitySearchBinding.validateFabBtn.setOnClickListener {
            showSearchEstate()
            if (estateSearch != null) {
                fabIntent = Intent(applicationContext, SearchResultActivity::class.java)
                fabIntent.putExtra("estateSearch", estateSearch)
                startActivity(fabIntent)
                Log.d("SaveSearch", "saveSearch$estateSearch")
            }
        }
    }

    private fun showSearchEstate() {
        estateSearch = SearchEstate()

        if (activitySearchBinding.etEstate.text.toString().isNotEmpty()) {
            estateType = activitySearchBinding.etEstate.text.toString();
            estateSearch.estateType = estateType
        }
        if (activitySearchBinding.etCity.text.toString().isNotEmpty()) {
            city = Objects.requireNonNull(activitySearchBinding.etCity.getText()).toString();
            estateSearch.city = city
        }
        if (activitySearchBinding.etRoomsMin.text.toString().isNotEmpty()) {
            minRooms = Integer.parseInt((activitySearchBinding.etRoomsMin.text).toString())
            estateSearch.minRooms = minRooms
        }
        if (activitySearchBinding.etRoomsMax.text.toString().isNotEmpty()) {
            maxRooms = Integer.parseInt(activitySearchBinding.etRoomsMax.text.toString())
            estateSearch.maxRooms = maxRooms
        }
        if (activitySearchBinding.etSurfaceMini.text.toString().isNotEmpty()) {
            minSurface = (activitySearchBinding.etSurfaceMini.text.toString().toInt())
            estateSearch.minSurface = minSurface
        }
        if (activitySearchBinding.etSurfaceMaxi.text.toString().isNotEmpty()) {
            maxSurface = activitySearchBinding.etSurfaceMaxi.text.toString().toInt()
            estateSearch.maxSurface = maxSurface
        }
        if (activitySearchBinding.etPriceMini.text.toString().isNotEmpty()) {
            minPrice = activitySearchBinding.etPriceMini.text.toString().toDouble()
            estateSearch.minPrice = minPrice
        }
        if (activitySearchBinding.etPriceMaxi.text.toString().isNotEmpty()) {
            maxPrice = activitySearchBinding.etPriceMaxi.text.toString().toDouble()
            estateSearch.maxPrice = maxPrice
        }
        if (activitySearchBinding.etUpOfSaleDateMini.text.toString().isNotEmpty()) {
            minUpOfSaleDate = activitySearchBinding.etUpOfSaleDateMini.text.toString()
            estateSearch.minUpOfSaleDate = minUpOfSaleDate
        }
        if (activitySearchBinding.etUpOfSaleDateMaxi.text.toString().isNotEmpty()) {
            maxUpOfSaleDate = activitySearchBinding.etUpOfSaleDateMaxi.text.toString()
            estateSearch.maxOfSaleDate = maxUpOfSaleDate
        }

        if (activitySearchBinding.etSoldDateMin.text.toString().isNotEmpty()) {
            minSoldDate = activitySearchBinding.etSoldDateMin.text.toString()
            estateSearch.minSoldDate = minSoldDate
        }
        if (activitySearchBinding.etSoldDateMax.text.toString().isNotEmpty()) {
            maxSoldDate = activitySearchBinding.etSoldDateMax.text.toString()
            estateSearch.maxSoldDate = maxSoldDate
        }

        if (activitySearchBinding.photosBox.isChecked) {
            photoSearch = activitySearchBinding.photosBox.isChecked
            estateSearch.photos = photoSearch
        }
        if (activitySearchBinding.boxSchools.isChecked) {
            schoolsSearch = activitySearchBinding.boxSchools.isChecked
            estateSearch.schools = schoolsSearch
        }

        if (activitySearchBinding.boxPark.isChecked) {
            parkSearch = activitySearchBinding.boxPark.isChecked
            estateSearch.park = parkSearch
        }
        if (activitySearchBinding.boxRestaurants.isChecked) {
            restaurantSearch = activitySearchBinding.boxRestaurants.isChecked
            estateSearch.restaurants = restaurantSearch
        }
        if (activitySearchBinding.boxStores.isChecked) {
            storeSearch = activitySearchBinding.boxStores.isChecked
            estateSearch.stores = storeSearch
        }
        if (activitySearchBinding.availableBox.isChecked) {
            availableSearch = activitySearchBinding.availableBox.isChecked
            estateSearch.sold = availableSearch
        }

    }
}