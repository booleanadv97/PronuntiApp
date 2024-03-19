package com.example.pronuntiapptherapist.fragments.ImageRecognitionExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ImageRecognitionExerciseGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageImageRecognitionExercisesBinding
import com.example.pronuntiapptherapist.models.ImageRecognitionExercise.ImageRecognitionExercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageImageRecognitionExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageImageRecognitionExercisesBinding
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val imgExercisesRef = database.getReference("Image Recognition Exercises")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        var exerciseList = listOf<ImageRecognitionExercise>()
        imgExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        exerciseList = exerciseList + ImageRecognitionExercise(
                            child.getValue<ImageRecognitionExercise>()?.exerciseName,
                            child.getValue<ImageRecognitionExercise>()?.exerciseDescription,
                            child.getValue<ImageRecognitionExercise>()?.urlCorrectAnswerImage,
                            child.getValue<ImageRecognitionExercise>()?.imgCorrectAnswerId,
                            child.getValue<ImageRecognitionExercise>()?.urlAlternativeImage,
                            child.getValue<ImageRecognitionExercise>()?.imgAlternativeId,
                            child.getValue<ImageRecognitionExercise>()?.audioId,
                            child.getValue<ImageRecognitionExercise>()?.audioUrl
                        )
                    }
                    val exerciseAdapter = context?.let { ImageRecognitionExerciseGridViewAdapter(exerciseList = exerciseList, it) }
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
            fragmentTransaction.replace(R.id.frameLayoutTherapist, AddImageRecognitionExerciseFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageImageRecognitionExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }
}