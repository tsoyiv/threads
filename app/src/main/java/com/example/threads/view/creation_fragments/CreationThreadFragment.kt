package com.example.threads.view.creation_fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentCreationThreadBinding

class CreationThreadFragment : Fragment() {

    private lateinit var binding: FragmentCreationThreadBinding
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var itemImage: ImageView
    private lateinit var dropdownMenu: PopupWindow

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreationThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
        callGallery()
        callDropDownMenu()
    }

    private fun callDropDownMenu() {
        val replyOptions = arrayOf("Anyone can reply", "Only followers can reply", "No one can reply")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, replyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = Spinner(requireContext())
        spinner.adapter = adapter

        dropdownMenu = PopupWindow(spinner, binding.txtReplyOptions.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        dropdownMenu.isOutsideTouchable = true

        binding.txtReplyOptions.setOnClickListener {
            dropdownMenu.showAsDropDown(binding.txtReplyOptions)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = replyOptions[position]
                binding.txtReplyOptions.text = selectedOption
                dropdownMenu.dismiss()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }


    private fun callGallery() {
        val btnAddImageOnThread = binding.btnAddImageOnThread
        itemImage = binding.itemImage

        btnAddImageOnThread.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            itemImage.visibility = ImageView.VISIBLE
            itemImage.setImageURI(imageUri)

            imageUri?.let { adjustImageSize(it) }
        }
    }

    private fun adjustImageSize(imageUri: Uri) {
        val constraintLayout = binding.root
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        val aspectRatio = calculateImageAspectRatio(imageUri)
        val imageId = itemImage.id

        if (aspectRatio == 316f / 316f) {
            constraintSet.setDimensionRatio(imageId, "H,1:1")
        } else if (aspectRatio == 309f / 160f) {
            constraintSet.setDimensionRatio(imageId, "H,309:160")
        }

        constraintSet.applyTo(constraintLayout)
    }

    private fun calculateImageAspectRatio(imageUri: Uri): Float {
        val resolver = requireContext().contentResolver
        val inputStream = resolver.openInputStream(imageUri)
        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        val imageWidth = options.outWidth.toFloat()
        val imageHeight = options.outHeight.toFloat()

        return imageWidth / imageHeight
    }

    private fun navigation() {
        binding.btnCancelAddingThread.setOnClickListener {
            //parentFragmentManager.popBackStackImmediate()
            findNavController().navigate(R.id.action_creationThreadFragment_to_tabMainFeedFragment)
        }
    }
}