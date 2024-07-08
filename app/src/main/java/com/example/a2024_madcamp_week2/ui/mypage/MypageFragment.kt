package com.example.a2024_madcamp_week2.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a2024_madcamp_week2.databinding.FragmentMypageBinding
import android.widget.Button
import com.example.a2024_madcamp_week2.LogoutActivity
import android.content.Intent
import com.example.a2024_madcamp_week2.CalendarActivity


class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mypageViewModel =
            ViewModelProvider(this).get(MypageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMypage
        mypageViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val logoutButton: Button = binding.logoutButton
        logoutButton.setOnClickListener {
            val intent = Intent(activity, LogoutActivity::class.java)
            startActivity(intent)
        }

        val calendarButton: Button = binding.calendarButton
        calendarButton.setOnClickListener {
            val intent = Intent(activity, CalendarActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}