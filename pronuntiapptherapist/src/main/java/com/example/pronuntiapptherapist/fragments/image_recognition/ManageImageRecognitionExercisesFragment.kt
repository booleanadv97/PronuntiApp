package com.example.pronuntiapptherapist.fragments.image_recognition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.common_utils.models.ImageRecognitionExercise
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ImageRecognitionExerciseGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentManageImageRecognitionExercisesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageImageRecognitionExercisesFragment : Fragment() {
    lateinit var gridView : GridView
    private lateinit var binding : FragmentManageImageRecognitionExercisesBinding
    private val database = FirebaseDatabase.getInstance()
    private val imgExercisesRef = database.getReference("Image Recognition Exercises")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = binding.gridViewExercises
        var exerciseList = listOf<ImageRecognitionExercise>()
        imgExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    binding.textView2.text = requireActivity().getString(R.string.txt_manage_image_recognition_land)
                    for (child in snapshot.children) {
                        exerciseList = exerciseList + child.getValue<ImageRecognitionExercise>()!!
                    }
                    val exerciseAdapter = context?.let { ImageRecognitionExerciseGridViewAdapter(exerciseList = exerciseList, it) }
                    gridView.adapter = exerciseAdapter
                    gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val bundle = Bundle()
                        bundle.putString("exerciseName",exerciseList[position].exerciseName)
                        bundle.putString("exerciseDescription",exerciseList[position].exerciseDescription)
                        bundle.putString("urlAlternativeImage",exerciseList[position].urlAlternativeImage)
                        bundle.putString("urlCorrectAnswerImage",exerciseList[position].urlCorrectAnswerImage)
                        bundle.putString("audioUrl",exerciseList[position].audioUrl)
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val targetFragment = ImageRecognitionExDetailsFragment()
                        targetFragment.arguments = bundle
                        fragmentTransaction.replace(R.id.frameLayoutTherapist, targetFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                }else{
                    binding.textView2.text = requireActivity().getString(R.string.no_exercises)
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