package com.example.pronuntiapptherapist.fragments.AudioExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentAudioExDetailsBinding
import com.example.pronuntiapptherapist.fragments.Assignment.AssignExercise
import com.example.pronuntiapptherapist.models.MediaPlayerManager

class AudioExDetailsFragment : Fragment() {
    private lateinit var binding : FragmentAudioExDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = arguments
        val exerciseName = exercise!!.getString("exerciseName")
        val exerciseDescription = exercise.getString("exerciseDescription")
        val txtExerciseNameTxt = "${requireActivity().resources.getString(R.string.txt_exercise_name)} : $exerciseName"
        binding.txtExerciseName.text = txtExerciseNameTxt
        val txtExerciseDescriptionTxt = "${requireActivity().resources.getString(R.string.txt_exercise_description)} : $exerciseDescription"
        binding.txtExerciseDescription.text = txtExerciseDescriptionTxt
        val audioUrl = exercise.getString("audioUrl")
        val mediaPlayer = MediaPlayerManager(requireContext())
        val btnStop = binding.btnStop
        val btnPlay = binding.btnPlay
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
        binding.btnAssign.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("exerciseName",exerciseName)
            bundle.putString("exerciseType","Audio Exercise")
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
        binding = FragmentAudioExDetailsBinding.inflate(inflater)
        return binding.root
    }
}