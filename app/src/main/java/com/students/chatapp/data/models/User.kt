package com.students.chatapp.data.models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class User(
    @DocumentId
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var profilePicture: String = "",
    var status: String? = ""
) : Serializable