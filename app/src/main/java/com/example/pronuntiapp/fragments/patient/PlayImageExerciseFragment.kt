package com.example.pronuntiapp.fragments.patient

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentPlayImageExerciseBinding
import com.example.pronuntiapp.models.patient.PlayImgExViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso
import java.io.File


class PlayImageExerciseFragment : Fragment() {
    private lateinit var viewModel : PlayImgExViewModel
    private lateinit var binding : FragmentPlayImageExerciseBinding
    private lateinit var image : ImageView
    private lateinit var description : TextView
    private lateinit var instructions : TextView
    private lateinit var animRec : LottieAnimationView
    private val REQUEST_MICROPHONE_PERMISSION = 1
    private lateinit var mediaRecorder: MediaRecorder

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                requestStoragePermission()
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestMicrophonePermission()
        } else {
            val userId = Firebase.auth.currentUser?.uid
            image = binding.image
            image.visibility = View.INVISIBLE
            description = binding.txtDescription
            description.visibility = View.INVISIBLE
            animRec = binding.animVoiceRec
            animRec.visibility = View.INVISIBLE
            instructions = binding.txtInstructions
            instructions.visibility = View.INVISIBLE
            viewModel.getAssignedImageEx(userId!!)
            viewModel.imageExList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    viewModel.getImageEx()
                    viewModel.currentImgEx.observe(viewLifecycleOwner) { exercise ->
                        Picasso.get().load(exercise.url).into(image)
                        image.visibility = View.VISIBLE
                        description.visibility = View.VISIBLE
                        description.text = exercise.exerciseDescription
                        animRec.visibility = View.VISIBLE
                        instructions.visibility = View.VISIBLE
                        mediaRecorder = MediaRecorder().apply {
                            setAudioSource(MediaRecorder.AudioSource.MIC)
                            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        }
                        animRec.setOnTouchListener(object : View.OnTouchListener {
                            @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
                            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                                if (event != null) {
                                    when (event.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            if(instructions.visibility == View.VISIBLE)
                                                instructions.visibility = View.INVISIBLE
                                            viewModel.outputMP4File = getOutputFilePath()
                                            mediaRecorder.apply { setOutputFile(viewModel.outputMP4File) }
                                            mediaRecorder.prepare()
                                            mediaRecorder.start()
                                            return true
                                        }
                                        MotionEvent.ACTION_UP ->{
                                            mediaRecorder.stop()
                                            mediaRecorder.release()
                                            viewModel.audioAnsId = viewModel.randomString()
                                            viewModel.mp4Uri = Uri.fromFile(File(viewModel.outputMP4File))
                                            viewModel.addExerciseOnMP4Upload(userId)
                                            viewModel.addAnswerResult.observe(viewLifecycleOwner){
                                                if(it.isNotEmpty()) {
                                                    if (it == viewModel.ADD_ANSWER_RESULT_OK) {
                                                        viewModel.getRewards()
                                                        viewModel.givePoints(userId)
                                                        viewModel.reward.observe(viewLifecycleOwner){
                                                            reward ->
                                                            viewModel.rewardType.observe(viewLifecycleOwner){
                                                                rewardType ->
                                                                if(rewardType == "phrase"){
                                                                    val inflater = LayoutInflater.from(requireContext())
                                                                    val containerView = requireActivity().findViewById<FrameLayout>(
                                                                        R.id.framePatientGame)
                                                                    containerView.removeAllViews()
                                                                    val newView = inflater.inflate(R.layout.reward_phrase_layout, containerView, false)
                                                                    containerView.addView(newView)
                                                                    val phraseTxt = newView.findViewById<TextView>(R.id.txtPhrase)
                                                                    phraseTxt.text = reward
                                                                }
                                                                if(rewardType == "image"){
                                                                    val inflater = LayoutInflater.from(requireContext())
                                                                    val containerView = requireActivity().findViewById<FrameLayout>(
                                                                        R.id.framePatientGame)
                                                                    containerView.removeAllViews()
                                                                    val newView = inflater.inflate(R.layout.reward_image_layout, containerView, false)
                                                                    containerView.addView(newView)
                                                                    val rewardImg = newView.findViewById<ImageView>(R.id.rewardImageView)
                                                                    Picasso.get().load(reward).into(rewardImg)
                                                                }
                                                                if(rewardType == "video"){
                                                                    val videoView : VideoView
                                                                    val inflater = LayoutInflater.from(requireContext())
                                                                    val containerView = requireActivity().findViewById<FrameLayout>(
                                                                        R.id.framePatientGame)
                                                                    containerView.removeAllViews()
                                                                    val newView = inflater.inflate(R.layout.reward_video_layout, containerView, false)
                                                                    containerView.addView(newView)
                                                                    val webView = newView.findViewById<WebView>(R.id.video_view)
                                                                    webView.settings.javaScriptEnabled = true
                                                                    webView.loadData(
                                                                        reward!!,
                                                                        "text/html",
                                                                        "UTF-8"
                                                                    )
                                                                }
                                                                val timer = object : CountDownTimer(5000, 1000) {
                                                                    override fun onTick(millisUntilFinished: Long) {
                                                                    }

                                                                    override fun onFinish() {
                                                                        fragmentManager?.popBackStack()
                                                                    }
                                                                }
                                                                timer.start()
                                                            }
                                                        }
                                                    } else {
                                                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                                    }
                                                    viewModel.resetAddAnswerResult()
                                                }
                                            }
                                            return true
                                        }
                                    }
                                }
                                return true
                            }
                        })
                    }
                }else{
                    description.visibility = View.VISIBLE
                    description.text = requireActivity().resources.getString(R.string.no_assignments)
                }
            }
        }
    }
    private fun requestMicrophonePermission() {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            REQUEST_MICROPHONE_PERMISSION
        )
    }
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayImageExerciseBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[PlayImgExViewModel::class.java]
        return binding.root
    }
    private fun getOutputFilePath(): String {
        val appContext = requireActivity().applicationContext
        val filename = "recording_${System.currentTimeMillis()}.mp4"
        val internalPath = appContext.filesDir.absolutePath
        return File(internalPath, filename).toString()
    }
}