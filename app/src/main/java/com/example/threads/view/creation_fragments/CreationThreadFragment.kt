package com.example.threads.view.creation_fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.data.models.ThreadRequest
import com.example.threads.databinding.FragmentCreationThreadBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.ThreadViewModel
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreationThreadFragment : Fragment(), BottomMenuDialog.OnOptionSelectedListener {

    private lateinit var binding: FragmentCreationThreadBinding
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var itemImage: ImageView
    private lateinit var dropdownMenu: PopupWindow
    private val userDataViewModel by viewModel<UserDataViewModel>()
    private val threadViewModel by viewModel<ThreadViewModel>()
    private lateinit var etThread: EditText
    private lateinit var symbolsCounter: TextView
    private lateinit var txtPost: TextView
    private lateinit var loadingDialogUtil: LoadingDialogUtil

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
        loadingDialogUtil = LoadingDialogUtil(requireContext())

        navigation()
        callGallery()
//        callDropDownMenu()
        replyOptions()
        fetchData()
        btnPostEnableCheck()
        createThread()
        isThreadAddedSuccess()
        lengthChecker()
    }

    private fun lengthChecker() {
        val item_ownerThread = binding.itemOwnerThread
        val txtPost = binding.txtPost
        symbolsCounter = binding.textCharacterCount

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0

                symbolsCounter.text = "$textLength/240"
                if (textLength > 240 || textLength == 0) {
                    txtPost.isEnabled = false
                    symbolsCounter.setTextColor(Color.RED)
                } else {
                    txtPost.isEnabled = true
                    txtPost.setTextColor(Color.parseColor("#0096FF"))
                    symbolsCounter.setTextColor(Color.BLACK)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        item_ownerThread.addTextChangedListener(textWatcher)
    }

    private fun isThreadAddedSuccess() {
        threadViewModel.threadActionStatus.observe(viewLifecycleOwner) { isSuccessful ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccessful) {
                Toast.makeText(requireContext(), "Thread created", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_creationThreadFragment_to_tabMainFeedFragment)
            } else {
                Toast.makeText(requireContext(), "Thread was not added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createThread() {
        binding.txtPost.setOnClickListener {
            val addThread = binding.itemOwnerThread.text.toString()

            val token = Holder.token
            val authHeader = "Bearer $token"
            val threadInstance = ThreadRequest(addThread)

            loadingDialogUtil.showLoadingDialog()

            threadViewModel.createThread(authHeader, threadInstance)
        }
    }

    private fun btnPostEnableCheck() {
        txtPost = binding.txtPost
        etThread = binding.itemOwnerThread

        val imgRemove = binding.imgDeleteEver

        imgRemove.setOnClickListener {
            etThread.text.clear()
        }

        etThread.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = s?.isNotBlank() == true

                txtPost.isEnabled = hasText
                txtPost.setTextColor(
                    if (hasText) Color.parseColor("#0073cf") else Color.parseColor(
                        "#590095F6"
                    )
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun fetchData() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        userDataViewModel.fetchUserInfo(authHeader)

        userDataViewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.itemOwnerUsername.text = userInfo.username

                userInfo.profile_picture?.let { uriString ->
                    val profilePictureUri = Uri.parse(uriString)
                    val glideRequest = Glide.with(requireContext())
                        .load(profilePictureUri)

                    glideRequest.into(binding.itemViewOwnerImage)
                    glideRequest.into(binding.itemViewOwnerImageSmall)
                }

            }
        }
    }

    private fun replyOptions() {
        val replyOptions = binding.txtReplyOptions

        replyOptions.setOnClickListener {
            // Show the ReplyOptionsDialogFragment
            val dialogFragment = BottomMenuDialog()
            dialogFragment.setOptionSelectedListener(this)
            dialogFragment.show(childFragmentManager, "ReplyOptionsDialogFragment")
        }
    }

    override fun onOptionSelected(option: String) {
        val replyOptionsText = binding.txtReplyOptions
        replyOptionsText.text = option
    }

//    private fun callDropDownMenu() {
//        val replyOptions = arrayOf("Anyone can reply", "Only followers can reply", "No one can reply")
//
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, replyOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        val spinner = Spinner(requireContext())
//        spinner.adapter = adapter
//
//        dropdownMenu = PopupWindow(spinner, binding.txtReplyOptions.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
//        dropdownMenu.isOutsideTouchable = true
//
//        binding.txtReplyOptions.setOnClickListener {
//            dropdownMenu.showAsDropDown(binding.txtReplyOptions)
//        }
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedOption = replyOptions[position]
//                binding.txtReplyOptions.text = selectedOption
//                dropdownMenu.dismiss()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing
//            }
//        }
//    }


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