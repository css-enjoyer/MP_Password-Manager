package com.example.mobproj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PasswordAdapter(private val passwordList: List<Password>) : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {

    // represents a single item/view in recycler view
    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // retrieve textviews in password_layout
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descTextView: TextView = itemView.findViewById(R.id.descTextView)
    }
    // creates new viewholders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.password_layout, parent, false)
        return PasswordViewHolder(itemView)
    }
    // binds views with data
    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        // Bind password data to views in the ViewHolder
        val password = passwordList[position]
        holder.titleTextView.text = password.pwTitle
        holder.descTextView.text = password.pwDesc
    }

    override fun getItemCount(): Int {
        return passwordList.size
    }
}