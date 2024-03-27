package com.example.pronuntiapp.models.parent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.Appointment
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class HomeViewModel : ViewModel() {
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    private val assignmentsRef = database.getReference("Assigned Exercises")
    private val appointmentsRef = database.getReference("Appointments")
    var userId : String? = ""
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user
    private val _appointmentsCounter = MutableLiveData<Int>()
    val appointmentsCounter : LiveData<Int> = _appointmentsCounter
    private val _assignmentsCounter = MutableLiveData<Int>()
    val assignmentsCounter : LiveData<Int> = _assignmentsCounter
    fun getUserData(){
        usersRef.child(userId!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _user.value = snapshot.getValue<User>()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getAppointmentsNumber(){
        appointmentsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _appointmentsCounter.value = 0
                for(child in snapshot.children){
                    val appointment = child.getValue<Appointment>()
                    if(appointment?.parentId!! == userId){
                        val currentTimeInMillis = System.currentTimeMillis()
                        if(currentTimeInMillis < appointment.appointmentDate!!)
                            _appointmentsCounter.value = _appointmentsCounter.value!! + 1
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getAssignmentsNumber(){
        assignmentsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _assignmentsCounter.value = 0
                for(child in snapshot.children){
                    val assignment = child.getValue<AssignedExercise>()
                    if(assignment?.userId!! == userId){
                        val currentTimeInMillis = System.currentTimeMillis()
                        if(currentTimeInMillis < assignment.endDate!!)
                            _assignmentsCounter.value = _assignmentsCounter.value!! + 1
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}