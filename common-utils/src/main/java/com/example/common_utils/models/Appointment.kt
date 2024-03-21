package com.example.common_utils.models

data class Appointment(
    val appointmentId : String ? = "",
    val parentId : String ? = "",
    val appointmentDate : String ? = "",
    val appointmentHour : String ? = "",
    val therapistCheck : String ? = ""
) {
}