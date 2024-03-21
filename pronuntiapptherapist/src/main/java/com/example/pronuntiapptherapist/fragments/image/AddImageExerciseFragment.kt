package com.example.pronuntiapptherapist.fragments.image

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentAddImageExerciseBinding
import com.example.pronuntiapptherapist.models.image.AddImageExerciseViewModel


@Suppress("DEPRECATION")
class AddImageExerciseFragment : Fragment() {
    private lateinit var viewModel : AddImageExerciseViewModel
    private lateinit var binding: FragmentAddImageExerciseBinding
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                if (result.data?.data != null) {
                    viewModel.imageUri = result.data?.data!!
                    viewModel.imgName = viewModel.getFileName(requireContext(), viewModel.imageUri)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUpload.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }
        binding.buttonSubmit.setOnClickListener() {
            if (!checkFields())
            {
                Toast.makeText(
                    context, requireActivity().resources.getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.addImageExerciseToRTDB(
                    binding.editTextImageExerciseName.text.toString(),
                    binding.editTextExerciseDescription.text.toString())
                var observed = false
                viewModel.exerciseResult.observe(viewLifecycleOwner){
                    if(!observed) {
                        when (it) {
                            viewModel.EXERCISE_STATUS_ONGOING -> {
                                binding.buttonSubmit.isEnabled = false
                                val inflater = LayoutInflater.from(requireContext())
                                val containerView = requireActivity().findViewById<FrameLayout>(R.id.frameLayoutTherapist)
                                containerView.removeAllViews()
                                val newView = inflater.inflate(R.layout.fragment_wait_upload, containerView, false)
                                containerView.addView(newView)
                                val txtProgress = newView.findViewById<TextView>(R.id.txtProgress)
                                val progressBar =  newView.findViewById<ProgressBar>(R.id.progressBarUpload)
                                viewModel.progressBarLevel.observe(viewLifecycleOwner){progressBarLevel ->
                                    progressBar.progress = progressBarLevel
                                    if(progressBarLevel < 100){
                                        viewModel.txtProgress.observe(viewLifecycleOwner){
                                            text -> txtProgress.text = text
                                        }
                                    }
                                    else {
                                        containerView.removeAllViews()
                                        fragmentManager?.popBackStack()
                                    }
                                }
                            }
                            else -> {
                                if(!binding.buttonSubmit.isEnabled)
                                    binding.buttonSubmit.isEnabled = true
                                Toast.makeText(
                                    context, "Errore: $it",
                                    Toast.LENGTH_SHORT
                                ).show()
                                observed = true
                            }
                        }
                    }
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImageExerciseBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AddImageExerciseViewModel::class.java]
        return binding.root
    }
    private fun checkFields() : Boolean{
        return !(binding.editTextImageExerciseName.toString().isEmpty() ||
            binding.textViewExerciseDescription.toString().isEmpty() ||
            viewModel.imgName!!.isEmpty()
        )
    }
}