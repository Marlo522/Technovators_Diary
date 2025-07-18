package com.example.technovatorsdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.technovatorsdiary.databinding.ItemJorunalEntryBinding
import com.example.technovatorsdiary.model.JournalEntry

class JournalAdapter(private val entries: List<JournalEntry>) : RecyclerView.Adapter<JournalAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(private val binding: ItemJorunalEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: JournalEntry) {
            binding.tvTitle.text = entry.title
            binding.tvDate.text = entry.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemJorunalEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size
}
