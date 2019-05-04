package com.acostadev.acostademo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acostadev.acostademo.databinding.RowFirebaseDatabaseBinding
import com.acostadev.acostademo.models.TextProfile

class FirebaseDatabaseAdapter(val profiles: List<TextProfile>) : RecyclerView.Adapter<FirebaseDatabaseAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(RowFirebaseDatabaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = profiles.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    class ViewHolder(val binding: RowFirebaseDatabaseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(textProfile: TextProfile) {
            binding.profile = textProfile
        }
    }
}