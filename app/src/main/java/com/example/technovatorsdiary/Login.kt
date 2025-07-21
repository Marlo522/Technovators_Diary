package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.technovatorsdiary.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Check if user is already logged in
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        // For setting up the about button
        binding.tvAbout.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }
        // For setting up the login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()
            // Validate input if blank
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // email and password validation
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    db.collection("users").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Retrieve user info from Firestore
                                val firstName = document.getString("firstName") ?: ""
                                val lastName = document.getString("lastName") ?: ""
                                val emailDb = document.getString("email") ?: ""

                                val intent = Intent(this, Dashboard::class.java)
                                intent.putExtra("firstName", firstName)
                                intent.putExtra("lastName", lastName)
                                intent.putExtra("email", emailDb)
                                startActivity(intent)
                                finish()
                            } else {
                                // warning if no user info found
                                Toast.makeText(this, "No user info found.", Toast.LENGTH_SHORT).show()
                            }

                        }
                        // Handle failure to retrieve user info
                        .addOnFailureListener {
                            auth.signOut()
                            Toast.makeText(this, "Error checking user database.", Toast.LENGTH_LONG).show()
                        }
                }
                // Handle login failure
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                }
        }
    }
}