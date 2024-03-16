package com.example.pronuntiapptherapist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pronuntiapptherapist.databinding.ActivityTherapistMainBinding
import com.example.pronuntiapptherapist.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

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
        val viewPager = binding.viewPagerTherapist
        val tabLayout = binding.tabLayoutTherapist
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentsTitle[position]
        }.attach()
    }
}