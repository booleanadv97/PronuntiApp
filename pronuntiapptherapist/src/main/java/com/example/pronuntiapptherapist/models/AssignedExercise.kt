package com.example.pronuntiapptherapist.models

data class AssignedExercise(
    val userId : String? = "",
    val exerciseType : String? = "",
    val exerciseName : String? = "",
    val answers : String? = "",
    val startDate : String? = "",
    val endDate : String? = "",
    val therapistCheck : String? = ""
)
