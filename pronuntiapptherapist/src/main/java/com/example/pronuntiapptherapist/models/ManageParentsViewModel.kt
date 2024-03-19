package com.example.pronuntiapptherapist.models

import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pronuntiapptherapist.adapters.ManageParentsGridViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageParentsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    var _parentsList : MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val parentsList : LiveData<List<User>> = _parentsList
    fun getParentsList(){
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<User>()
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        currentList = currentList + (
                            User(
                                child.getValue<User>()?.email,
                                child.getValue<User>()?.firstName,
                                child.getValue<User>()?.lastName
                            )
                        )
                        _parentsList.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}