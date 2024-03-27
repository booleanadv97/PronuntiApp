package com.example.pronuntiapptherapist.fragments.audio

import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentAddAudioExerciseBinding
import com.example.pronuntiapptherapist.models.audio.AddAudioExerciseViewModel
import java.io.File

@Suppress("DEPRECATION")
class AddAudioExerciseFragment : Fragment() {
    private lateinit var binding: FragmentAddAudioExerciseBinding
    private lateinit var viewModel: AddAudioExerciseViewModel
    private lateinit var mediaRecorder: MediaRecorder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStopRec.isEnabled = false
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        binding.buttonStartRec.setOnClickListener {
            binding.buttonStartRec.isEnabled = false
            binding.buttonStopRec.isEnabled = true
            viewModel.outputMP4File = getOutputFilePath()
            mediaRecorder.apply { setOutputFile(viewModel.outputMP4File) }
            mediaRecorder.prepare()
            mediaRecorder.start()
        }
        binding.buttonStopRec.setOnClickListener {
            binding.buttonStopRec.isEnabled = false
            binding.buttonStartRec.isEnabled = true
            mediaRecorder.stop()
            mediaRecorder.release()
            viewModel.audioAnsId = viewModel.randomString()
            viewModel.mp4Uri = Uri.fromFile(File(viewModel.outputMP4File))
        }
        binding.buttonSubmit.setOnClickListener {
            if (checkFields()) {
                viewModel.addExerciseToRTDB(
                    binding.editTextImageExerciseName.text.toString(),
                    binding.editTextExerciseDescription.text.toString()
                )
                var observed = false
                viewModel.addExerciseResult.observe(viewLifecycleOwner) {
                    if (!observed) {
                        when (it) {
                            viewModel.EXERCISE_RESULT_ONGOING -> {
                                binding.buttonSubmit.isEnabled = false
                                val inflater = LayoutInflater.from(requireContext())
                                val containerView = requireActivity().findViewById<FrameLayout>(
                                    R.id.frameLayoutTherapist
                                )
                                containerView.removeAllViews()
                                val newView = inflater.inflate(
                                    R.layout.fragment_wait_upload,
                                    containerView,
                                    false
                                )
                                containerView.addView(newView)
                                val txtProgress = newView.findViewById<TextView>(R.id.txtProgress)
                                val progressBar =
                                    newView.findViewById<ProgressBar>(R.id.progressBarUpload)
                                viewModel.progressBarLevel.observe(viewLifecycleOwner) { progressBarLevel ->
                                    progressBar.progress = progressBarLevel
                                    if (progressBarLevel < 100) {
                                        viewModel.txtProgress.observe(viewLifecycleOwner) { text ->
                                            txtProgress.text = text
                                        }
                                    } else {
                                        containerView.removeAllViews()
                                        fragmentManager?.popBackStack()
                                    }
                                }
                            }

                            else -> {
                                if (!binding.buttonSubmit.isEnabled)
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
            } else {
                Toast.makeText(
                    context, requireActivity().resources.getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAudioExerciseBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this)[AddAudioExerciseViewModel::class.java]
        return binding.root
    }

    private fun checkFields(): Boolean {
        return (binding.editTextImageExerciseName.text.toString().isNotEmpty()
                && binding.editTextExerciseDescription.text.toString().isNotEmpty()
                && viewModel.audioAnsId!!.isNotEmpty())
    }

    private fun getOutputFilePath(): String {
        val appContext = requireActivity().applicationContext
        val filename = "recording_${System.currentTimeMillis()}.mp4"
        val internalPath = appContext.filesDir.absolutePath
        return File(internalPath, filename).toString()
    }
}