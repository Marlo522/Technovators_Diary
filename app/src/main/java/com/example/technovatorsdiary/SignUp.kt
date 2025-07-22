package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.technovatorsdiary.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //for setting up the back button
        binding.btnback.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            // Navigate to the Login activity when the login button is clicked
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        // Set up the sign-up button click listener
        binding.btnSignUp.setOnClickListener {
            // Retrieve user input from EditText fields
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            var email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Check if the email is empty or does not contain "@" and append "@yahoo.com" if necessary
            if (!email.contains("@")) {
                email += "@yahoo.com"
            }
            // Validate user input
            if (firstName.isBlank() ||
                lastName.isBlank() ||
                email.isBlank() ||
                password.isBlank() ||
                confirmPassword.isBlank()
            ) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password does not match, please re-type your password.", Toast.LENGTH_LONG).show()
            } else {
                // If all validations pass, proceed to the ReviewInfo activity
                val bundle = Bundle()
                bundle.putString("firstname", firstName)
                bundle.putString("lastname", lastName)
                bundle.putString("email", email)
                bundle.putString("password", password)
                val intent = Intent(this, ReviewInfo::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}