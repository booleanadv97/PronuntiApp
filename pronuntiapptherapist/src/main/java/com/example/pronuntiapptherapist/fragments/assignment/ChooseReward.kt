package com.example.pronuntiapptherapist.fragments.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentChooseRewardBinding
import com.example.pronuntiapptherapist.models.assignment.AssignExerciseViewModel

class ChooseReward : Fragment() {
    private lateinit var binding: FragmentChooseRewardBinding
    private lateinit var viewModel: AssignExerciseViewModel
    private lateinit var parentId: String
    private lateinit var exerciseType: String
    private lateinit var exerciseName: String
    private var startDate: Long = 0
    private var endDate: Long = 0
    private lateinit var reward: String
    private lateinit var rewardType: String
    private lateinit var newView : View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentId = arguments?.getString("parentId")!!
        exerciseType = arguments?.getString("exerciseType")!!
        exerciseName = arguments?.getString("exerciseName")!!
        startDate = arguments?.getLong("startDate")!!
        endDate = arguments?.getLong("endDate")!!

        binding.btnImgReward.setOnClickListener {
            resetView()
            val txtReward = newView.findViewById<TextView>(R.id.txtReward)
            val txtRewardTxt =
                "Url ${requireActivity().resources.getString(R.string.reward_img_txt)}:"
            txtReward.text = txtRewardTxt
            val editTextReward = newView.findViewById<EditText>(R.id.editTextReward)
            val btnSubmit = newView.findViewById<Button>(R.id.btnSubmit)
            btnSubmit.setOnClickListener {
                if (editTextReward.text.isNotEmpty()) {
                    rewardType = "image"
                    reward = editTextReward.text.toString()
                    if (URLUtil.isValidUrl(reward)) {
                        assignExercise()
                    } else {
                        Toast.makeText(context, "Url immagine non valido!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnVideoReward.setOnClickListener {
            resetView()
            val txtReward = newView.findViewById<TextView>(R.id.txtReward)
            val txtRewardTxt =
                "Url ${requireActivity().resources.getString(R.string.reward_video_txt)}:"
            txtReward.text = txtRewardTxt
            val editTextReward = newView.findViewById<EditText>(R.id.editTextReward)
            val btnSubmit = newView.findViewById<Button>(R.id.btnSubmit)
            btnSubmit.setOnClickListener {
                if (editTextReward.text.isNotEmpty()) {
                    rewardType = "video"
                    reward = editTextReward.text.toString()
                    assignExercise()
                } else {
                    Toast.makeText(context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnPhrase.setOnClickListener {
            resetView()
            val txtReward = newView.findViewById<TextView>(R.id.txtReward)
            val txtRewardTxt =
                "${requireActivity().resources.getString(R.string.reward_txt)}:"
            txtReward.text = txtRewardTxt
            val editTextReward = newView.findViewById<EditText>(R.id.editTextReward)
            val btnSubmit = newView.findViewById<Button>(R.id.btnSubmit)
            btnSubmit.setOnClickListener {
                if (editTextReward.text.isNotEmpty()) {
                    rewardType = "phrase"
                    reward = editTextReward.text.toString()
                    assignExercise()
                } else {
                    Toast.makeText(context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetView(){
        val inflater = LayoutInflater.from(requireContext())
        val containerView =
            requireActivity().findViewById<FrameLayout>(R.id.frameLayoutTherapist)
        containerView.removeAllViews()
        newView = inflater.inflate(R.layout.on_chosen_reward_type, containerView, false)
        containerView.addView(newView)
    }

    private fun assignExercise() {
        viewModel.assignExercise(
            parentId,
            exerciseType,
            exerciseName,
            startDate,
            endDate,
            rewardType,
            reward
        )
        viewModel.exerciseResult.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                if (it == viewModel.EXERCISE_RESULT_OK) {
                    Toast.makeText(
                        context, "Esercizio assegnato con successo",
                        Toast.LENGTH_SHORT
                    ).show()
                    val containerView =
                        requireActivity().findViewById<FrameLayout>(R.id.frameLayoutTherapist)
                    containerView.removeAllViews()
                    fragmentManager?.popBackStack()
                } else {
                    Toast.makeText(
                        context, "Errore: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.resetResult()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseRewardBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AssignExerciseViewModel::class.java]
        return binding.root
    }

}