package com.example.pronuntiapptherapist.fragments.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ViewAssignmentsGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentViewParentAssignmentsBinding
import com.example.pronuntiapptherapist.models.assignment.ViewAssignments

class ViewParentAssignmentsFragment : Fragment() {
    private lateinit var binding : FragmentViewParentAssignmentsBinding
    private lateinit var viewModel : ViewAssignments
    private lateinit var gridView : GridView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentId = arguments?.getString("parentId")
        gridView = binding.gridViewExercises
        viewModel.getAssignedExercises(parentId!!)
        viewModel.assignedExercisesList.observe(viewLifecycleOwner){
            listAssignments ->
            val exerciseAdapter =
                context?.let { ViewAssignmentsGridViewAdapter(assignmentsList = listAssignments, it) }
            gridView.adapter = exerciseAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val bundle = Bundle()
                    bundle.putString("assignId",listAssignments[position].assignId)
                    bundle.putString("exerciseType",listAssignments[position].exerciseType)
                    bundle.putString("therapistCheck",listAssignments[position].therapistCheck)
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val targetFragment = AssignmentAnswersFragment()
                    targetFragment.arguments = bundle
                    fragmentTransaction.replace(R.id.frameLayoutTherapist, targetFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewParentAssignmentsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ViewAssignments::class.java]
        return binding.root
    }
}