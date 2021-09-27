package com.openclassrooms.realestatemanager.ui.baseActivity

import android.Manifest
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

open class BaseActivity : AppCompatActivity() {

    private val RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION = 100
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

    /**
     * For permissions
     */
    @AfterPermissionGranted(100)
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


}