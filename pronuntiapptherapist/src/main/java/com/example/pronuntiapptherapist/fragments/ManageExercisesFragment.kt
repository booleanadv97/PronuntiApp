package com.example.pronuntiapptherapist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.adapters.GridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageExercisesBinding
import com.example.pronuntiapptherapist.models.GridViewModal
import com.example.pronuntiapptherapist.ui.AddImageExercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class ManageExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageExercisesBinding
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val imgExercisesRef = database.getReference("Image Exercises")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageExercisesBinding.inflate(inflater, container, false)
        gridView = binding.gridViewExercises
        var exerciseList = listOf<GridViewModal>()
        imgExercisesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        exerciseList = exerciseList + GridViewModal(
                            child.getValue<GridViewModal>()?.exerciseName,
                            child.getValue<GridViewModal>()?.exerciseDescription,
                            child.getValue<GridViewModal>()?.url
                        )
                    }
                    val exerciseAdapter = context?.let { GridViewAdapter(exerciseList = exerciseList, it) }
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
                TODO("Not yet implemented")
            }
        })

        val addExerciseButton = binding.buttonAddExercise
        addExerciseButton.setOnClickListener {
            startActivity(Intent(context, AddImageExercise::class.java))
        }
        return binding.root
    }
}