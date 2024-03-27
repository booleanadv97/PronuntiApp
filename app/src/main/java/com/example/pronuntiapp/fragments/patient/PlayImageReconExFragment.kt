package com.example.pronuntiapp.fragments.patient

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import com.example.common_utils.models.ImageRecognitionExercise
import com.example.common_utils.models.MediaPlayerManager
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentPlayImageReconBinding
import com.example.pronuntiapp.models.patient.PlayImageReconExViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class PlayImageReconExFragment : Fragment() {
    private lateinit var binding: FragmentPlayImageReconBinding
    private lateinit var viewModel: PlayImageReconExViewModel
    private lateinit var hero: LottieAnimationView
    private lateinit var speechBubble: LottieAnimationView
    private lateinit var description: TextView
    private lateinit var correctImage: ImageView
    private lateinit var altImage: ImageView
    private lateinit var instructions: TextView
    var userId: String? = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = Firebase.auth.currentUser?.uid
        initHero(userId!!)
        initObjects()
        viewModel.getAssignedImageReconEx(userId!!)
        viewModel.imageReconExList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                viewModel.getImageReconEx()
                viewModel.currentEx.observe(viewLifecycleOwner) { exercise ->
                    initExercise(exercise)
                    hero.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            if (event != null) {
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        playAudioExercise(exercise)
                                        return true
                                    }
                                }
                            }
                            return true
                        }
                    })
                    correctImage.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            if (event != null) {
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        viewModel.answerCorrect = "Yes"
                                        addAnswer(true)
                                        return true
                                    }
                                }
                            }
                            return true
                        }
                    })
                    altImage.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            if (event != null) {
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        viewModel.answerCorrect = "No"
                                        addAnswer(false)
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
    private fun initObjects(){
        speechBubble = binding.speechAnim
        description = binding.txtDescription
        instructions = binding.txtInstructions
        if (Math.random() < 0.5) {
            correctImage = binding.imageHolder1
            altImage = binding.imageHolder2
        } else {
            correctImage = binding.imageHolder2
            altImage = binding.imageHolder1
        }
        speechBubble.visibility = View.INVISIBLE
        description.visibility = View.INVISIBLE
        altImage.visibility = View.INVISIBLE
        correctImage.visibility = View.INVISIBLE
        instructions.visibility = View.INVISIBLE
    }

    private fun initExercise(exercise : ImageRecognitionExercise){
        Picasso.get().load(exercise.urlCorrectAnswerImage).into(correctImage)
        correctImage.visibility = View.VISIBLE
        Picasso.get().load(exercise.urlAlternativeImage).into(altImage)
        altImage.visibility = View.VISIBLE
        description.visibility = View.VISIBLE
        description.text = exercise.exerciseDescription
        instructions.visibility = View.VISIBLE
    }

    private fun playAudioExercise(exercise: ImageRecognitionExercise){
        val mediaPlayer = MediaPlayerManager(requireContext())
        if(instructions.visibility == View.VISIBLE)
            instructions.visibility = View.INVISIBLE
        mediaPlayer.playAudio(exercise.audioUrl!!)
        mediaPlayer.mediaPlayer!!.setOnCompletionListener {
            speechBubble.visibility = View.INVISIBLE
        }
        speechBubble.visibility = View.VISIBLE
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

    @SuppressLint("SetJavaScriptEnabled", "DiscouragedApi")
    fun addAnswer(answerCorrect: Boolean) {
        viewModel.addImageReconExAnswer()
        viewModel.addAnswerResult.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                if (it == viewModel.ADD_ANSWER_RESULT_OK) {
                    if (answerCorrect) {
                        viewModel.getRewards()
                        viewModel.givePoints(userId!!)
                        viewModel.reward.observe(viewLifecycleOwner) { reward ->
                            viewModel.rewardType.observe(viewLifecycleOwner) { rewardType ->
                                showRewards(rewardType, reward)
                                playCorrectSoundEffect()
                                startRewardTimer()
                            }
                        }
                    }else{
                        swapLayout(R.layout.incorrect_answer_layout)
                        playIncorrectSoundEffect()
                        startRewardTimer()
                    }
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
                viewModel.resetAddAnswerResult()
            }
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

    private fun playIncorrectSoundEffect(){
        val rawFileId = R.raw.incorrect
        val mediaPlayer = MediaPlayer.create(
            requireContext(),
            rawFileId
        )
        mediaPlayer.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayImageReconBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[PlayImageReconExViewModel::class.java]
        return binding.root
    }
}