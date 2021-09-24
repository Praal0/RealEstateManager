package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import java.util.*
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.MediaController
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat

import java.io.File

import android.content.DialogInterface
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.io.IOException
import android.graphics.Bitmap

import android.content.ContextWrapper
import android.net.Uri
import android.widget.DatePicker
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.ui.master.MasterAdapter
import java.io.FileOutputStream


@AndroidEntryPoint
class AddEditActivity : AppCompatActivity(), PermissionCallbacks,View.OnClickListener {

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar

    private var mUpOfSaleDateDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private var mSoldDate: DatePickerDialog? = null
    private var estateEdit: Long = 0

    private val idEstate: Long = 0
    private lateinit var currentPhotoPath : String
    private var selectedImage : Bitmap? = null
    private var listPhoto: List<Uri>? = null
    private lateinit var adapter: PhotoAdapter
    private var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm
        estateEdit = intent.getLongExtra("iDEstate", idEstate)
        val view: View = activityAddBinding.root
        setContentView(view)
        setDateField()
        initialize()
        dropDownAdapters()
        onClickOpenCamera()
        onClickBtnDeleteVideo()
        onClickVideoBtn()

        //Set title toolbar
        setToolbar()

        setupRecyclerView()

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

        //for video in edit
        estateFormBinding.videoView.requestFocus()
        val mediaController = MediaController(this)
        estateFormBinding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(estateFormBinding.videoView)
        estateFormBinding.videoView.start()
    }

    private fun setupRecyclerView() {
        adapter = PhotoAdapter()
        estateFormBinding.rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        estateFormBinding.rvPhoto.adapter = adapter
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        if (estateEdit == 0L) {
            ab?.setTitle("Create Estate")
        } else {
            ab?.setTitle("Edit Estate")
        }
    }

    /**
     * for date picker
     */
    private fun setDateField() {
        estateFormBinding.upOfSaleDate.setOnClickListener(this)
        estateFormBinding.soldDate.setOnClickListener(this)
        //For up of sale date
        val newCalendar = Calendar.getInstance()
        mUpOfSaleDateDialog = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                estateFormBinding.upOfSaleDate.setText(mDateFormat!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        //For sold date
        mSoldDate = DatePickerDialog(
            this,
            { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                estateFormBinding.soldDate.setText(mDateFormat!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
    }

    override fun onClick(v: View?) {
        if (v == estateFormBinding.upOfSaleDate) {
            mUpOfSaleDateDialog?.show();
            mUpOfSaleDateDialog?.datePicker?.maxDate = (Calendar.getInstance().timeInMillis);

        } else if (v == estateFormBinding.soldDate) {
            mSoldDate?.show();
            mSoldDate?.datePicker?.maxDate = Calendar.getInstance().timeInMillis;
        }
    }

    /**
     * Adapter generic for dropdown
     * @param resId
     * @return
     */
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?>? {
        return ArrayAdapter(this, R.layout.dropdown_menu_popup_item, resources.getStringArray(resId))
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = estateFormBinding.includedToolbarAdd.simpleToolbar
    }

    /**
     * For set adapters dropdown
     */
    @SuppressLint("ClickableViewAccessibility")
    fun dropDownAdapters() {
        estateFormBinding.etRooms.setAdapter(factoryAdapter(R.array.ROOMS))
        estateFormBinding.etRooms.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    estateFormBinding.etRooms.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))
        estateFormBinding.etBedrooms.setAdapter(factoryAdapter(R.array.BEDROOMS))
        estateFormBinding.etBedrooms.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    estateFormBinding.etBedrooms.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))

        estateFormBinding.etBathrooms.setAdapter(factoryAdapter(R.array.BATHROOMS))
        estateFormBinding.etBathrooms.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    estateFormBinding.etBathrooms.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))

        estateFormBinding.etAgent.setAdapter(factoryAdapter(R.array.AGENT))
        estateFormBinding.etAgent.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    estateFormBinding.etAgent.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))
    }

    /**
     * Click on delete Video
     */
    fun onClickBtnDeleteVideo(){
        estateFormBinding.deleteVideo.setOnClickListener(View.OnClickListener {
            estateFormBinding.videoView.visibility = View.INVISIBLE
            estateFormBinding.deleteVideo.visibility = View.INVISIBLE
        })
    }

    fun onClickVideoBtn(){
        estateFormBinding.cameraBtn.setOnClickListener(View.OnClickListener {
            selectVideo()
        })
    }

    @AfterPermissionGranted(123)
    private fun selectVideo() {
        val options = arrayOf<CharSequence>("Take Video", "Choose Video", "Cancel")
        val builderVideo = AlertDialog.Builder(this, R.style.AlertDialog)
        builderVideo.setTitle("Add video")
        builderVideo.setItems(options) { dialog, item ->
            if (options[item] == "Take Video") {
                val takeVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivity(takeVideo)
            } else if (options[item] == "Choose Video") {
                val pickVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivity(pickVideo);
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builderVideo.show()
    }

    /**
     * Click on capture camera
     */
    fun onClickOpenCamera(){
        estateFormBinding.photoBtn.setOnClickListener(View.OnClickListener {
            selectImage()
            saveImageInInternalStorage();
        })
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setTitle("Add pictures")
        builder.setItems(options) { dialog, item ->
            if (options[item].equals("Take Photo")) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(takePicture)
                if (takePicture.resolveActivity(packageManager) != null) {
                    //Create the File where the photo should go var photoFile: File? = null
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex: IOException) {
                        ex.message?.let { Log.e("PhotoFileException", it) }
                    }
                    //Continue only if the file was successfully created
                    if (photoFile != null) {

                        photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.openclassrooms.realestatemanager.fileprovider", photoFile);

                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        Log.d("PhotoUri", "photoUri =" + photoUri);

                        startActivity(takePicture)
                    }
                }
            } else if (options[item].equals("Choose from Gallery")) {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivity(pickPhoto)
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun createImageFile() : File? {
        //Create an image file name
        val timeStamp = SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(Date())
        val imageFileName = "JPEG" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /*prefix*/
            ".jpg",  /*suffix*/
            storageDir /*directory*/
        )
        // Save file : path for use with ACTION_VIEW intent
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun saveImageInInternalStorage() {
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, "UniqueFileName" + ".jpg")
        if (!file.exists()) {
            Log.d("path", file.toString())
            var fos: FileOutputStream? = null
            try {
                if (selectedImage != null) {
                    fos = FileOutputStream(file)
                    selectedImage!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }




}