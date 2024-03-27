package com.example.pronuntiapp.fragments.patient

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.common_utils.models.AudioExercise
import com.example.common_utils.models.MediaPlayerManager
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentPlayAudioExBinding
import com.example.pronuntiapp.models.patient.PlayAudioExViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso
import java.io.File
@Suppress("DEPRECATION")
class PlayAudioExFragment : Fragment() {
    private lateinit var viewModel: PlayAudioExViewModel
    private lateinit var binding: FragmentPlayAudioExBinding
    private lateinit var hero: LottieAnimationView
    private lateinit var description: TextView
    private lateinit var instructions: TextView
    private lateinit var animRec: LottieAnimationView
    private lateinit var audioExChar: LottieAnimationView
    private lateinit var audioExSpeech: LottieAnimationView
    private lateinit var mediaRecorder: MediaRecorder

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = Firebase.auth.currentUser?.uid
        initHero(userId!!)
        initObjects()
        viewModel.getAssignedAudioEx(userId)
        viewModel.audioExList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                viewModel.getAudioEx()
                viewModel.currentAudioEx.observe(viewLifecycleOwner) { exercise ->
                    initExercise(exercise)
                    audioExChar.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            if (event != null) {
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        playExerciseAudio(exercise)
                                        return true
                                    }
                                }
                            }
                            return true
                        }
                    })
                    animRec.setOnTouchListener(object : View.OnTouchListener {
                        @SuppressLint(
                            "ClickableViewAccessibility"
                        )
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            if (event != null) {
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        startRecordingAnswer()
                                        return true
                                    }

                                    MotionEvent.ACTION_UP -> {
                                        stopRecordingAnswer()
                                        getAddAnswerResult(userId)
                                        return true
                                    }
                                }
                            }
                            return true
                        }
                    })
                }
            } else {
                description.visibility = View.VISIBLE
                description.text = requireActivity().resources.getString(R.string.no_assignments)
            }
        }
    }
    private fun getAddAnswerResult(userId : String){
        viewModel.addAnswerResult.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                if (it == viewModel.ADD_ANSWER_RESULT_OK) {
                    viewModel.getRewards()
                    viewModel.givePoints(userId)
                    viewModel.reward.observe(viewLifecycleOwner) { reward ->
                        viewModel.rewardType.observe(
                            viewLifecycleOwner
                        ) { rewardType ->
                            showRewards(rewardType, reward)
                            playCorrectSoundEffect()
                            startRewardTimer()
                        }
                    }
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.resetAddAnswerResult()
            }
        }
    }
    private fun playExerciseAudio(exercise : AudioExercise) {
        val mediaPlayer = MediaPlayerManager(requireContext())
        if (instructions.visibility == View.VISIBLE)
            instructions.visibility = View.INVISIBLE
        mediaPlayer.playAudio(exercise.audioUrl!!)
        mediaPlayer.mediaPlayer!!.setOnCompletionListener {
            audioExSpeech.visibility = View.INVISIBLE
        }
        audioExSpeech.visibility = View.VISIBLE
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun showRewards(rewardType : String, reward : String){
        if (rewardType == "phrase") {
            val newView = swapLayout(R.layout.reward_phrase_layout)
            val phraseTxt =
                newView.findViewById<TextView>(R.id.txtPhrase)
            phraseTxt.text = reward
        }
        if (rewardType == "image") {
            val newView = swapLayout(R.layout.reward_image_layout)
            val rewardImg =
                newView.findViewById<ImageView>(
                    R.id.rewardImageView
                )
            Picasso.get().load(reward)
                .into(rewardImg)
        }
        if (rewardType == "video") {
            val newView = swapLayout(R.layout.reward_video_layout)
            val webView =
                newView.findViewById<WebView>(R.id.video_view)
            webView.settings.javaScriptEnabled =
                true
            webView.loadData(
                reward,
                "text/html",
                "UTF-8"
            )
        }
    }
    private fun playCorrectSoundEffect(){
        val rawFileId = R.raw.correct
        val mediaPlayer = MediaPlayer.create(
            requireContext(),
            rawFileId
        )
        mediaPlayer.start()
    }
    private fun startRewardTimer(){
        val timer = object :
            CountDownTimer(5000, 1000) {
            override fun onTick(
                millisUntilFinished: Long
            ) {
            }

            override fun onFinish() {
                fragmentManager?.popBackStack()
            }
        }
        timer.start()
    }
    private fun swapLayout(layoutId : Int) : View{
        val inflater = LayoutInflater.from(
            requireContext()
        )
        val containerView =
            requireActivity().findViewById<FrameLayout>(
                R.id.mainFrame
            )
        containerView.removeAllViews()
        val newView = inflater.inflate(
            layoutId,
            containerView,
            false
        )
        containerView.addView(newView)
        return newView
    }
    private fun initExercise(exercise : AudioExercise){
        audioExChar.visibility = View.VISIBLE
        description.visibility = View.VISIBLE
        description.text = exercise.exerciseDescription
        animRec.visibility = View.VISIBLE
        instructions.visibility = View.VISIBLE
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
    }
    private fun initObjects(){
        description = binding.txtDescription
        description.visibility = View.INVISIBLE
        animRec = binding.animVoiceRec
        animRec.visibility = View.INVISIBLE
        instructions = binding.txtInstructions
        instructions.visibility = View.INVISIBLE
        audioExChar = binding.audioExercise
        audioExChar.visibility = View.INVISIBLE
        audioExSpeech = binding.animSpeech
        audioExSpeech.visibility = View.INVISIBLE
    }
    private fun initHero(userId : String){
        hero = binding.hero
        hero.visibility = View.INVISIBLE
        var flag = false
        viewModel.getHero(userId)
        viewModel.userHero.observe(viewLifecycleOwner){
            if(!flag) {
                when (it) {
                    "hero_1" -> hero.setAnimation(R.raw.hero_1)
                    "hero_2" -> hero.setAnimation(R.raw.hero_2)
                    "hero_3" -> hero.setAnimation(R.raw.hero_3)
                    else -> hero.setAnimation(R.raw.hero_1)
                }
                hero.visibility = View.VISIBLE
                hero.loop(true)
                flag = true
            }
        }
    }
    private fun stopRecordingAnswer(){
        mediaRecorder.stop()
        mediaRecorder.release()
        viewModel.audioAnsId = viewModel.randomString()
        viewModel.mp4Uri =
            Uri.fromFile(File(viewModel.outputMP4File))
        viewModel.addExerciseOnMP4Upload()
    }
    private fun startRecordingAnswer(){
        if (instructions.visibility == View.VISIBLE)
            instructions.visibility = View.INVISIBLE
        viewModel.outputMP4File = getOutputFilePath()
        mediaRecorder.apply { setOutputFile(viewModel.outputMP4File) }
        mediaRecorder.prepare()
        mediaRecorder.start()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayAudioExBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[PlayAudioExViewModel::class.java]
        return binding.root
    }

    private fun getOutputFilePath(): String {
        val appContext = requireActivity().applicationContext
        val filename = "recording_${System.currentTimeMillis()}.mp4"
        val internalPath = appContext.filesDir.absolutePath
        return File(internalPath, filename).toString()
    }
}