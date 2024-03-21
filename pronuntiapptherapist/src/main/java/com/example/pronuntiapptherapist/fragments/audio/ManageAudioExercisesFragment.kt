package com.example.pronuntiapptherapist.fragments.audio

import AudioExerciseGridViewAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentManageAudioExercisesBinding
import com.example.pronuntiapptherapist.models.audio.ManageAudioExerciseViewModel


class ManageAudioExercisesFragment : Fragment() {
    private lateinit var gridView : GridView
    private lateinit var binding : FragmentManageAudioExercisesBinding
    private lateinit var viewModel : ManageAudioExerciseViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        viewModel.getExerciseList()
        val addExerciseButton = binding.buttonAddExercise
        addExerciseButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutTherapist, AddAudioExerciseFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        viewModel.exerciseList.observe(viewLifecycleOwner) {
                list ->
            if(list.isEmpty())
                binding.txtParentsList.text = requireActivity().resources.getString(R.string.no_exercises)
            else
                binding.txtParentsList.text = requireActivity().resources.getString(R.string.txt_audio_exercises_land)
            val exerciseAdapter =
                context?.let { AudioExerciseGridViewAdapter(it, dataSource = list) }
            gridView.adapter = exerciseAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val bundle = Bundle()
                    bundle.putString("exerciseName",list[position].exerciseName)
                    bundle.putString("exerciseDescription",list[position].exerciseDescription)
                    bundle.putString("audioUrl",list[position].audioUrl)
                    bundle.putString("audioId",list[position].audioId)
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val targetFragment = AudioExDetailsFragment()
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
        binding = FragmentManageAudioExercisesBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ManageAudioExerciseViewModel::class.java]
        return binding.root
    }
}