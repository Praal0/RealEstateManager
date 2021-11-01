package com.openclassrooms.realestatemanager.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySearchResultBinding
import com.openclassrooms.realestatemanager.models.Estate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {

    private lateinit var activitySearchResultBinding: ActivitySearchResultBinding
    private var searchResultFragment: SearchResultFragment = SearchResultFragment()
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchResultBinding = ActivitySearchResultBinding.inflate(layoutInflater)
        val view: View = activitySearchResultBinding.root
        setContentView(view)
        this.configureAndShowSearchResultFragment()
        toolbar = activitySearchResultBinding.includedToolbar.simpleToolbar
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setTitle(R.string.result)
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
     * For declaration fragment
     */
    private fun configureAndShowSearchResultFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        searchResultFragment = SearchResultFragment()

        //Add it to FrameLayout container
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_search_result, searchResultFragment)
            .commit()
    }
}