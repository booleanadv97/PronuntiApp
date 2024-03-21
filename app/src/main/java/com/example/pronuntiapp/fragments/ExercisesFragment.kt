package com.example.pronuntiapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pronuntiapp.databinding.FragmentExercisesBinding

class ExercisesFragment : Fragment() {
    private lateinit var binding : FragmentExercisesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesBinding.inflate(inflater)
        return binding.root
    }

}