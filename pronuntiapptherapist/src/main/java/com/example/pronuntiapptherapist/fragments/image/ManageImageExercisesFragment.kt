package com.example.pronuntiapptherapist.fragments.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.common_utils.models.ImageExercise
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ImageExerciseGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageImageExercisesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class ManageImageExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageImageExercisesBinding
    private val database = FirebaseDatabase.getInstance()
    private val imgExercisesRef = database.getReference("Image Exercises")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        var exerciseList = listOf<ImageExercise>()
        imgExercisesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    binding.txtLand.text = requireActivity().resources.getString(R.string.image_exercises_land_text)
                    for (child in snapshot.children) {
                        exerciseList = exerciseList + child.getValue<ImageExercise>()!!
                    }
                    val exerciseAdapter = context?.let { ImageExerciseGridViewAdapter(exerciseList = exerciseList, it) }
                    gridView.adapter = exerciseAdapter
                    gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val bundle = Bundle()
                        bundle.putString("exerciseName",exerciseList[position].exerciseName)
                        bundle.putString("exerciseDescription",exerciseList[position].exerciseDescription)
                        bundle.putString("imageUrl",exerciseList[position].url)
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val targetFragment = ImageExDetailsFragment()
                        targetFragment.arguments = bundle
                        fragmentTransaction.replace(R.id.frameLayoutTherapist, targetFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                }else{
                    binding.txtLand.text = requireActivity().resources.getString(R.string.no_exercises)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val addExerciseButton = binding.buttonAddExercise
        addExerciseButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutTherapist, AddImageExerciseFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageImageExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }
}