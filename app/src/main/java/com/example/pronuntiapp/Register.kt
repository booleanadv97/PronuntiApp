package com.example.pronuntiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.registerButtonR)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddressR)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordR)
        val fNameEditText = findViewById<EditText>(R.id.editTextFName)
        val lNameEditText = findViewById<EditText>(R.id.editTextLName)
        signUpButton.setOnClickListener { _: View? ->
            val email: String = emailEditText.getText().toString().trim()
            val password: String = passwordEditText.getText().toString().trim()
            val fName = fNameEditText.getText().toString().trim()
            val lName = lNameEditText.getText().toString().trim()
            if( fName.isNotEmpty() && fName.all { it.isLetter() || it.isWhitespace() }
                    && lName.isNotEmpty() && lName.all { it.isLetter() || it.isWhitespace() }) {
                mAuth!!.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                            if (task.isSuccessful) {
                                // Sign up success, update UI with the signed-in user's information
                                Toast.makeText(
                                        this, "Sign up successful.",
                                        Toast.LENGTH_SHORT
                                ).show()
                                addUserToRTDB(task.result?.user?.uid.toString(), email, fName, lName)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(
                                        this, "Sign up failed.",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
            }else{
                Toast.makeText(
                        this, getString(R.string.invalid_input),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun addUserToRTDB(uid : String, email : String, fName : String, lName : String){
        val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("/users/$uid")
        val user = User(email, fName, lName)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "New registration : userid: $uid - email: $email")
                }
                .addOnFailureListener{
                    Log.d("RegisterActivity", "Failed to save user registration to database: ${it.message}")
                }
    }
}