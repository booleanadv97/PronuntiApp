package com.example.pronuntiapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment(private val user: FirebaseAuth) : Fragment() {

    private lateinit var usernameText : TextView
    private lateinit var btnSignOut : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val currentUser = user.currentUser
        usernameText = view.findViewById<TextView>(R.id.textUsername) as TextView
        btnSignOut = view.findViewById<Button>(R.id.buttonSignOut) as Button
        usernameText.text = String.format("Benvenuto nel tuo profilo,\n%s", currentUser?.email)
        btnSignOut.setOnClickListener(){
            user.signOut()
            startActivity(Intent(activity, Login::class.java))
        }
        return view
    }
}