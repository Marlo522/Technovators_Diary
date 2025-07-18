package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.technovatorsdiary.databinding.ActivityDashboardBinding
import com.example.technovatorsdiary.adapter.JournalAdapter
import com.example.technovatorsdiary.model.JournalEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        binding.noteFAB.setOnClickListener {
            val intent = Intent(this, AddEntry::class.java)
            startActivity(intent)
        }

        val db = FirebaseFirestore.getInstance()
        val entriesList = mutableListOf<JournalEntry>()
        val adapter = JournalAdapter(entriesList)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter

        db.collection("entries")
            .whereEqualTo("uid", currentUid)
            .get()
            .addOnSuccessListener { result ->
                entriesList.clear()
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val date = document.getTimestamp("date")?.toDate() // ← Timestamp to Date
                    entriesList.add(JournalEntry(title, date))
                }

                binding.tvEmptyNotes.visibility =
                    if (entriesList.isEmpty()) View.VISIBLE else View.GONE
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                binding.tvEmptyNotes.text = "Failed to load entries."
                binding.tvEmptyNotes.visibility = View.VISIBLE
            }

    }
}
