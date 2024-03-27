package com.example.pronuntiapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.Appointment
import com.example.pronuntiapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentsListViewAdapter(private val context: Context,
                                   private val appointmentsList: List<Appointment>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return appointmentsList.size
    }

    //2
    override fun getItem(position: Int): Appointment {
        return appointmentsList[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.appointments_listview_item, parent, false)
        val appointment : Appointment = getItem(position)
        val txtAppointmentDate: TextView = rowView.findViewById(R.id.txtDate)
        val txtAppointmentTherapistCheck: TextView = rowView.findViewById(R.id.txtTherapistCheck)
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ITALY).format(appointment.appointmentDate)
        val txtAppointmentDateTxt = "${context.getString(R.string.appointment_date_txt)} : $date"
        txtAppointmentDate.text = txtAppointmentDateTxt
        val check = if(appointment.therapistCheck == "") "No" else appointment.therapistCheck
        val txtAppointmentTherapistCheckTxt = "${context.getString(R.string.appointment_check_txt)} : $check"
        txtAppointmentTherapistCheck.text = txtAppointmentTherapistCheckTxt
        return rowView
    }
}