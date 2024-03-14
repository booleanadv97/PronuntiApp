package com.example.pronuntiapp

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.auth.FirebaseAuth

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val user : FirebaseAuth) : FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> HomeFragment()
            1 -> {
                ProfileFragment(user)
            }
            else -> { throw Resources.NotFoundException("Position not found") }
        }
    }
}