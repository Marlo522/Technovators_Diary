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
        private const val REQUEST_EDIT_ENTRY = 1001
    }

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

        currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        db = FirebaseFirestore.getInstance()

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

        loadEntries()
    }

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

                binding.tvEmptyNotes.visibility =
                    if (entriesList.isEmpty()) View.VISIBLE else View.GONE
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                binding.tvEmptyNotes.text = "Failed to load entries."
                binding.tvEmptyNotes.visibility = View.VISIBLE
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_ENTRY && resultCode == RESULT_OK) {
            loadEntries() // 🔁 Refresh dashboard after edit
        }
    }
}
