package com.example.pronuntiapp
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
   override fun onStart() {
        super.onStart()
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            tabLayout = findViewById(R.id.tab_layout)
            viewPager = findViewById(R.id.viewPager2)
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, auth)
            TabLayoutMediator(tabLayout, viewPager){
                    tab,index ->
                tab.text = when(index){
                    0 -> {"Home"}
                    1 -> {"Profile"}
                    else -> { throw Resources.NotFoundException("Position not found")}
                }
            }.attach()
        }else {
            startActivity(Intent(this, Login::class.java))
        }
    }
}