package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentManageExercisesBinding
import com.example.pronuntiapptherapist.fragments.audio.ManageAudioExercisesFragment
import com.example.pronuntiapptherapist.fragments.image.ManageImageExercisesFragment
import com.example.pronuntiapptherapist.fragments.image_recognition.ManageImageRecognitionExercisesFragment


class ManageExercisesFragment : Fragment() {
    private lateinit var binding : FragmentManageExercisesBinding
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
        binding.cardViewAudioSeq.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutTherapist, ManageAudioExercisesFragment())
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