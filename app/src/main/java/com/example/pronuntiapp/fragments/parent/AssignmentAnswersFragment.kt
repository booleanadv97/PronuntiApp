package com.example.pronuntiapp.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.adapters.AudioAnswersGridViewAdapter
import com.example.pronuntiapp.adapters.ImageAnswersGridViewAdapter
import com.example.pronuntiapp.adapters.ImageReconAnswersGridViewAdapter
import com.example.pronuntiapp.databinding.FragmentAssignmentAnswersBinding
import com.example.pronuntiapp.models.parent.assignment.AssignmentAnswersViewModel

class AssignmentAnswersFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentAnswersBinding
    private lateinit var viewModel: AssignmentAnswersViewModel
    private lateinit var gridView: GridView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val assignId = arguments?.getString("assignId")!!
        val exerciseType = arguments?.getString("exerciseType")!!
        gridView = binding.gridViewAnswers
        when (exerciseType) {
            "Image Exercise" -> {
                viewModel.getImageAssignAnswers(assignId)
                viewModel.imageAnswers.observe(viewLifecycleOwner) { list ->
                    val exerciseAdapter =
                        context?.let { ImageAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }

            "Image Recognition Exercise" -> {
                viewModel.getImageReconAssignAnswers(assignId)
                viewModel.imageReconAnswers.observe(viewLifecycleOwner) { list ->
                    val exerciseAdapter =
                        context?.let { ImageReconAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }

            "Audio Exercise" -> {
                viewModel.getAudioAssignAnswers(assignId)
                viewModel.audioAnswers.observe(viewLifecycleOwner) { list ->
                    val exerciseAdapter =
                        context?.let { AudioAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignmentAnswersBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AssignmentAnswersViewModel::class.java]
        return binding.root
    }
}