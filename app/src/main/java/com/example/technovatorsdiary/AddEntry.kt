package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.technovatorsdiary.databinding.ActivityAddEntryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddEntry : AppCompatActivity() {

    private lateinit var binding: ActivityAddEntryBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.btnsave.setOnClickListener {
            saveEntry()
        }

        binding.btnback.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun saveEntry() {
        val title = binding.ettitle.text.toString().trim()
        val text = binding.etentry.text.toString().trim()
        val uid = auth.currentUser?.uid

        if (title.isEmpty() || text.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show()
            return
        }

        val entry = hashMapOf(
            "title" to title,
            "text" to text,
            "date" to Date(),
            "uid" to uid
        )

        db.collection("entries")
            .add(entry)
            .addOnSuccessListener {
                Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}