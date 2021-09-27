package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.MediaController
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.PhotoDescription
import com.openclassrooms.realestatemanager.models.UriList
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddEditActivity : BaseActivity(),View.OnClickListener {

    protected val PICK_IMAGE_CAMERA = 1
    protected val PICK_IMAGE_GALLERY = 2
    protected val PICK_VIDEO_CAMERA = 3
    protected val PICK_VIDEO_GALLERY = 4

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar
    private  val VIDEO_DIRECTORY : String = "/realEstateManager"

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
    private val photo = UriList()
    private val video = UriList()
    private val photoText = PhotoDescription()
    private lateinit var cursor: Cursor
    private var newfile: File? = null

    private val viewModel: EstateViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm
        estateEdit = intent.getLongExtra("iDEstate", idEstate)
        val view: View = activityAddBinding.root
        setContentView(view)
        methodRequiresTwoPermission()
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

    private fun setupRecyclerView() {
        adapter = PhotoAdapter()
        estateFormBinding.rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        estateFormBinding.rvPhoto.adapter = adapter
        viewModel.getEstate(estateEdit).observe(this,this::updateUIFromEdit)
    }

    private fun updateUIFromEdit(estate: Estate) {
        estateFormBinding.etMandate.setText(estate.numMandat.toString())
        estateFormBinding.etEstate.setText(estate.agentName)
        estateFormBinding.etSurface.setText(estate.surface.toString())
        estateFormBinding.etDescription.setText(estate.description)
        estateFormBinding.etRooms.setText(estate.rooms.toString().replace("5 et +", "5"), false)
        estateFormBinding.etBathrooms.setText(Objects.requireNonNull(estate.bathrooms).toString().replace("4 et +", "4"), false)
        estateFormBinding.etBedrooms.setText(Objects.requireNonNull(estate.bedrooms).toString().replace("5 et +", "5"), false)
        estateFormBinding.etGround.setText(estate.ground.toString())
        estateFormBinding.etPrice.setText(estate.price.toString())
        estateFormBinding.etAddress.setText(estate.address)
        estateFormBinding.etPostalCode.setText(estate.postalCode.toString())
        estateFormBinding.etCity.setText(estate.city)
        estateFormBinding.boxSchools.isChecked = estate.schools
        estateFormBinding.boxPark.isChecked = estate.park
        estateFormBinding.boxRestaurants.isChecked = estate.restaurants
        estateFormBinding.boxStores.isChecked = estate.stores
        estateFormBinding.availableRadiobtn.isChecked = estate.sold
        estateFormBinding.upOfSaleDate.setText(estate.upOfSaleDate?.let { Utils.longDateToString(it) })
        estateFormBinding.soldDate.setText(estate.soldDate)
        estateFormBinding.etAgent.setText(estate.agentName, false)

        if (estate.photoList.photoList.isNotEmpty()){
            listPhoto
        }

        if (!estate.video.photoList.isEmpty()){
            for (videoStr in estate.video.photoList) {
                estateFormBinding.deleteVideo.visibility = View.VISIBLE
                estateFormBinding.videoView.visibility = View.VISIBLE
                estateFormBinding.videoView.setVideoURI(Uri.parse(videoStr))
            }
        }

    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        if (estateEdit == 0L) {
            ab?.setTitle("Create Estate")
        } else {
            ab?.setTitle("Edit Estate")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            mUpOfSaleDateDialog?.show()
            mUpOfSaleDateDialog?.datePicker?.maxDate = (Calendar.getInstance().timeInMillis)

        } else if (v == estateFormBinding.soldDate) {
            mSoldDate?.show()
            mSoldDate?.datePicker?.maxDate = Calendar.getInstance().timeInMillis
        }
    }

    /**
     * Adapter generic for dropdown
     * @param resId
     * @return
     */
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?> {
        return ArrayAdapter(this, R.layout.dropdown_menu_popup_item, resources.getStringArray(resId))
    }

    // Initialisation variable
    private fun initialize() {
        listPhoto = ArrayList()
        toolbar = estateFormBinding.includedToolbarAdd.simpleToolbar
        estateFormBinding.deleteVideo.visibility = INVISIBLE
        estateFormBinding.videoView.visibility = INVISIBLE
    }

    /**
     * For set adapters dropdown
     */
    @SuppressLint("ClickableViewAccessibility")
    fun dropDownAdapters() {
        estateFormBinding.etEstate.setAdapter(factoryAdapter(R.array.ESTATES))
        estateFormBinding.etEstate.setOnTouchListener(View.OnTouchListener(
            fun(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    estateFormBinding.etEstate.showDropDown()
                    return true
                }
                return event.action == MotionEvent.ACTION_UP
            }
        ))

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
    private fun onClickBtnDeleteVideo(){
        estateFormBinding.deleteVideo.setOnClickListener {
            estateFormBinding.videoView.visibility = INVISIBLE
            estateFormBinding.deleteVideo.visibility = INVISIBLE
        }
    }

    fun onClickVideoBtn(){
        estateFormBinding.cameraBtn.setOnClickListener {
            selectVideo()
        }
    }

    private fun selectVideo() {
        val options = arrayOf<CharSequence>("Take Video", "Choose Video", "Cancel")
        val builderVideo = AlertDialog.Builder(this, R.style.AlertDialog)
        builderVideo.setTitle("Add video")
        builderVideo.setItems(options) { dialog, item ->
            if (options[item] == "Take Video") {
                val takeVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(takeVideo, PICK_VIDEO_CAMERA)
            } else if (options[item] == "Choose Video") {
                val pickVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(pickVideo, PICK_VIDEO_GALLERY)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builderVideo.show()
    }

    /**
     * Click on capture camera
     */
    private fun onClickOpenCamera(){
        estateFormBinding.photoBtn.setOnClickListener(View.OnClickListener {
            selectImage()
            saveImageInInternalStorage()
        })
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setTitle("Add pictures")
        builder.setItems(options) { dialog, item ->
            if (options[item].equals("Take Photo")) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 1);
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
                        photoUri = FileProvider.getUriForFile(applicationContext, "com.openclassrooms.realestatemanager.provider", photoFile)
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        Log.d("PhotoUri", "photoUri =" + photoUri);

                        startActivityForResult(takePicture, 1);
                    }
                }
            } else if (options[item].equals("Choose from Gallery")) {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto,2)
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

    /**
     * For photos
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_CAMERA) {
                if (resultCode == RESULT_OK) {
                    photo.photoList.add(photoUri.toString())
                    photoText.photoDescription.add("")
                    adapter.setPhotoList(listPhoto)
                }
            }
            if (requestCode == PICK_IMAGE_GALLERY && data != null && data.data != null) {
                if (resultCode == RESULT_OK) {
                    val contentUri = Objects.requireNonNull(data).data
                    val timeStamp = SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(Date())
                    val imageFileName = "JPEG" + timeStamp + "." + contentUri?.let { getFileExt(it) }
                    Log.d(
                        "Test uri gallery",
                        "onActivityResult : Gallery Image Uri:$imageFileName"
                    )

                    //For save image in internal storage
                    var fOut: FileOutputStream? = null
                    try {
                        fOut = openFileOutput("imageGallery", MODE_PRIVATE)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    val osw = OutputStreamWriter(Objects.requireNonNull(fOut))
                    try {
                        osw.write(imageFileName)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        osw.flush()
                        osw.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    photo.photoList.add(contentUri.toString())
                    photoText.photoDescription.add("")
                    adapter.setPhotoList(listPhoto)
                }
            }
            if (requestCode == PICK_VIDEO_CAMERA && data != null && data.data != null) {
                if (resultCode == RESULT_OK) {
                    val contentURI = data.data
                    val recordedVideoPath: String? = contentURI?.let { getPath(it) }
                    recordedVideoPath?.let { Log.d("recordedVideoPaht", it) }
                    recordedVideoPath?.let { saveVideoToInternalStorage(it) }
                    estateFormBinding.videoView.setVideoURI(contentURI)
                    estateFormBinding.videoView.requestFocus()
                    estateFormBinding.videoView.visibility = View.VISIBLE
                    estateFormBinding.deleteVideo.visibility = View.VISIBLE
                    val mediaController = MediaController(this)
                    estateFormBinding.videoView.setMediaController(mediaController)
                    mediaController.setAnchorView(estateFormBinding.videoView)
                    estateFormBinding.videoView.start()
                    video.photoList.add(contentURI.toString())
                }
            }
            if (requestCode == PICK_VIDEO_GALLERY && data != null && data.data != null) {
                if (resultCode == RESULT_OK) {
                    val contentURI = data.data
                    val selectedVideoPath: String? = contentURI?.let { getPath(it) }
                    selectedVideoPath?.let { Log.d("path", it) }
                    selectedVideoPath?.let { saveVideoToInternalStorage(it) }
                    estateFormBinding.videoView.setVideoURI(contentURI)
                    estateFormBinding.videoView.visibility = View.VISIBLE
                    estateFormBinding.videoView.requestFocus()
                    val mediaController = MediaController(this)
                    estateFormBinding.videoView.setMediaController(mediaController)
                    mediaController.setAnchorView(estateFormBinding.videoView)
                    estateFormBinding.videoView.start()
                    estateFormBinding.videoView.visibility = View.VISIBLE
                    estateFormBinding.deleteVideo.visibility = View.VISIBLE
                    selectedVideoPath?.let { video.photoList.add(it) }
                }
            }
        }
    }

    private fun saveVideoToInternalStorage( filePath: String) {
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
     * For video
     *
     * @param uri
     * @return
     */
    private fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        contentResolver.query(uri, projection, null, null, null).also {
            if (it != null) {
                cursor = it
            }
        }
        return run {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = (cursor as Cursor)
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        }
    }

    private fun getFileExt(contentUri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(contentUri))
    }

}