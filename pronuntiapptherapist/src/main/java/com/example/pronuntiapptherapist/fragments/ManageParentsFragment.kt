package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ManageParentsGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageParentsBinding
import com.example.pronuntiapptherapist.fragments.assignment.ViewParentAssignmentsFragment
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
            if(list.isEmpty())
                binding.txtParentsList.text = requireActivity().resources.getString(R.string.no_parents)
            else
                binding.txtParentsList.text = requireActivity().resources.getString(R.string.txt_parents_list_desc)
            val manageParentsAdapter =
                context?.let { ManageParentsGridViewAdapter(parentsList = list, it) }
            gridView.adapter = manageParentsAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val bundle = Bundle()
                    bundle.putString("parentId",list[position].userId)
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val targetFragment = ViewParentAssignmentsFragment()
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
        binding = FragmentManageParentsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ManageParentsViewModel::class.java]
        return binding.root
    }
}