package com.openclassrooms.realestatemanager.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.R
import android.view.View
import com.openclassrooms.realestatemanager.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {

    private lateinit var activitySearchResultBinding: ActivitySearchResultBinding
    private var searchResultFragment: SearchResultFragment = SearchResultFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchResultBinding = ActivitySearchResultBinding.inflate(layoutInflater)
        val view: View = activitySearchResultBinding.root
        setContentView(view)
        //this.configureAndShowSearchResultFragment()

    }

    /**
     * For declaration fragment
     */
    private fun configureAndShowSearchResultFragment() {
//        Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        searchResultFragment = supportFragmentManager.findFragmentById(com.openclassrooms.realestatemanager.R.id.fragment_search_result) as SearchResultFragment
        if (searchResultFragment == null) {
            //Create new main fragment
            searchResultFragment = SearchResultFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(com.openclassrooms.realestatemanager.R.id.fragment_search_result, searchResultFragment)
                .commit()
        }
    }
}