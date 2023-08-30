package com.example.threads.view.user_fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentEditProfileBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.ImageConverter
import com.example.threads.view_models.UserDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var circularImageView: ShapeableImageView
    private val userDataViewModel by viewModel<UserDataViewModel>()
    private var selectedImageUri: Uri? = null
    private lateinit var imageContainer: ViewGroup

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
        finishUpdateUser()
        checkUpdating()
    }

    private fun checkUpdating() {
        userDataViewModel.updateStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_editProfileFragment_to_userMainFragment)
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun finishUpdateUser() {
        binding.txtFinishEdit.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val username = binding.etUsername.text.toString()
        val name = binding.etName.text.toString()
        val bio = binding.etBio.text.toString()
        val link = binding.etLink.text.toString()
        val switch = false

        val token = Holder.token
        val authHeader = "Bearer $token"

        selectedImageUri?.let { imageUri ->
            val imageFile = ImageConverter.getFile(requireContext(), imageUri)
            if (imageFile != null) {
                val imagePart = prepareImagePart(imageFile)
                val imageParts = imagePart

                userDataViewModel.updateUserInfo(
                    requireContext(),
                    token = authHeader,
                    username = username,
                    profile_picture = imageUri,
                    name = name,
                    bio = bio,
                    link = link,
                    isPrivate = false
                )

                Log.d("TokenCheck", imagePart.toString())
            } else {
                Toast.makeText(requireContext(), "Error converting image", Toast.LENGTH_SHORT)
                    .show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepareImagePart(imageFile: File): MultipartBody.Part {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
    }


    private fun imageInstall() {
        circularImageView = binding.imgSelectImageUser
        circularImageView.setImageResource(R.drawable.img_userphoto)

        val changePhotoTextView = binding.txtChangePhoto
        changePhotoTextView.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun navigation() {
//        binding.btnCancelEdit.setOnClickListener {
//            findNavController().navigate(R.id.action_editProfileFragment_to_userMainFragment)
//        }
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
            selectedImageUri = null
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
            selectedImageUri = data?.data
            circularImageView.setImageURI(selectedImageUri)
        }
    }

}