package com.example.technovatorsdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.technovatorsdiary.databinding.ItemJournalEntryBinding
import com.example.technovatorsdiary.model.JournalEntry
import java.text.SimpleDateFormat
import java.util.*

class JournalAdapter(
    private val entries: List<JournalEntry>,
    private val onClick: (JournalEntry) -> Unit
) : RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemJournalEntryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJournalEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.binding.tvTitle.text = entry.title

        // Format date if available
        val formattedDate = entry.date?.let {
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
        } ?: "No date"

        holder.binding.tvDate.text = formattedDate

        // Set click listener and pass the full JournalEntry (including id)
        holder.itemView.setOnClickListener {
            onClick(entry)
        }
    }
}
