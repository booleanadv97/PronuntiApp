package com.example.common_utils.models

data class Appointment(
    val appointmentId : String ? = "",
    val parentId : String ? = "",
    val appointmentDate : Long? = 0,
    val therapistCheck : String ? = ""
) {
}