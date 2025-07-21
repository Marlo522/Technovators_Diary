package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.technovatorsdiary.databinding.ActivityReviewInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReviewInfo : AppCompatActivity() {
    private lateinit var binding: ActivityReviewInfoBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReviewInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set up the back button to return to SignUp
        binding.btnback.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        // Retrieve user data from the intent
        val bundle = intent.extras
        val firstName = bundle?.getString("firstname") ?: ""
        val lastName = bundle?.getString("lastname") ?: ""
        val email = bundle?.getString("email") ?: ""
        val password = bundle?.getString("password") ?: ""
        // Set the user data to the TextViews
        binding.tvFirstName.text = firstName
        binding.tvLastName.text = lastName
        binding.tvEmail.text = email

        // Set up the Confirm button to create a new user
        binding.btnConfirm.setOnClickListener {
            if (email.isNotBlank() && password.isNotBlank()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        val userData = mapOf(
                            "uid" to uid,
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email
                        )
                        // Save user data to Firestore
                        db.collection("users").document(uid).set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error saving user data", Toast.LENGTH_LONG).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}