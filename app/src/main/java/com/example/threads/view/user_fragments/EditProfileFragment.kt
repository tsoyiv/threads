package com.example.threads.view.user_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentEditProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var circularImageView: ShapeableImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()
        navigation()
        imageInstall()
    }

    private fun imageInstall() {
        circularImageView = binding.imgSelectImageUser

        val changePhotoTextView = binding.txtChangePhoto
        changePhotoTextView.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun navigation() {
        binding.btnCancelEdit.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_userMainFragment)
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.btm_dialog_add_photo, null)

        val addPhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_addPhoto)
        val removePhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_removePhoto)

        addPhotoButton.setOnClickListener {
            openGallery()
            bottomSheetDialog.dismiss()
        }

        removePhotoButton.setOnClickListener {
            circularImageView.setImageResource(R.drawable.img_userphoto)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 123
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            circularImageView.setImageURI(imageUri)
        }
    }
}