package com.example.pronuntiapptherapist.fragments.ImageRecognitionExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentImageRecognitionExDetailsBinding
import com.example.pronuntiapptherapist.fragments.AssignExercise
import com.example.pronuntiapptherapist.models.MediaPlayerManager
import com.squareup.picasso.Picasso

class ImageRecognitionExDetailsFragment : Fragment() {
    private lateinit var binding : FragmentImageRecognitionExDetailsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseName = arguments?.getString("exerciseName")
        val exerciseDescription = arguments?.getString("exerciseDescription")
        val urlAlternativeImage = arguments?.getString("urlAlternativeImage")
        val ultCorrectAnswerImage = arguments?.getString("urlCorrectAnswerImage")
        val audioUrl = arguments?.getString("audioUrl")
        Picasso.get().load(urlAlternativeImage).into(binding.altImageView)
        Picasso.get().load(ultCorrectAnswerImage).into(binding.correctImageView)
        val txtExerciseName = "${requireActivity().resources.getString(R.string.txt_exercise_name)} : $exerciseName"
        binding.txtExerciseName.text = txtExerciseName
        val txtExerciseDescription = "${requireActivity().resources.getString(R.string.txt_exercise_description)} : $exerciseDescription"
        binding.txtExerciseDescription.text = txtExerciseDescription
        val btnStop = binding.btnStop
        val btnPlay = binding.btnPlay
        val mediaPlayer = MediaPlayerManager(requireContext())
        btnStop.isEnabled = false
        btnPlay.setOnClickListener{
            btnPlay.isEnabled = false
            btnStop.isEnabled = true
            mediaPlayer.playAudio(audioUrl!!)
            mediaPlayer.mediaPlayer!!.setOnCompletionListener {
                btnStop.isEnabled = false
                btnPlay.isEnabled = true
            }
        }
        btnStop.setOnClickListener{
            btnStop.isEnabled = false
            btnPlay.isEnabled = true
            mediaPlayer.stopPlayingAudio()
        }
        binding.btnSubmit.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("exerciseName",exerciseName)
            bundle.putString("exerciseType","Image Recognition Exercise")
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
        binding = FragmentImageRecognitionExDetailsBinding.inflate(inflater)
        return binding.root
    }

}