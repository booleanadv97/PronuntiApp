package com.example.pronuntiapp.models.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class ChooseExerciseViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private var _userHero = MutableLiveData<String>()
    val userHero : LiveData<String> = _userHero

    fun getHero(userId : String){
        usersRef.child(userId).child("character").get().addOnSuccessListener {
            _userHero.value = it.value.toString()
        }
    }
}