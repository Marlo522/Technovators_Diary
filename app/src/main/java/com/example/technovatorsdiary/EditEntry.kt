package com.example.technovatorsdiary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.technovatorsdiary.databinding.ActivityEditEntryBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditEntry : AppCompatActivity() {

    private lateinit var binding: ActivityEditEntryBinding
    private lateinit var firestore: FirebaseFirestore
    private var entryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()

        // Retrieve data from intent
        val title = intent.getStringExtra("title") ?: ""
        val text = intent.getStringExtra("text") ?: ""
        val dateMillis = intent.getLongExtra("date", -1L)
        entryId = intent.getStringExtra("id")

        // Set existing values
        binding.ettitle.setText(title)
        binding.etentry.setText(text)

        if (dateMillis != -1L) {
            val formattedDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date(dateMillis))
            binding.tvdate.text = formattedDate
        } else {
            binding.tvdate.text = "No date"
        }

        binding.btnback.setOnClickListener {
            finish()
        }

        binding.btnsave.setOnClickListener {
            val updatedTitle = binding.ettitle.text.toString().trim()
            val updatedText = binding.etentry.text.toString().trim()

            if (updatedTitle.isNotEmpty() && updatedText.isNotEmpty()) {
                val updatedData = mapOf(
                    "title" to updatedTitle,
                    "text" to updatedText
                )

                if (!entryId.isNullOrEmpty()) {
                    firestore.collection("entries") // MATCHES Dashboard.kt
                        .document(entryId!!)
                        .update(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Entry updated successfully!", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK) // Signal Dashboard to refresh
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Entry ID is missing.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Title and Entry cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
