package com.example.common_utils.models

data class AssignedExercise(
    val assignId : String? = "",
    val userId : String? = "",
    val exerciseType : String? = "",
    val exerciseName : String? = "",
    val startDate : Long? = 0,
    val endDate : Long? = 0,
    val therapistCheck : String? = "",
    val rewardType : String? = "",
    val reward : String? = ""
)
