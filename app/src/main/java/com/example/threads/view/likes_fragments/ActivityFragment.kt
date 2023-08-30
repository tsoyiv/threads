package com.example.threads.view.likes_fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.threads.R
import com.example.threads.databinding.FragmentActivityBinding
import com.example.threads.utils.ActivityFragmentAdapter

class ActivityFragment : Fragment() {

    private lateinit var binding: FragmentActivityBinding

    private lateinit var button1: AppCompatButton
    private lateinit var button2: AppCompatButton
    private lateinit var button3: AppCompatButton

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

        button1 = binding.btnActivityComments
        button2 = binding.btnActivityFollowing
        button3 = binding.btnActivityRequests

        setActiveButton(button1, viewPager, 0)

        button1.setOnClickListener { setActiveButton(button1, viewPager, 0) }
        button3.setOnClickListener { setActiveButton(button3, viewPager, 1) }
        button2.setOnClickListener { setActiveButton(button2, viewPager, 2) }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> setActiveButton(button1, viewPager, 0)
                    1 -> setActiveButton(button3, viewPager, 1)
                    2 -> setActiveButton(button2, viewPager, 2)
                }
            }
        })
    }

    private fun setActiveButton(activeButton: Button, viewPager: ViewPager2, position: Int) {
        button1.setBackgroundResource(if (activeButton == button1) R.drawable.btn_rounded else R.drawable.btn_rounded_user_page)
        button2.setBackgroundResource(if (activeButton == button2) R.drawable.btn_rounded else R.drawable.btn_rounded_user_page)
        button3.setBackgroundResource(if (activeButton == button3) R.drawable.btn_rounded else R.drawable.btn_rounded_user_page)

        button1.setTextColor(if (activeButton == button1) Color.WHITE else Color.BLACK)
        button2.setTextColor(if (activeButton == button2) Color.WHITE else Color.BLACK)
        button3.setTextColor(if (activeButton == button3) Color.WHITE else Color.BLACK)

        viewPager.setCurrentItem(position, true)
    }
}
