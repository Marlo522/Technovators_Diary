package com.example.technovatorsdiary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.technovatorsdiary.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddEntry : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etEntry: EditText
    private lateinit var btnSave: Button

    private lateinit var entriesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        etTitle = findViewById(R.id.ettitle)
        etEntry = findViewById(R.id.etentry)
        btnSave = findViewById(R.id.btnsave)

        entriesRef = FirebaseDatabase.getInstance().getReference("entries")

        btnSave.setOnClickListener {
            saveEntry()
        }
    }

    private fun saveEntry() {
        val title = etTitle.text.toString().trim()
        val text = etEntry.text.toString().trim()

        if (title.isEmpty() || text.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return
        }

        val entry = mapOf(
            "title" to title,
            "text" to text
        )

        entriesRef.push().setValue(entry)
            .addOnSuccessListener {
                Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show()
                etTitle.text.clear()
                etEntry.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}

