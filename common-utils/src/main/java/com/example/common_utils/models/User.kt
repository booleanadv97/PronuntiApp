package com.example.common_utils.models

data class User(
    val userId: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val points: Int? = 0,
    val character: String? = null) {
}
