package com.example.threads.view.user_fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.databinding.FragmentEditProfileBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.UserDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var circularImageView: ShapeableImageView
    private val userDataViewModel by viewModel<UserDataViewModel>()
    private var selectedImageUri: Uri? = null
    private lateinit var imageContainer: ViewGroup
    private lateinit var loadingDialogUtil: LoadingDialogUtil

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

        loadingDialogUtil = LoadingDialogUtil(requireContext())

        navigation()
        imageInstall()
//      finishUpdateUser()
        checkUpdating()
        fetchData()
        updateUser()
        checkPhotoUpdate()
    }

    private fun checkPhotoUpdate() {
        userDataViewModel.imageAddedSuccess.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(requireContext(), "Photo added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Photo was not uploaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 123
    }

    private fun selectImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val selectedImageUri = data?.data
//
//            if (selectedImageUri != null) {
//                userDataViewModel.uploadProfilePicture(getFileFromUri(selectedImageUri))
//            } else {
//                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the selected image URI
            val selectedImageUri = data?.data

            if (selectedImageUri != null) {
                // Compress the selected image
                val compressedImage = compressImage(getRealPathFromURI(selectedImageUri), 800, 600, 80)

                if (compressedImage != null) {
                    // Now, you can send the compressed image to the server
                    userDataViewModel.uploadProfilePicture(compressedImage)
                } else {
                    // Handle the case where compression failed
                    Toast.makeText(requireContext(), "Image compression failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle the case where the selectedImageUri is null
            }
        }
    }

    // Helper function to get the real path from URI
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(columnIndex ?: 0) ?: ""
        cursor?.close()
        return filePath
    }

    fun compressImage(imagePath: String, maxWidth: Int, maxHeight: Int, quality: Int): ByteArray? {
        try {
            // Decode the image file into a Bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)

            // Calculate the inSampleSize to scale the image down
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)

            // Decode the image with the calculated inSampleSize
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(imagePath, options)

            // Convert the Bitmap to a byte array with compression
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            return outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // Calculate the inSampleSize to scale the image down
    private fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        var inSampleSize = 1

        if (imageHeight > maxHeight || imageWidth > maxWidth) {
            val halfHeight = imageHeight / 2
            val halfWidth = imageWidth / 2

            while ((halfHeight / inSampleSize) >= maxHeight && (halfWidth / inSampleSize) >= maxWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun getFileFromUri(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (columnIndex != -1) {
                val filePath = it.getString(columnIndex)
                if (!filePath.isNullOrBlank()) {
                    return File(filePath)
                }
            }
        }
        return File("")
    }

    private fun updateUser() {
        binding.txtFinishEdit.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val name = binding.etName.text.toString()
            val bio = binding.etBio.text.toString()
            val link = binding.etLink.text.toString()
            val switch = binding.btnSwitch.isChecked

            val token = Holder.token
            val authHeader = "Bearer $token"

            val loginInstance = ProfileUpdateRequest(username, name, bio, link, switch)

            loadingDialogUtil.showLoadingDialog()

            userDataViewModel.updateUser(authHeader, loginInstance)
        }
    }

    private fun navigation() {
        binding.btnCancelEdit.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_userMainFragment)
        }
    }

//    private fun updateUser() {
//        val username = binding.etUsername.text.toString()
//        val name = binding.etName.text.toString()
//        val bio = binding.etBio.text.toString()
//        val link = binding.etLink.text.toString()
//        val switch = binding.btnSwitch.isChecked
//
//        val token = Holder.token
//        val authHeader = "Bearer $token"
//
//        selectedImageUri?.let { imageUri ->
//            val imageFile = ImageConverter.getFile(requireContext(), imageUri)
//            if (imageFile != null) {
//                val imagePart = prepareImagePart(imageFile)
//                val imageParts = imagePart
//
//                userDataViewModel.updateUserInfo(
//                    requireContext(),
//                    token = authHeader,
//                    username = username,
//                    profile_picture = imageUri,
//                    name = name,
//                    bio = bio,
//                    link = link,
//                    isPrivate = switch
//                )
//            } else {
//                Toast.makeText(requireContext(), "Error converting image", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        } ?: run {
//            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun fetchData() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        userDataViewModel.fetchUserInfo(authHeader)

        userDataViewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.etUsername.text =
                    Editable.Factory.getInstance().newEditable(userInfo.username)
                binding.etName.text =
                    Editable.Factory.getInstance().newEditable(userInfo.name ?: "")
                binding.etBio.text = Editable.Factory.getInstance().newEditable(userInfo.bio ?: "")
                binding.etLink.text =
                    Editable.Factory.getInstance().newEditable(userInfo.link ?: "")
                binding.btnSwitch.isChecked = userInfo.is_private

                userInfo.profile_picture?.let { uriString ->
                    val profilePictureUri = Uri.parse(uriString)
                    Glide.with(requireContext())
                        .load(profilePictureUri)
                        .into(binding.imgSelectImageUser)
                }
            }
        }
    }

    private fun checkUpdating() {
        userDataViewModel.updateStatus.observe(viewLifecycleOwner) { isSuccess ->

            loadingDialogUtil.dismissLoadingDialog()

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

//    private fun finishUpdateUser() {
//        binding.txtFinishEdit.setOnClickListener {
//
//            selectedImageUri?.let { imageUri ->
//                val imageFile = ImageConverter.getFile(requireContext(), imageUri)
//                if (imageFile != null) {
//                    val imagePart = prepareImagePart(imageFile)
//                    val imageParts = listOf(imagePart)
//                    userDataViewModel.inputItem(
//                        requireContext(),
//                        imageUri = imageUri
//                    )
//                } else {
//                    Toast.makeText(requireContext(), "Error converting image", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } ?: run {
//                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }

//    private fun openGallery2() {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
//    }

//    private fun prepareImagePart(imageFile: File): MultipartBody.Part {
//        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
//    }

    private fun imageInstall() {
        circularImageView = binding.imgSelectImageUser
        circularImageView.setImageResource(R.drawable.img_userphoto)

        val changePhotoTextView = binding.txtChangePhoto
        changePhotoTextView.setOnClickListener {
            showBottomSheetDialog()
        }
    }
//
//    private fun openGallery() {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
//    }
//
//    companion object {
//        private const val GALLERY_REQUEST_CODE = 123
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            selectedImageUri = data?.data
//            circularImageView.setImageURI(selectedImageUri)
//        }
//    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.btm_dialog_add_photo, null)

        val addPhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_addPhoto)
        val removePhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_removePhoto)

        addPhotoButton.setOnClickListener {
            selectImageFromGallery()
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
}