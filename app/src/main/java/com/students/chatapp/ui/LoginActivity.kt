package com.students.chatapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.students.chatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.registerNavButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {

            // Get User Input
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            // Validate User Input
            if (email.isEmpty() || password.isEmpty()) {
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

            attemptLogin(email, password)

        }
    }

    private fun attemptLogin(email: String, password: String) {

        binding.loginProgressBar.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                binding.loginProgressBar.remove()
                sendHome()
            }
            .addOnFailureListener {
                binding.loginProgressBar.remove()
                longToast(it.localizedMessage ?: "Login failed, please try again")
            }
    }

    override fun onStart() {
        super.onStart()

        // Check Whether the user is already authenticated and act where necessary
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            sendHome()
        }
    }

    private fun sendHome() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also { startActivity(it) }
    }
}