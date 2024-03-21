package com.example.pronuntiapptherapist.models.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.Appointment
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.UUID

class AppointmentsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val appointmentsRef = database.getReference("Appointments")
    private val usersRef = database.getReference("users")
    private val _appointmentsList : MutableLiveData<List<Appointment>> = MutableLiveData(emptyList())
    var _parentsList : MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val parentsList : LiveData<List<User>> = _parentsList
    val appointmentsList : LiveData<List<Appointment>> = _appointmentsList
    val APPOINTMENT_RESULT_OK = "OK"
    private val _addAppointmentResult = MutableLiveData<String>()
    val addAppointmentResult : LiveData<String> = _addAppointmentResult
    private val _confirmAppointment = MutableLiveData<String>()
    val confirmAppointment : LiveData<String> = _confirmAppointment
    fun getAppointments(){
        appointmentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<Appointment>()
                if(snapshot.exists())
                    for(child in snapshot.children){
                        currentList = currentList + child.getValue<Appointment>()!!
                        _appointmentsList.postValue(currentList)
                    }
                else
                    _appointmentsList.postValue(currentList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun resetConfirm(){
        _confirmAppointment.value = ""
    }
    fun confirmAppointment(appointment : Appointment){
        appointmentsRef.child(appointment.appointmentId!!)
            .child("therapistCheck").setValue("Si")
            .addOnSuccessListener{
                _confirmAppointment.value = APPOINTMENT_RESULT_OK
            }
            .addOnFailureListener{
                _confirmAppointment.value = "Errore nel cambiamento dello stato dell'appuntamento!"
                Log.d("$this",it.stackTraceToString())
            }
    }

    fun resetResult(){
        _addAppointmentResult.value = ""
    }
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

    fun addAppointment(parentId: String, date : String, hour : String){
        val appointmentId = UUID.randomUUID().toString()
        val appointment = Appointment(appointmentId, parentId, date, hour, "Si")
        appointmentsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var flag = false
                for(child in snapshot.children){
                    val childParent : String = child.getValue<Appointment>()?.parentId!!
                    val childDate : String = child.getValue<Appointment>()?.appointmentDate!!
                    if(childParent == parentId && childDate == date)
                        flag = true
                }
                if(!flag){
                    appointmentsRef.child(appointmentId).setValue(appointment)
                        .addOnSuccessListener {
                            _addAppointmentResult.value = APPOINTMENT_RESULT_OK
                            Log.d("$this","New appointment added $appointmentId")
                        }.addOnFailureListener{
                            Log.d("$this","Failed to add appointment $appointmentId, ${it.stackTraceToString()}")
                            _addAppointmentResult.value = "Errore nell'aggiunta dell'appuntamento!"
                        }
                }else{
                    _addAppointmentResult.value = "Errore: hai già un appuntamento in quella data!"
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}