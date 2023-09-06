package com.example.threads.view.creation_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.threads.R

class BottomMenuDialog : DialogFragment() {

    interface OnOptionSelectedListener {
        fun onOptionSelected(option: String)
    }
    private var optionSelectedListener: OnOptionSelectedListener? = null

    override fun onStart() {
        super.onStart()

        // Get the dialog window and its layout params
        val window = dialog?.window
        val layoutParams = window?.attributes

        // Set the dialog's width and height
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT

        // Set the dialog's position to the bottom left corner
        layoutParams?.gravity = Gravity.CENTER

        // Set the dialog's background to null (transparent)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        layoutParams?.x = -100// Replace with your desired X coordinate
        layoutParams?.y = -200

        // Apply the changes to the dialog's window
        window?.attributes = layoutParams
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_dialog_reply_options, container, false)

        val option1Button = view.findViewById<LinearLayout>(R.id.anyone)
        val option2Button = view.findViewById<LinearLayout>(R.id.profileFollow)
        val option3Button = view.findViewById<LinearLayout>(R.id.mentionedOnly)

        // Set click listeners for the buttons
        option1Button.setOnClickListener {
            optionSelectedListener?.onOptionSelected("Anyone can reply")
            dismiss()
        }

        option2Button.setOnClickListener {
            optionSelectedListener?.onOptionSelected("Profiles you follow")
            dismiss()
        }

        option3Button.setOnClickListener {
            optionSelectedListener?.onOptionSelected("Mentioned only")
            dismiss()
        }
        return view
    }

    fun setOptionSelectedListener(listener: OnOptionSelectedListener) {
        optionSelectedListener = listener
    }
}