package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.*
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
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import com.openclassrooms.realestatemanager.databinding.LayoutDialogBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.Location
import com.openclassrooms.realestatemanager.models.PhotoDescription
import com.openclassrooms.realestatemanager.models.UriList
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.utils.EstateManagerStream
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class AddEditActivity : BaseActivity(),View.OnClickListener {

    protected val PICK_IMAGE_CAMERA = 1
    protected val PICK_IMAGE_GALLERY = 2
    protected val PICK_VIDEO_CAMERA = 3
    protected val PICK_VIDEO_GALLERY = 4

    private val mCompositeDisposable = CompositeDisposable()

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar
    private  val VIDEO_DIRECTORY : String = "/realEstateManager"
    lateinit var estate : Estate

    private var mUpOfSaleDateDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private var mSoldDate: DatePickerDialog? = null
    private var estateEdit: Long = 0
    private var mError = false
    private var mDisposable: Disposable? = null
    private var completeAddress: String? = null
    private val idEstate: Long = 0
    lateinit var currentPhotoPath: String
    private var listPhoto: MutableList<Uri>? = null
    private lateinit var adapter: PhotoAdapter
    private val photo = UriList()
    private val video = UriList()
    private val photoText = PhotoDescription()
    private lateinit var cursor: Cursor
    private var newfile: File? = null
    private lateinit var location:Location

    private var description : String? = null
    private val estateViewModel: EstateViewModel by viewModels()
    private val locationViewModel : LocationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm
        estateEdit = intent.getLongExtra("iDEstate", idEstate)
        if(estateEdit==0L) {
            estateFormBinding.deleteVideo.visibility = INVISIBLE
        }

        val view: View = activityAddBinding.root
        setContentView(view)
        methodRequiresTwoPermission()
        setDateField()
        initialize()
        dropDownAdapters()
        onClickOpenCamera()
        onClickBtnDeleteVideo()
        onClickVideoBtn()
        clickFabButton()
        clickSoldButon()

        //Set title toolbar
        setToolbar()

        setupRecyclerView()

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    }

    private fun clickSoldButon() {
        estateFormBinding.availableCheckbtn.setOnClickListener(View.OnClickListener {
            if  (!estateFormBinding.availableCheckbtn.isChecked){
                estateFormBinding.soldDate.text = null
            }
        })
    }

    // Initialisation variable
    private fun initialize() {
        listPhoto = ArrayList()
        toolbar = estateFormBinding.includedToolbarAdd.simpleToolbar
        estateFormBinding.deleteVideo.visibility = INVISIBLE
        estateFormBinding.videoView.visibility = INVISIBLE
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
        adapter = PhotoAdapter(listPhoto, Glide.with(this), photoText.photoDescription, estateEdit)
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        estateFormBinding.rvPhoto.layoutManager = horizontalLayoutManager
        estateFormBinding.rvPhoto.adapter = adapter

        if(estateEdit!=0L) {
            estateViewModel.getEstateById(estateEdit).observe(this,this::updateUIFromEdit)
        }

    }

    private fun updateUIFromEdit(estate: Estate) {
        estateFormBinding.etMandate.setText(estate.numMandat.toString())
        estateFormBinding.etEstate.setText(estate.estateType)
        estateFormBinding.etSurface.setText(estate.surface.toString())
        estateFormBinding.etDescription.setText(estate.description)
        estateFormBinding.etRooms.setText(estate.rooms.toString().replace("5 et +", "5"), false)
        estateFormBinding.etBathrooms.setText(Objects.requireNonNull(estate.bathrooms).toString().replace("4 et +", "4"), false)
        estateFormBinding.etBedrooms.setText(Objects.requireNonNull(estate.bedrooms).toString().replace("5 et +", "5"), false)
        estateFormBinding.etGround.setText(estate.ground.toString())
        estateFormBinding.etPrice.setText(estate.price.toString())
        estateFormBinding.boxSchools.isChecked = estate.schools
        estateFormBinding.boxPark.isChecked = estate.park
        estateFormBinding.boxRestaurants.isChecked = estate.restaurants
        estateFormBinding.boxStores.isChecked = estate.stores
        estateFormBinding.availableCheckbtn.isChecked = estate.sold
        estateFormBinding.upOfSaleDate.setText(estate.upOfSaleDate?.let { Utils.longDateToString(it) })
        estateFormBinding.soldDate.setText(estate.soldDate)
        estateFormBinding.etAgent.setText(estate.agentName, false)

        locationViewModel.getLocationById(estate.numMandat).observe(this, androidx.lifecycle.Observer {
            estateFormBinding.etAddress.setText(it.address)
            estateFormBinding.etCity.setText(it.city.toString())
            estateFormBinding.etPostalCode.setText(it.zipCode.toString())
        })

        listPhoto?.let { adapter.setPhotoList(it) };
        adapter.setPhotoDescription(estate.photoDescription.photoDescription)
        photo.photoList.addAll(estate.photoList.photoList);

        if (estate.video.photoList.isNotEmpty()){
            for (videoStr in estate.video.photoList) {
                estateFormBinding.deleteVideo.visibility = View.VISIBLE
                estateFormBinding.videoView.visibility = View.VISIBLE
                estateFormBinding.videoView.setVideoURI(Uri.parse(videoStr))
                //for video in edit
                estateFormBinding.videoView.requestFocus()
                val mediaController = MediaController(this)
                estateFormBinding.videoView.setMediaController(mediaController)
                mediaController.setAnchorView(estateFormBinding.videoView)
                estateFormBinding.videoView.start()
            }
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
                //getTmpFileUri().let { uri ->
                //resultLauncher.launch(uri) }

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
        })
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setTitle("Add pictures")
        builder.setItems(options) { dialog, item ->
            if (options[item].equals("Take Photo")) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            Log.e("Capture",ex.toString())
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "com.example.android.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA)
                        }
                    }
                }

            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto,PICK_IMAGE_GALLERY)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
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
            if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                saveCoolerImage(imageBitmap)

            }
            if (requestCode == PICK_IMAGE_GALLERY && data != null && data.data != null) {
                if (resultCode == RESULT_OK) {
                    val contentUri = data.data
                    val timeStamp = SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(Date())
                    val imageFileName = "JPEG" + timeStamp + "." + getFileExt(contentUri)
                    Log.d("Test uri gallery", "onActivityResult : Gallery Image Uri:$imageFileName")

                    //For save image in internal storage
                    var fOut: FileOutputStream? = null
                    try {
                        fOut = openFileOutput("imageGallery", MODE_PRIVATE)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    val osw = OutputStreamWriter(fOut)
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
                    openDialog(contentUri)
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

    private fun saveCoolerImage(bitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saveImage")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val imageName = "Image$n.jpg"
        val file = File(myDir,imageName)
        if (file.exists()) file.delete()
        try {
            val out: FileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out)
            file.absolutePath
        }catch (e:Exception){

        }
    }



    fun openDialog(contentUri: Uri?) {
        val builder = android.app.AlertDialog.Builder(this)
        var binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        val view: View = binding.root
        binding.imageDescription.setImageURI(contentUri)
        builder.setView(view)
            .setNegativeButton("cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            })
            .setPositiveButton("ok",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val descirption: String = binding.editDescription.text.toString()
                    contentUri?.let { listPhoto?.add(it) }
                    Log.e("Picutre", "contentUri = ${contentUri.toString()}")
                    photoText.photoDescription.add(descirption)
                    photo.photoList.add(contentUri.toString())
                    listPhoto?.let { adapter.setPhotoList(it)
                        Log.e("Picutre", " it = ${it.size}")}
                }

            })
        builder.create()
        builder.show()
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

    /**
     * Click Validate button and verification
     */

    fun clickFabButton(){
        estateFormBinding.validateFabBtn.setOnClickListener {
            //For description photo in recyclerView
            val photoDescriptionList: ArrayList<String> = ArrayList()
            for (i in photoText.photoDescription.indices) {
                val editText: AppCompatEditText? = activityAddBinding.includeForm.rvPhoto.layoutManager?.findViewByPosition(i)?.findViewById(R.id.photo_description)
                val desc = editText?.text.toString()
                photoDescriptionList.add(desc)
            }

            validateTextView(estateFormBinding.inputMandate)
            validateTextView(estateFormBinding.inputEstate)
            validateTextView(estateFormBinding.inputSurface)
            validateTextView(estateFormBinding.inputRooms)
            validateTextView(estateFormBinding.inputBedrooms)
            validateTextView(estateFormBinding.inputBathrooms)
            validateTextView(estateFormBinding.inputGround)
            validateTextView(estateFormBinding.inputPrice)
            validateTextView(estateFormBinding.inputDescription)
            validateTextView(estateFormBinding.inputAddress)
            validateTextView(estateFormBinding.inputGround)
            validateTextView(estateFormBinding.inputPostalCode)
            validateTextView(estateFormBinding.inputCity)


            if (!soldDatedRequired()){
                return@setOnClickListener
            }


            if (mError){
                mError = false
                return@setOnClickListener
            }

            saveEstates()
        }
    }

    private fun saveEstates() {
         estate = Estate(estateFormBinding.etMandate.text.toString().toLong(),
            estateFormBinding.etEstate.text.toString(),
            Integer.parseInt(estateFormBinding.etSurface.text.toString()),
            Integer.parseInt(estateFormBinding.etRooms.text.toString()),
            Integer.parseInt(estateFormBinding.etBedrooms.text.toString()),
            Integer.parseInt(estateFormBinding.etBathrooms.text.toString()),
            Integer.parseInt(estateFormBinding.etGround.text.toString()),
            estateFormBinding.etPrice.text.toString().toDouble(),
            estateFormBinding.etDescription.text.toString(),
            estateFormBinding.boxSchools.isChecked,
            estateFormBinding.boxStores.isChecked,
            estateFormBinding.boxPark.isChecked,
            estateFormBinding.boxRestaurants.isChecked,
            estateFormBinding.availableCheckbtn.isChecked,
            null,
            estateFormBinding.soldDate.text.toString(),
            estateFormBinding.etAgent.text.toString(),
            photo,
            photoText,
            video,0)

        Log.d("saveEstate", "saveEstate$estate")

        if (estateEdit == 0L) {
            executeHttpRequestWithRetrofit(this)
            Snackbar.make(activityAddBinding.root, "Your new Estate is created", Snackbar.LENGTH_SHORT)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        super.onDismissed(snackbar, event)
                        finish()
                    }
                })
                .show()
        } else {
            executeHttpRequestWithRetrofit(this)
            Snackbar.make(activityAddBinding.root, "Your new Estate is updated", Snackbar.LENGTH_SHORT)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        super.onDismissed(snackbar, event)
                        finish()
                    }
                }).show()
        }
    }

    private fun validateTextView(inputValue: TextInputLayout): String? {
        val tmpValue = inputValue.editText?.text.toString()
        return if (tmpValue.isEmpty()) {
            inputValue.error = getText(R.string.require)
            mError = true
            null
        } else {
            mError = false
            inputValue.error = null
            tmpValue
        }
    }


    private fun soldDatedRequired() : Boolean {
        val soldDateInput = estateFormBinding.inputSoldDate.editText?.text.toString()
        if (soldDateInput.isEmpty() && estateFormBinding.availableCheckbtn.isChecked){
            estateFormBinding.soldDate.error = R.string.require.toString()
            return false
        }
        return true
    }


    /**
     * RX Java http request for geocoding API
     */
    private fun executeHttpRequestWithRetrofit(context: Context) {
        completeAddress = estateFormBinding.etAddress.text.toString() + estateFormBinding.etCity.text.toString()+ estateFormBinding.etPostalCode.text.toString()
        mDisposable = EstateManagerStream.streamFetchGeocode(completeAddress)
            .subscribeWith(object : DisposableObserver<Geocoding?>() {
                override fun onNext(geocoding: Geocoding) {
                    if (!geocoding.results.isNullOrEmpty()){
                        location = Location(0,
                            geocoding.results[0].geometry.location.lng,
                            geocoding.results[0].geometry.location.lat,
                            estateFormBinding.etAddress.text.toString(),
                            estateFormBinding.etCity.text.toString(),
                            estateFormBinding.etPostalCode.text.toString(),
                            estateFormBinding.etMandate.text.toString().toLong())

                        if (estateEdit == 0L){
                            locationViewModel.insertLocation(location)
                        }else{
                            locationViewModel.getLocationById(estateEdit).observe(this@AddEditActivity,
                                androidx.lifecycle.Observer {
                                    locationViewModel.updateLocation(location)
                                })
                        }
                        locationViewModel.getLocationById(estate.numMandat).observe(this@AddEditActivity, androidx.lifecycle.Observer {
                            estate.locationId = it.id
                            if (estateEdit == 0L){
                                estateViewModel.insertEstates(estate,this@AddEditActivity)
                            }else{
                                estateViewModel.updateEstate(estate)
                            }
                        })
                    }else{
                        Toast.makeText(context,"Geocoding : Null or Empty",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(@NonNull e: Throwable) {
                    Log.e("Geocoding","Error insert",e)
                }
                override fun onComplete() {

                }
            })
        mCompositeDisposable.add(mDisposable as DisposableObserver<*>);
    }

    /**
     * Dispose subscription
     */
    private fun disposeWhenDestroy() {
        mCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeWhenDestroy()
    }


}