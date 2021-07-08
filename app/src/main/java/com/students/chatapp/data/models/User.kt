package com.students.chatapp.data.models

import java.io.Serializable

data class User(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var profilePicture: String = ""
) : Serializable