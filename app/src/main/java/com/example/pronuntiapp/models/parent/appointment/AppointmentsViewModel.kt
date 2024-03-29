package com.example.pronuntiapp.models.parent.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.Appointment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.UUID

class AppointmentsViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance()
    private val appointmentsRef = database.getReference("Appointments")
    private val _appointmentsList : MutableLiveData<List<Appointment>> = MutableLiveData(emptyList())
    val appointmentsList : LiveData<List<Appointment>> = _appointmentsList
    val APPOINTMENT_RESULT_OK = "OK"
    private val _addAppointmentResult = MutableLiveData<String>()
    val addAppointmentResult : LiveData<String> = _addAppointmentResult
    fun getAppointments(parentId : String){
        appointmentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<Appointment>()
                if(snapshot.exists())
                    for(child in snapshot.children){
                        val childParent : String = child.getValue<Appointment>()?.parentId!!
                        if(childParent == parentId){
                            currentList = currentList + child.getValue<Appointment>()!!
                            _appointmentsList.postValue(currentList)
                        }
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

    fun addAppointment(parentId: String, date : Long){
        val appointmentId = UUID.randomUUID().toString()
        val appointment = Appointment(appointmentId, parentId, date, "")
        appointmentsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var flag = false
                for(child in snapshot.children){
                    val childParent : String = child.getValue<Appointment>()?.parentId!!
                    val childDate : Long = child.getValue<Appointment>()?.appointmentDate!!
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