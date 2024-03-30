package com.example.pronuntiapptherapist.fragments.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.adapters.AudioAnswersGridViewAdapter
import com.example.pronuntiapptherapist.adapters.ImageAnswersGridViewAdapter
import com.example.pronuntiapptherapist.adapters.ImageReconAnswersGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentAssignmentDetailsBinding
import com.example.pronuntiapptherapist.models.assignment.AssignmentAnswersViewModel

class AssignmentAnswersFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentDetailsBinding
    private lateinit var viewModel: AssignmentAnswersViewModel
    private lateinit var gridView: GridView
    private lateinit var btnCheck : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val assignId = arguments?.getString("assignId")!!
        val exerciseType = arguments?.getString("exerciseType")!!
        gridView = binding.gridViewAnswers
        btnCheck = binding.btnCheck
        btnCheck.isEnabled = false
        btnCheck.visibility = View.INVISIBLE
        when (exerciseType) {
            "Image Exercise" -> {
                viewModel.getImageAssignAnswers(assignId)
                viewModel.imageAnswers.observe(viewLifecycleOwner) { list ->
                    if(list.isNotEmpty())
                    {
                        btnCheck.isEnabled = true
                        btnCheck.visibility = View.VISIBLE
                    }
                    val exerciseAdapter =
                        context?.let { ImageAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }

            "Image Recognition Exercise" -> {
                viewModel.getImageReconAssignAnswers(assignId)
                viewModel.imageReconAnswers.observe(viewLifecycleOwner) { list ->
                    if(list.isNotEmpty())
                    {
                        btnCheck.isEnabled = true
                        btnCheck.visibility = View.VISIBLE
                    }
                    val exerciseAdapter =
                        context?.let { ImageReconAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }

            "Audio Exercise" -> {
                viewModel.getAudioAssignAnswers(assignId)
                viewModel.audioAnswers.observe(viewLifecycleOwner) { list ->
                    if(list.isNotEmpty())
                    {
                        btnCheck.isEnabled = true
                        btnCheck.visibility = View.VISIBLE
                    }
                    val exerciseAdapter =
                        context?.let { AudioAnswersGridViewAdapter(it, answers = list) }
                    gridView.adapter = exerciseAdapter
                }
            }
        }
        btnCheck.setOnClickListener {
            viewModel.markAssignAsComplete(assignId)
            viewModel.markResult.observe(viewLifecycleOwner) {
                if(it.isNotEmpty()){
                    if(it == viewModel.MARK_AS_COMPLETE_OK){
                        btnCheck.isEnabled = false
                        btnCheck.isVisible = false
                    }
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.resetMarkResult()
                }
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignmentDetailsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AssignmentAnswersViewModel::class.java]
        return binding.root
    }
}