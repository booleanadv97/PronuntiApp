package TherapistFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.pronuntiapp.MainActivity
import com.example.pronuntiapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val welcomeText = view.findViewById<TextView>(R.id.textViewWelcome)
        welcomeText.text = getString(R.string.welcome, currentUser?.email)
        val buttonSignOut = view.findViewById<Button>(R.id.buttonSignOut)
        buttonSignOut.setOnClickListener{
            auth.signOut()
            startActivity(Intent(context,MainActivity::class.java))
            Toast.makeText(
                context, "You've signed out",
                Toast.LENGTH_SHORT
            ).show()
        }
        return view
    }
}