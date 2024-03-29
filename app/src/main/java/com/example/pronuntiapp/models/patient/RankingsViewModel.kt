package com.example.pronuntiapp.models.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class RankingsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance()
    private val users = database.getReference("users")
    private var _usersList : MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val usersList : LiveData<List<User>> = _usersList
    fun getUserList(){
        users.orderByChild("points").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<User>()
                if(snapshot.exists()) {
                    for (child in snapshot.children.reversed()) {
                            val user = child.getValue<User>()
                            currentList = currentList + user!!
                        _usersList.postValue(currentList)
                        }
                    }
                }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}