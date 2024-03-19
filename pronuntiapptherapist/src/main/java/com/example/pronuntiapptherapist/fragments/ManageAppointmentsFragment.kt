package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pronuntiapptherapist.databinding.FragmentManageAppointmentsBinding

class ManageAppointmentsFragment : Fragment() {
    private lateinit var binding : FragmentManageAppointmentsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageAppointmentsBinding.inflate(inflater)
        return binding.root
    }
}