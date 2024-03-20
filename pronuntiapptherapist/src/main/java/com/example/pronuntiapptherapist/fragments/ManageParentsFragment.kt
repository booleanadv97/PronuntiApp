package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.adapters.ManageParentsGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageParentsBinding
import com.example.pronuntiapptherapist.models.ManageParentsViewModel


class ManageParentsFragment : Fragment() {
    private lateinit var viewModel : ManageParentsViewModel
    private lateinit var gridView : GridView
    private lateinit var binding : FragmentManageParentsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        viewModel.getParentsList()
        viewModel.parentsList.observe(viewLifecycleOwner) {
            list ->
            val manageParentsAdapter =
                context?.let { ManageParentsGridViewAdapter(parentsList = list, it) }
            gridView.adapter = manageParentsAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageParentsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ManageParentsViewModel::class.java]
        return binding.root
    }
}