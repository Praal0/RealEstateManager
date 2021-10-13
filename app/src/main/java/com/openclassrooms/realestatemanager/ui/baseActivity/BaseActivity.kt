package com.openclassrooms.realestatemanager.ui.baseActivity

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.ImageDialog
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import android.content.DialogInterface
import androidx.activity.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private val RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION = 100
    private val estateViewModel: EstateViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val CAM_AND_READ_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * For permissions camera and read external storage
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun showDialog(context : Context,estateId : Long): Boolean {
        var result : Boolean = true

        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.delete_message)
        builder.setCancelable(true)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                finish()
                estateViewModel.deleteEstate(estateId)
            })

        builder.setNegativeButton("No") { dialog, id ->
            result = false
            dialog.cancel() }

        val alert: AlertDialog = builder.create()
        alert.show()

        return result
    }

    /**
     * For permissions
     */
    protected fun methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, *CAM_AND_READ_EXTERNAL_STORAGE)) {
            Log.d("Permissions", "Permissions granted")
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "This application need permissions to access",
                RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION, *CAM_AND_READ_EXTERNAL_STORAGE
            )
        }
    }

    
    fun openDialog(contentUri: Uri?) {
        val imageDialog = ImageDialog(contentUri)
        imageDialog.show(supportFragmentManager, "Image dialog")
    }

    fun getFileExt(contentUri: Uri?): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentUri?.let { contentResolver.getType(it) })
    }


}