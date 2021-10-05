package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialogFragment
import com.openclassrooms.realestatemanager.R

class ImageDialog : AppCompatDialogFragment() {
    private lateinit var imageView: ImageView
    private lateinit var editTextDescription: EditText
    private var listener: DialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)

        imageView = view.findViewById(R.id.image_description)
        editTextDescription = view.findViewById(R.id.edit_description)

        builder.setView(view)
            .setNegativeButton("cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }

            })
            .setPositiveButton("ok",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val descirption: String = editTextDescription.getText().toString()
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