package com.example.pronuntiapp.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.adapters.RankingsListViewAdapter
import com.example.pronuntiapp.databinding.FragmentRankingsBinding
import com.example.pronuntiapp.models.patient.RankingsViewModel

class RankingsFragment : Fragment() {
    private lateinit var binding : FragmentRankingsBinding
    private lateinit var viewModel : RankingsViewModel
    private lateinit var listView : ListView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.listViewRankings
        viewModel.getUserList()
        viewModel.usersList.observe(viewLifecycleOwner){
                list ->
            val rankingsAdapter =
                context?.let { RankingsListViewAdapter(it, userList = list) }
            listView.adapter = rankingsAdapter
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[RankingsViewModel::class.java]
        return binding.root
    }
}