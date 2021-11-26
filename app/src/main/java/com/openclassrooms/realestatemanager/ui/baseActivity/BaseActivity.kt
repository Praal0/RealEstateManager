package com.openclassrooms.realestatemanager.ui.baseActivity

import android.Manifest
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions
import android.os.Environment
import androidx.activity.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.util.*

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private val RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION = 100
    private var newfile: File? = null
    private var mError:Boolean = false
    private  val VIDEO_DIRECTORY : String = "/realEstateManager"
    private val CAM_AND_READ_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)



    fun saveVideoToInternalStorage( filePath: String) {
        try {
            val currentFile = File(filePath)
            val wallpaperDirectory = File(
                Environment.getExternalStorageState()
                    .toString() + VIDEO_DIRECTORY
            )
            newfile = File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".mp4")
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }
            if (currentFile.exists()) {
                val `in`: InputStream = FileInputStream(currentFile)
                val out: OutputStream = FileOutputStream(newfile)

                // Copy the bits from instream to outstream
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                Log.d("vii", "Video file saved successfully.")
            } else {
                Log.e("vii", "Video saving failed. Source file missing.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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


    fun getFileExt(contentUri: Uri?): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentUri?.let { contentResolver.getType(it) })
    }


}