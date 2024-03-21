package com.example.pronuntiapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.pronuntiapp.R
import com.example.common_utils.models.Appointment

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
        val txtAppointmentDateTxt = "${context.getString(R.string.appointment_date_txt)} : ${appointment.appointmentDate}"
        txtAppointmentDate.text = txtAppointmentDateTxt
        val check = if(appointment.therapistCheck == "") "No" else appointment.therapistCheck
        val txtAppointmentTherapistCheckTxt = "${context.getString(R.string.appointment_check_txt)} : ${check}"
        txtAppointmentTherapistCheck.text = txtAppointmentTherapistCheckTxt
        val txtAppointmentHour: TextView = rowView.findViewById(R.id.txtHour)
        val txtAppointmentHourTxt = "${context.getString(R.string.appointment_hour_txt)} : ${appointment.appointmentHour}"
        txtAppointmentHour.text = txtAppointmentHourTxt
        return rowView
    }
}