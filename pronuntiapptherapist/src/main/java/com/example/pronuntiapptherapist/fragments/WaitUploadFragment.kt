package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentWaitUploadBinding
import com.example.pronuntiapptherapist.models.AddImageRecognitionExerciseViewModel

class WaitUploadFragment : Fragment() {
    private lateinit var binding : FragmentWaitUploadBinding
    private lateinit var viewModel : AddImageRecognitionExerciseViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.progressBarLevel.observe(viewLifecycleOwner){
            if(it < 100)
                binding.txtProgress.text = "${it.toString()}%"
            else {
                binding.txtViewUpload.text = requireActivity().resources.getString(R.string.txt_upload_complete)
                fragmentManager?.popBackStack()
                fragmentManager?.popBackStack()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaitUploadBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(requireActivity())[AddImageRecognitionExerciseViewModel::class.java]
        return binding.root
    }
}