package com.students.chatapp.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.students.chatapp.R
import com.students.chatapp.data.Constants
import com.students.chatapp.data.models.User
import com.students.chatapp.databinding.ActivitySettingsBinding

private const val TAG = "SettingsActivity"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.updated_settings_text)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.updateSettingsButton.setOnClickListener { handleUpdateUser() }

    }

    /**
     * Handle the updating of users information
     */
    private fun handleUpdateUser() {

        val name = binding.nameField.text.toString()
        val phone = binding.phoneField.text.toString()
        val status = binding.statusField.text.toString()

        if (name.isEmpty()) {
            binding.nameField.error = "Name is required"
            binding.nameField.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            binding.phoneField.error = "Phone Number is required"
            binding.phoneField.requestFocus()
            return
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.phoneField.error = "A valid phone number is required"
            binding.phoneField.requestFocus()
            return

        }

        val user = User(
            name = name,
            phone = phone,
            status = status
        )

        binding.updateSettingProgressBar.show()

        firebaseFirestore.collection(Constants.USER_ROOT)
            .document(firebaseAuth.currentUser!!.uid)
            .set(user)
            .addOnCompleteListener {
                binding.updateSettingProgressBar.remove()
                shortToast("Your profile has been updated successfully")
                finish()
            }
            .addOnFailureListener {
                binding.updateSettingProgressBar.remove()
                longToast(it.localizedMessage ?: "A fatal error occurred, try again later")
            }


    }

    override fun onStart() {
        super.onStart()

        updateUI()
    }

    private fun updateUI() {

        firebaseFirestore.collection(Constants.USER_ROOT)
            .document(firebaseAuth.currentUser!!.uid)
            .addSnapshotListener(this) { value, error ->

                if (error != null) {
                    Log.e(TAG, "updateUI: failed getting user settings", error)
                    return@addSnapshotListener
                }

                value?.let { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)


                    user?.let { nonNullUser ->

                        binding.nameField.setText(nonNullUser.name)
                        binding.phoneField.setText(nonNullUser.phone)
                        binding.statusField.setText(nonNullUser.status)

                    }
                }

            }
    }

}