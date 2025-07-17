package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.technovatorsdiary.databinding.ActivityReviewInfoBinding

class ReviewInfo : AppCompatActivity() {
    private lateinit var binding: ActivityReviewInfoBinding
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

        binding.btnback.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }


        val bundle = intent.extras
        if (bundle != null){
            binding.tvFirstName.text = bundle.getString("firstname")
            binding.tvMiddleName.text = bundle.getString("middlename")
            binding.tvLastName.text = bundle.getString("lastname")
            binding.tvEmail.text = bundle.getString("email")
        }

        bind

    }
}