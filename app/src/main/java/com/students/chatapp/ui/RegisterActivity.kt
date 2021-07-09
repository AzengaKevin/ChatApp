package com.students.chatapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.students.chatapp.data.Constants
import com.students.chatapp.data.models.User
import com.students.chatapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loginNavButton.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener {

            // Get User Input
            val name = binding.nameField.text.toString()
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            // Validate User Input
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                shortToast("All fields are required")
                return@setOnClickListener
            }

            if (password.length < 6) {
                shortToast("A minimum of 6 chars password required")
                binding.passwordField.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                shortToast("The email address given is invalid")
                binding.emailField.requestFocus()
                return@setOnClickListener
            }

            // Register the user
            attemptRegistration(name, email, password)
        }
    }

    private fun attemptRegistration(name: String, email: String, password: String) {
        binding.registerProgressBar.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Create a user
                val user = User(
                    name = name,
                    email = email
                )

                // Push the user to fire-store database
                firebaseFirestore.collection(Constants.USER_ROOT).document(it.user!!.uid)
                    .set(user)
                    .addOnSuccessListener {
                        binding.registerProgressBar.remove()
                        sendHome()
                    }
                    .addOnFailureListener {
                        longToast(
                            it.localizedMessage
                                ?: "Initial profile setup failed, update your profile later"
                        )
                        sendHome()
                    }
            }
            .addOnFailureListener {
                binding.registerProgressBar.remove()
                longToast(it.localizedMessage ?: "Login failed, please try again")
            }

    }


    private fun sendHome() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also { startActivity(it) }
    }
}