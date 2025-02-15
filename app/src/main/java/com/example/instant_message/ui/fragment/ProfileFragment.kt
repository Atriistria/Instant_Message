package com.example.instant_message.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instant_message.databinding.FragmentProfileBinding
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.ui.activity.LoginActivity

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sessionManager = SessionManager.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginProfile.setOnClickListener{
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            sessionManager.clearSession()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}