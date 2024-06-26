package com.example.pronuntiapptherapist.models


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageParentsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    var _parentsList : MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val parentsList : LiveData<List<User>> = _parentsList
    fun getParentsList(){
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<User>()
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        currentList = currentList + child.getValue<User>()!!
                        _parentsList.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}