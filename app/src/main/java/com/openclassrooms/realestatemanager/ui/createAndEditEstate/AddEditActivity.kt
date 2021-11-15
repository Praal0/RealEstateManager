package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.*
import android.content.ContentValues.TAG
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.MediaController
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
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
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import io.reactivex.observers.DisposableObserver
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


@AndroidEntryPoint
class AddEditActivity : BaseActivity(),View.OnClickListener {
    protected val PICK_IMAGE_CAMERA = 1
    protected val PICK_IMAGE_GALLERY = 2
    protected val PICK_VIDEO_CAMERA = 3
    protected val PICK_VIDEO_GALLERY = 4

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar
    lateinit var estate : Estate
    lateinit var location:Location

    private var mUpOfSaleDateDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private var mSoldDate: DatePickerDialog? = null
    private var estateEdit: Long = 0L
    private var mError = false
    private var completeAddress: String? = null

    private val idEstate: Long = 0
    private var listPhoto : MutableList<Uri> = ArrayList()
    private var listDescription : MutableList<String> = ArrayList()
    private val photoList : PhotoDescription = PhotoDescription()
    private lateinit var adapter: PhotoAdapter
    private val photo = UriList()
    private val video = UriList()
    private lateinit var cursor: Cursor

    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm
        estateEdit = intent.getLongExtra("iDEstate", idEstate)

        if(estateEdit==0L) { estateFormBinding.deleteVideo.visibility = INVISIBLE }

        estateViewModel.currentPhoto.observe(this){uriList ->
            estateViewModel.currentPhotoText.observe(this){ stringList ->
                adapter.setPhotoList(uriList,stringList)
            }
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
        configureOnClickRecyclerView()

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
        toolbar = estateFormBinding.includedToolbarAdd.simpleToolbar
        estateFormBinding.deleteVideo.visibility = INVISIBLE
        estateFormBinding.videoView.visibility = INVISIBLE
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        if (estateEdit == 0L) {
            ab?.setTitle(R.string.createEstate)
        } else {
            ab?.setTitle(R.string.editEstate)
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
        adapter = PhotoAdapter(Glide.with(this), photoList.photoDescription, false)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        estateFormBinding.rvPhoto.layoutManager = horizontalLayoutManager
        estateFormBinding.rvPhoto.adapter = adapter

        if(estateEdit!=0L) { estateViewModel.getEstateById(estateEdit).observe(this,this::updateUIFromEdit) }
    }

    private fun updateUIFromEdit(estate: Estate) {
        estateFormBinding.etMandate.setText(estate.numMandat.toString())
        estateFormBinding.etMandate.isEnabled = false
        estateFormBinding.etEstate.setText(estate.estateType)
        estateFormBinding.etSurface.setText(estate.surface.toString())
        estateFormBinding.etDescription.setText(estate.description)
        estateFormBinding.etRooms.setText(estate.rooms.toString().replace("5 et +", "5"), false)
        estateFormBinding.etBathrooms.setText(estate.bathrooms.toString().replace("4 et +", "4"), false)
        estateFormBinding.etBedrooms.setText(estate.bedrooms.toString().replace("5 et +", "5"), false)
        estateFormBinding.etGround.setText(estate.ground.toString())
        estateFormBinding.etPrice.setText(estate.price.toString())
        estateFormBinding.boxSchools.isChecked = estate.schools
        estateFormBinding.boxPark.isChecked = estate.park
        estateFormBinding.boxRestaurants.isChecked = estate.restaurants
        estateFormBinding.boxStores.isChecked = estate.stores
        estateFormBinding.availableCheckbtn.isChecked = estate.sold
        estateFormBinding.saleDate.setText(estate.upOfSaleDate)
        estateFormBinding.soldDate.setText(estate.soldDate)
        estateFormBinding.etAgent.setText(estate.agentName, false)
        estateFormBinding.etAddress.setText(estate.locationEstate.address)
        estateFormBinding.etCity.setText(estate.locationEstate.city.toString())
        estateFormBinding.etPostalCode.setText(estate.locationEstate.zipCode.toString())


        if (estate.photoList.photoList.isNotEmpty()) {
            listPhoto.clear()
            photo.photoList.clear()
            photoList.photoDescription.clear()
            for (photoStr in estate.photoList.photoList) {
                listPhoto.add((Uri.parse(photoStr)))
            }
            estateViewModel.currentPhoto.postValue(listPhoto)
            estateViewModel.currentPhotoText.postValue(estate.photoDescription.photoDescription)
            photo.photoList.addAll(estate.photoList.photoList)
            adapter.setPhotoList(listPhoto,estate.photoDescription.photoDescription)
            adapter.notifyDataSetChanged()
        }

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
        estateFormBinding.saleDate.setOnClickListener(this)
        estateFormBinding.soldDate.setOnClickListener(this)
        //For up of sale date
        val newCalendar = Calendar.getInstance()
        mUpOfSaleDateDialog = DatePickerDialog(
            this, { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                estateFormBinding.saleDate.setText(mDateFormat!!.format(newDate.time)) },
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
        if (v == estateFormBinding.saleDate) {
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
     * For delete photos and descriptions
     */
    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(estateFormBinding.rvPhoto, R.layout.activity_add_photo_item)
            .setOnItemClickListener { recyclerView: RecyclerView?, position: Int, v: View? ->
                val estatePhoto = listPhoto[position].toString()
                Log.d("estatePhoto", "estatePhoto$estatePhoto")
                val estateDescription = photoList.photoDescription[position]
                listPhoto.remove(Uri.parse(estatePhoto))
                photo.photoList.remove(estatePhoto)
                photoList.photoDescription.remove(estateDescription)
                estateViewModel.currentPhoto.postValue(listPhoto)
                estateViewModel.currentPhotoText.postValue(photoList.photoDescription)

                adapter.setPhotoList(listPhoto,photoList.photoDescription)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
            }
    }

    private fun onClickVideoBtn(){
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
     * Click on delete Video
     */
    private fun onClickBtnDeleteVideo(){
        estateFormBinding.deleteVideo.setOnClickListener {
            video.photoList.clear()
            estateFormBinding.videoView.visibility = INVISIBLE
            estateFormBinding.deleteVideo.visibility = INVISIBLE
        }
    }



    /**
     * Click on capture camera
     */
    private fun onClickOpenCamera(){
        estateFormBinding.photoBtn.setOnClickListener(View.OnClickListener {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(this, R.style.AlertDialog)
            builder.setTitle("Add pictures")
            builder.setItems(options) { dialog, item ->
                if (options[item] == "Take Photo") {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA)
                    Log.d(TAG, "dispatchTakePictureIntent: called")
                } else if (options[item] == "Choose from Gallery") {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto,PICK_IMAGE_GALLERY)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
            builder.show()
        })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult from take picture is call")
                val takenImage = data?.extras?.get("data") as Bitmap
                val bytes = ByteArrayOutputStream()
                takenImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    this.contentResolver,
                    takenImage,
                    System.currentTimeMillis().toString(),
                    null
                )
                val image = Uri.parse(path.toString())
                openDialog(image)
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
                    selectedVideoPath?.let {
                        Log.d("path", it)
                        saveVideoToInternalStorage(it)
                    }
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

    /**
     * For video
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

    private fun openDialog(contentUri: Uri?) {
        val builder = android.app.AlertDialog.Builder(this)
        var binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        val view: View = binding.root
        val glide : RequestManager = Glide.with(this)
         glide.load(contentUri).apply(RequestOptions.centerCropTransform())
            .into(binding.imageDescription)
        builder.setView(view)
            .setNegativeButton("cancel") { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton("ok") { dialog, which ->
                val description: String = binding.editDescription.text.toString()
                contentUri?.let { listPhoto.add(it) }
                Log.e("Picture", "contentUri = $listPhoto")
                photo.photoList.add(contentUri.toString())
                listDescription.add(description)
                photoList.photoDescription.add(description)
                estateViewModel.currentPhoto.postValue(listPhoto)
                estateViewModel.currentPhotoText.postValue(listDescription)

                adapter.setPhotoList(listPhoto,listDescription)
            }
        builder.create()
        builder.show()
    }

    /**
     * Click Validate button and verification
     */
    fun clickFabButton(){
        estateFormBinding.validateFabBtn.setOnClickListener {
            //For description photo in recyclerView
            val photoDescriptionList: ArrayList<String> = ArrayList()
            for (i in photoList.photoDescription.indices) {
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
            validateTextView(estateFormBinding.inputPrice)
            validateTextView(estateFormBinding.inputDescription)
            validateTextView(estateFormBinding.inputAddress)
            validateTextView(estateFormBinding.inputPostalCode)
            validateTextView(estateFormBinding.inputCity)
            validateTextView(estateFormBinding.inputAgent)

            if (!soldDatedRequired()){ return@setOnClickListener }
            if (!saleDateRequired()){ return@setOnClickListener }


            if (mError){
                mError = false
                return@setOnClickListener
            }
            saveEstates()
        }
    }

    private fun saveEstates() {
        var ground : Int = 0
        location = Location(
            0.0,
            0.0,
            estateFormBinding.etAddress.text.toString(),
            estateFormBinding.etCity.text.toString(),
            estateFormBinding.etPostalCode.text.toString())

        if (estateFormBinding.etGround.text.toString().isNotEmpty()){
            ground = estateFormBinding.etGround.text.toString().toInt()
        }


         estate = Estate(estateFormBinding.etMandate.text.toString().toLong(),
            estateFormBinding.etEstate.text.toString(),
            Integer.parseInt(estateFormBinding.etSurface.text.toString()),
            Integer.parseInt(estateFormBinding.etRooms.text.toString().replace("5 et +", "5")),
            Integer.parseInt(estateFormBinding.etBedrooms.text.toString().replace("5 et +", "5")),
            Integer.parseInt(estateFormBinding.etBathrooms.text.toString().replace("4 et +", "4")),
             ground,
            estateFormBinding.etPrice.text.toString().toDouble(),
            estateFormBinding.etDescription.text.toString(),
            estateFormBinding.boxSchools.isChecked,
            estateFormBinding.boxStores.isChecked,
            estateFormBinding.boxPark.isChecked,
            estateFormBinding.boxRestaurants.isChecked,
            estateFormBinding.availableCheckbtn.isChecked,
             estateFormBinding.saleDate.text.toString(),
            estateFormBinding.soldDate.text.toString(),
            estateFormBinding.etAgent.text.toString(),
            photo,
             photoList,
            video,location)

        Log.d("saveEstate", "saveEstate$estate")

        executeInsert()
        finish()

    }

    private fun saleDateRequired() : Boolean{
        val saleDateInput = estateFormBinding.inputUpOfSaleDate.editText?.text.toString()
        if (saleDateInput.isEmpty()){
            estateFormBinding.saleDate.error = R.string.require.toString()
            return false
        }
        return true
    }

    private fun soldDatedRequired() : Boolean {
        val soldDateInput = estateFormBinding.inputSoldDate.editText?.text.toString()
        if (soldDateInput.isEmpty() && estateFormBinding.availableCheckbtn.isChecked){
            estateFormBinding.soldDate.error = R.string.require.toString()
            return false
        }
        return true
    }

    //RX Java http request for geocoding API
    private fun executeInsert() {
        completeAddress = estate.locationEstate.address + estate.locationEstate.city+ estate.locationEstate.zipCode
        EstateManagerStream.streamFetchGeocode(completeAddress)
            .subscribeWith(object : DisposableObserver<Geocoding>() {
                override fun onNext(geocoding: Geocoding) {
                    if (!geocoding.results.isNullOrEmpty()){
                        estate.locationEstate.latitude = geocoding.results[0].geometry.location.lat
                        estate.locationEstate.longitude = geocoding.results[0].geometry.location.lng
                    }else{
                        Log.d("Geocoding","Geocoding : Null or Empty")
                    }
                }
                override fun onError(@NonNull e: Throwable) { Log.e("Geocoding","Error insert",e) }
                override fun onComplete() {}
            })
        if (estateEdit == 0L){
            estateViewModel.insertEstates(estate,this)
        }else{
            estateViewModel.updateEstate(estate)
        }
    }
}