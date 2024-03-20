package com.example.pronuntiapp.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val userId: String? = null, val email: String? = null, val firstName: String? = null, val lastName: String? = null) {

}
