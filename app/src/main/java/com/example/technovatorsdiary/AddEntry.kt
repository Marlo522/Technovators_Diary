package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddEntry : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etEntry: EditText
    private lateinit var btnSave: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        etTitle = findViewById(R.id.ettitle)
        etEntry = findViewById(R.id.etentry)
        btnSave = findViewById(R.id.btnsave)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        btnSave.setOnClickListener {
            saveEntry()
        }

    }


    private fun saveEntry() {
        val title = etTitle.text.toString().trim()
        val text = etEntry.text.toString().trim()
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

                // Redirect to DashboardActivity
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
