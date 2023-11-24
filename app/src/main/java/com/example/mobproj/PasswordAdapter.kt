package com.example.mobproj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PasswordAdapter(private var passwordList: List<Password>,
                      private val editClickListener: (Int) -> Unit,
                      private val deleteClickListener: (Int) -> Unit) : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {

    // represents a single item/view in recycler view
    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // retrieve ui in password_layout
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descTextView: TextView = itemView.findViewById(R.id.descTextView)
        val passwordTextView: TextView = itemView.findViewById(R.id.passwordTextView)
        val editPasswordButton: Button = itemView.findViewById(R.id.editPasswordBtn)
        val delPasswordButton: Button = itemView.findViewById(R.id.delPasswordBtn)
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
        holder.passwordTextView.text = password.password

        holder.editPasswordButton.setOnClickListener { editClickListener(position) }
        holder.delPasswordButton.setOnClickListener { deleteClickListener(position) }
    }
    fun getItemAtPosition(position: Int): Password {
        return passwordList[position]
    }
    fun updateData(newData: List<Password>) {
        passwordList = newData
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return passwordList.size
    }
}