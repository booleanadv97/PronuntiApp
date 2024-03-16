package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentTherapistHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentTherapistHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentTherapistHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}