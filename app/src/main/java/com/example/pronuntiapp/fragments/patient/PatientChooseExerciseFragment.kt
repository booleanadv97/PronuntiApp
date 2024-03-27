package com.example.pronuntiapp.fragments.patient

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentPatientChooseExerciseBinding
import com.example.pronuntiapp.models.patient.ChooseExerciseViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class PatientChooseExerciseFragment : Fragment() {
    private lateinit var viewModel : ChooseExerciseViewModel
    private lateinit var binding: FragmentPatientChooseExerciseBinding
    private lateinit var mainLayout: ViewGroup
    private lateinit var hero: LottieAnimationView
    private lateinit var imageExerciseChar: LottieAnimationView
    private lateinit var imageReconChar: LottieAnimationView
    private lateinit var audioExerciseChar: LottieAnimationView
    private lateinit var txtMove: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = Firebase.auth.currentUser?.uid
        mainLayout = binding.main
        hero = binding.hero
        hero.visibility = View.INVISIBLE
        var flag = false
        viewModel.getHero(userId!!)
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
        imageExerciseChar = binding.imageExercise
        imageReconChar = binding.imageReconExercise
        audioExerciseChar = binding.AudioExercise
        txtMove = binding.txtMove
        var initialX = 0.0f
        var initialY = 0.0f
        hero.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(txtMove.visibility == View.VISIBLE)
                                txtMove.visibility = View.INVISIBLE
                            initialX = event.x
                            initialY = event.y
                            return true
                        }
                        MotionEvent.ACTION_UP ->{
                            val heroRect = Rect()
                            val imageExRect = Rect()
                            val imageReconRect = Rect()
                            val audioExerciseRect = Rect()
                            hero.getHitRect(heroRect)
                            imageExerciseChar.getHitRect(imageExRect)
                            imageReconChar.getHitRect(imageReconRect)
                            audioExerciseChar.getHitRect(audioExerciseRect)
                            if (heroRect.intersect(imageExRect)) {
                                val fragmentManager = requireActivity().supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.mainFrame, PlayImageExFragment())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            if (heroRect.intersect(imageReconRect)) {
                                val fragmentManager = requireActivity().supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.mainFrame, PlayImageReconExFragment())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            if (heroRect.intersect(audioExerciseRect)) {
                                val fragmentManager = requireActivity().supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.mainFrame, PlayAudioExFragment())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val deltaX = event.x - initialX
                            val deltaY = event.y - initialY
                            v!!.translationX += deltaX
                            v!!.translationY += deltaY
                            return true
                        }
                    }
                }
                mainLayout.invalidate()
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientChooseExerciseBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ChooseExerciseViewModel::class.java]
        return binding.root
    }

}