package com.example.pronuntiapp.fragments.patient

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentPatientChooseExerciseBinding

class PatientChooseExercise : Fragment() {
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
        mainLayout = binding.main
        hero = binding.hero
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
                                fragmentTransaction.replace(R.id.framePatientGame, PlayImageExerciseFragment())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            if (heroRect.intersect(imageReconRect)) {
                                Toast.makeText(context,"imageRecon",Toast.LENGTH_SHORT).show()
                            }
                            if (heroRect.intersect(audioExerciseRect)) {
                                Toast.makeText(context,"audioEx",Toast.LENGTH_SHORT).show()
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
        return binding.root
    }

}