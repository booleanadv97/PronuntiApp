package com.example.pronuntiapptherapist.models.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.UUID

class AppointmentsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val appointmentsRef = database.getReference("Appointments")
    private val _appointmentsList : MutableLiveData<List<Appointment>> = MutableLiveData(emptyList())
    val appointmentsList : LiveData<List<Appointment>> = _appointmentsList
    val APPOINTMENT_RESULT_OK = "OK"
    private val _addAppointmentResult = MutableLiveData<String>()
    val addAppointmentResult : LiveData<String> = _addAppointmentResult
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

    fun resetResult(){
        _addAppointmentResult.value = ""
    }

    fun addAppointment(parentId: String, date : String, hour : String){
        val appointmentId = UUID.randomUUID().toString()
        val appointment = Appointment(appointmentId, parentId, date, hour, "Yes")
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
                            _addAppointmentResult.value = it.stackTraceToString()
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