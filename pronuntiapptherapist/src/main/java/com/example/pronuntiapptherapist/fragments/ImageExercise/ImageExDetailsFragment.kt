package com.example.pronuntiapptherapist.fragments.ImageExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentImageExDetailsBinding
import com.example.pronuntiapptherapist.fragments.AssignExercise
import com.squareup.picasso.Picasso

class ImageExDetailsFragment : Fragment() {
    private lateinit var binding : FragmentImageExDetailsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseName = arguments?.getString("exerciseName")
        val txtExerciseNameTxt = "${requireActivity().resources.getString(R.string.txt_exercise_name)} : $exerciseName"
        val exerciseDescription = arguments?.getString("exerciseDescription")
        val txtExerciseDescriptionTxt = "${requireActivity().resources.getString(R.string.txt_exercise_description)} : $exerciseDescription"
        val imageUrl = arguments?.getString("imageUrl")
        binding.txtExerciseName.text = txtExerciseNameTxt
        binding.txtExerciseDescription.text = txtExerciseDescriptionTxt
        Picasso.get().load(imageUrl).into(binding.imageView)
        binding.buttonSubmit.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("exerciseName",exerciseName)
            bundle.putString("exerciseType","Image Exercise")
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val targetFragment = AssignExercise()
            targetFragment.arguments = bundle
            fragmentTransaction.replace(R.id.frameLayoutTherapist, targetFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageExDetailsBinding.inflate(inflater)
        return binding.root
    }

}