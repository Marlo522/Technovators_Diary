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
        binding.btnback.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("firstname", binding.etFirstName.text.toString())
            bundle.putString("middlename", binding.etMiddleName.text.toString())
            bundle.putString("lastname", binding.etLastName.text.toString())
            bundle.putString("email", binding.etEmail.text.toString())
            bundle.putString("password", binding.etPassword.text.toString())

            if (binding.etFirstName.text.isNullOrBlank() ||
                binding.etMiddleName.text.isNullOrBlank() ||
                binding.etLastName.text.isNullOrBlank() ||
                binding.etEmail.text.isNullOrBlank() ||
                binding.etPassword.text.isNullOrBlank() ||
                binding.etConfirmPassword.text.isNullOrBlank()
            ) {
                Toast.makeText(
                    this, "Please fill in all fields.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                if (binding.etPassword.text.toString() == binding.etConfirmPassword.text.toString()) {
                    val intent = Intent(this, ReviewInfo::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this, "Password does not match, please re-type your password.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}