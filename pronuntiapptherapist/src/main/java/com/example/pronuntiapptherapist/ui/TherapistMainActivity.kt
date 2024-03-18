package com.example.pronuntiapptherapist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.ActivityTherapistMainBinding
import com.example.pronuntiapptherapist.fragments.HomeFragment
import com.example.pronuntiapptherapist.fragments.ManageExercisesFragment
import com.example.pronuntiapptherapist.fragments.ManageParentsFragment

val fragmentsTitle = arrayOf(
    "Home",
    "Manage parents",
    "Manage exercises",
)
class TherapistMainActivity : AppCompatActivity() {

    lateinit var binding : ActivityTherapistMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapistMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.parents -> replaceFragment(ManageParentsFragment())
                R.id.exercises -> replaceFragment(ManageExercisesFragment())
                else -> {}
            }
            true
        }
    }
     private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutTherapist, fragment)
        fragmentTransaction.commit()
    }
}