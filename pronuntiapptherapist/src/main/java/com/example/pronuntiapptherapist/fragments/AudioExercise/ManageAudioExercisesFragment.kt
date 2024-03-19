package com.example.pronuntiapptherapist.fragments.AudioExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.AudioExerciseGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageAudioExercisesBinding
import com.example.pronuntiapptherapist.fragments.ImageExercise.AddImageExerciseFragment
import com.example.pronuntiapptherapist.models.AudioExercise.AudioExercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageAudioExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageAudioExercisesBinding
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val audioExercisesRef = database.getReference("Audio Exercises")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        var exerciseList = listOf<AudioExercise>()
        audioExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        exerciseList = exerciseList + AudioExercise(
                            child.getValue<AudioExercise>()?.exerciseName,
                            child.getValue<AudioExercise>()?.exerciseDescription,
                            child.getValue<AudioExercise>()?.audioUrl,
                            child.getValue<AudioExercise>()?.audioId
                        )
                    }
                    val exerciseAdapter = context?.let { AudioExerciseGridViewAdapter(exerciseList = exerciseList, it) }
                    gridView.adapter = exerciseAdapter
                    gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        Toast.makeText(
                            context, exerciseList[position].exerciseName + " selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val addExerciseButton = binding.buttonAddExercise
        addExerciseButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutTherapist, AddAudioExerciseFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageAudioExercisesBinding.inflate(inflater)
        return binding.root
    }
}