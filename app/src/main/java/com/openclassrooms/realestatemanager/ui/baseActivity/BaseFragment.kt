package com.openclassrooms.realestatemanager.ui.baseActivity

import android.content.Context
import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    private val estateViewModel: EstateViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    fun showDialog(context : Context, estateId : Long): Boolean {
        var result : Boolean = true

        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.delete_message)
        builder.setCancelable(true)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                estateViewModel.deleteEstate(estateId)
                locationViewModel.deleteLocation(estateId)
            })

        builder.setNegativeButton("No") { dialog, id ->
            result = false
            dialog.cancel()
            activity?.finish()
            startActivity(activity?.intent)}

        val alert: AlertDialog = builder.create()
        alert.show()

        return result
    }
}