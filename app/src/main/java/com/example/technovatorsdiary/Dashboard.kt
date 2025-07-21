package com.example.technovatorsdiary

import android.content.Intent
import android.os.Bundle
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

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: JournalAdapter
    private val entriesList = mutableListOf<JournalEntry>()
    private var currentUid: String? = null

    companion object {
        private const val REQUEST_EDIT_ENTRY = 1001 // Request code for edit entry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply padding for system bars (status bar, nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if user is logged in
        currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        // Get user info from intent
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        db = FirebaseFirestore.getInstance()

        // Go to Profile screen
        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        // Go to AddEntry screen
        binding.noteFAB.setOnClickListener {
            val intent = Intent(this, AddEntry::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView and adapter
        adapter = JournalAdapter(entriesList) { entry ->
            val editIntent = Intent(this, EditEntry::class.java).apply {
                putExtra("title", entry.title)
                putExtra("text", entry.text)
                putExtra("date", entry.date?.time ?: -1L)
                putExtra("id", entry.id)
            }
            startActivityForResult(editIntent, REQUEST_EDIT_ENTRY)
        }

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter

        loadEntries() // Load all journal entries
    }

    // Load entries from Firestore
    private fun loadEntries() {
        val uid = currentUid ?: return

        db.collection("entries")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                entriesList.clear()
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val text = document.getString("text") ?: ""
                    val date = document.getTimestamp("date")?.toDate()
                    val id = document.id
                    entriesList.add(JournalEntry(title, date, text, id))
                }

                // Show empty message if no entries
                binding.tvEmptyNotes.visibility =
                    if (entriesList.isEmpty()) View.VISIBLE else View.GONE
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Show error message on failure
                binding.tvEmptyNotes.text = "Failed to load entries."
                binding.tvEmptyNotes.visibility = View.VISIBLE
            }
    }

    // Handle result from EditEntry
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_ENTRY && resultCode == RESULT_OK) {
            loadEntries() // Reload entries after editing
        }
    }
}
