package TherapistActivities

import TherapistFragments.HomeFragment
import TherapistFragments.ManageExercisesFragment
import TherapistFragments.ManageParentsFragment
import TherapistFragments.SettingsFragment
import TherapistFragments.adapters.ViewPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.pronuntiapp.R
import com.google.android.material.tabs.TabLayout

class TherapistMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_main)
        setUpTabs()
    }
    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(),"")
        adapter.addFragment(ManageParentsFragment(),"")
        adapter.addFragment(ManageExercisesFragment(),"")
        adapter.addFragment(SettingsFragment(),"")
        val viewPager = findViewById<ViewPager>(R.id.viewPagerTherapist)
        viewPager.adapter = adapter
        val tabLayoutTherapist = findViewById<TabLayout>(R.id.tabLayoutTherapist)
        tabLayoutTherapist.setupWithViewPager(viewPager)
        tabLayoutTherapist.getTabAt(0)!!.setIcon(R.drawable.baseline_home_24)
        tabLayoutTherapist.getTabAt(1)!!.setIcon(R.drawable.baseline_accessibility_24)
        tabLayoutTherapist.getTabAt(2)!!.setIcon(R.drawable.baseline_book_24)
        tabLayoutTherapist.getTabAt(3)!!.setIcon(R.drawable.baseline_app_settings_alt_24)
    }
}