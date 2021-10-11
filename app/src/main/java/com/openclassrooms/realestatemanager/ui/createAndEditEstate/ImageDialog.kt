package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialogFragment
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import java.io.File
import android.provider.MediaStore

import android.graphics.Bitmap
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding
import com.openclassrooms.realestatemanager.databinding.LayoutDialogBinding


class ImageDialog(val contentUri: Uri?) : AppCompatDialogFragment() {
    private lateinit var binding: LayoutDialogBinding
    private var listener: DialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        binding = LayoutDialogBinding.inflate(layoutInflater)
        val view: View = binding.root


        binding.imageDescription.setImageURI(contentUri)

        builder.setView(view)
            .setNegativeButton("cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }

            })
            .setPositiveButton("ok",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val descirption: String = binding.editDescription.text.toString()
                    listener?.applyTexts(descirption)
                }

            })


        return builder.create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                        "must implement ExampleDialogListener"
            )
        }
    }

    interface DialogListener {
        fun applyTexts(descirption: String?)
    }
}