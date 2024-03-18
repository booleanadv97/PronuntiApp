package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentManageExercisesBinding
import com.google.firebase.database.FirebaseDatabase


class ManageExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageExercisesBinding
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val imgExercisesRef = database.getReference("Image Exercises")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageExercisesBinding.inflate(inflater, container, false)
        binding.cardViewImageExercise.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutTherapist, ManageImageExercisesFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.cardViewImageRecognition.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutTherapist, ManageImageRecognitionExercisesFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return binding.root
    }
}