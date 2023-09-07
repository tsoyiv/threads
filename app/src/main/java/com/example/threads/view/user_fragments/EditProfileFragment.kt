package com.example.threads.view.user_fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
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
        checkUpdating()
        fetchData()
    }

    private fun uploadPhoto() {
        userDataViewModel.editPhoto(
            requireContext(),
            onSuccess = {
                Toast.makeText(requireContext(), "added", Toast.LENGTH_SHORT).show()
            },
            onError = {
                // Handle an unsuccessful response here
                Toast.makeText(requireContext(), "not", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.btm_dialog_add_photo, null)

        val addPhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_addPhoto)
        val removePhotoButton = bottomSheetView.findViewById<TextView>(R.id.btn_removePhoto)

        addPhotoButton.setOnClickListener {
            //selectImageFromGallery()
            chooseImage()
            bottomSheetDialog.dismiss()
        }

        removePhotoButton.setOnClickListener {
            selectedImageUri = Uri.EMPTY // Set your default image URI or clear it as needed
            circularImageView.setImageResource(R.drawable.img_userphoto)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun updateUser() {
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

    private fun navigation() {
        binding.btnCancelEdit.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_userMainFragment)
        }
        binding.txtFinishEdit.setOnClickListener {
            updateUser()
            uploadPhoto()
        }
    }
    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Holder.selectedImageUri = selectedImageUri
            Glide.with(this)
                .load(selectedImageUri)
                .circleCrop()
                .into(binding.imgSelectImageUser)
        }
    }
    private var PICK_IMAGE_REQUEST  = 1

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
    private fun imageInstall() {
        circularImageView = binding.imgSelectImageUser
        circularImageView.setImageResource(R.drawable.img_userphoto)

        val changePhotoTextView = binding.txtChangePhoto
        changePhotoTextView.setOnClickListener {
            showBottomSheetDialog()
        }
    }
}