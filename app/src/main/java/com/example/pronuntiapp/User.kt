package com.example.pronuntiapp

class User constructor(private var email : String, private var fName : String, private var lName : String, private var password : String) {
    fun getEmail() : String = email
    fun getFName() : String = fName
    fun getLName() : String = lName
    fun setPass(newPass : String) { password = newPass }
}