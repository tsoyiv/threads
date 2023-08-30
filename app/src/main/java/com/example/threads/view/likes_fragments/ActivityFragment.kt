package com.example.threads.view.likes_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.threads.R
import com.example.threads.databinding.FragmentActivityBinding
import com.example.threads.utils.ActivityFragmentAdapter

class ActivityFragment : Fragment() {

    private lateinit var binding: FragmentActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupState()
    }


    private fun setupState() {
        val viewPager = binding.viewPager
        val adapter = ActivityFragmentAdapter(requireActivity())
        viewPager.adapter = adapter

        binding.btnActivityComments.setOnClickListener {
            viewPager.currentItem = 0
            updateButtonStates(0)
        }

        binding.btnActivityRequests.setOnClickListener {
            viewPager.currentItem = 1
            updateButtonStates(1)
        }

        binding.btnActivityFollowing.setOnClickListener {
            viewPager.currentItem = 2
            updateButtonStates(2)
        }
    }

    private fun updateButtonStates(position: Int) {
        val btnComments = binding.btnActivityComments
        val btnRequests = binding.btnActivityRequests
        val btnFollowing = binding.btnActivityFollowing
        
        val currentPage = "comments";

        if ("comments".equals(currentPage)) {
            btnComments.setBackgroundResource(R.drawable.btn_google);
            btnComments.setTextColor(getResources().getColor(R.color.black));
            btnRequests.setBackgroundResource(R.drawable.btn_rounded);
            btnFollowing.setBackgroundResource(R.drawable.btn_rounded);
        } else if ("requests".equals(currentPage)) {
            btnComments.setBackgroundResource(R.drawable.btn_rounded);
            btnRequests.setBackgroundResource(R.drawable.btn_google);
            btnRequests.setTextColor(getResources().getColor(R.color.black));
            btnFollowing.setBackgroundResource(R.drawable.btn_rounded);
        } else if ("following".equals(currentPage)) {
            btnComments.setBackgroundResource(R.drawable.btn_rounded);
            btnRequests.setBackgroundResource(R.drawable.btn_rounded);
            btnFollowing.setBackgroundResource(R.drawable.btn_google);
            btnFollowing.setTextColor(getResources().getColor(R.color.black));
        }
    }
}
