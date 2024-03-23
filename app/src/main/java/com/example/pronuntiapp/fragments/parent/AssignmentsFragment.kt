package com.example.pronuntiapp.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.adapters.ViewAssignmentsGridViewAdapter
import com.example.pronuntiapp.databinding.FragmentAssignmentsBinding
import com.example.pronuntiapp.models.parent.assignment.ViewAssignments
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AssignmentsFragment : Fragment() {
    private lateinit var binding : FragmentAssignmentsBinding
    private lateinit var viewModel : ViewAssignments
    private lateinit var gridView : GridView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAuth = Firebase.auth.currentUser?.uid
        val parentId : String = mAuth!!
        gridView = binding.gridViewExercises
        viewModel.getAssignedExercises(parentId)
        viewModel.assignedExercisesList.observe(viewLifecycleOwner){
                listAssignments ->
            val exerciseAdapter =
                context?.let { ViewAssignmentsGridViewAdapter(assignmentsList = listAssignments, it) }
            gridView.adapter = exerciseAdapter
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignmentsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ViewAssignments::class.java]
        return binding.root
    }
}