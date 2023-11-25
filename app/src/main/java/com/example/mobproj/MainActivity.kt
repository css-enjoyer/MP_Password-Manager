package com.example.mobproj

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var menuItems: LinearLayout

    // For initial lockscreen
    private lateinit var pinDialog: Dialog
    private lateinit var pinInput: EditText
    private lateinit var confirmButton: Button
    private lateinit var instructionText: TextView
    private var pinAttempts = 0

    // For database
    private var dbHandler: DBHandler? = null

    // For recycler
    private lateinit var passwordRecyclerView: RecyclerView
    private lateinit var passwordAdapter: PasswordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize lockscreen
        initPinDialog()

        // Initialize add button
        fabAdd = findViewById(R.id.fabAdd)
        menuItems = findViewById(R.id.menuItems)
        fabAdd.setOnClickListener {
            menuItems.visibility = if (menuItems.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Initialize database
        dbHandler = DBHandler(this)

        // Initialize recycler and adapter
        passwordRecyclerView = findViewById(R.id.passwordRecyclerView)
        val passwordList = dbHandler?.getPassword() ?: emptyList()

        passwordAdapter = PasswordAdapter(passwordList,
                                          editClickListener = {position -> onPasswordEditClick(position) },
                                          deleteClickListener = {position -> onPasswordDeleteClick(position)})
        passwordRecyclerView.adapter = passwordAdapter
        passwordRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initPinDialog() {
        pinDialog = Dialog(this)
        pinDialog.setContentView(R.layout.lockscreen)

        // Initialize UI components from the dialog
        pinInput = pinDialog.findViewById(R.id.pinInput)
        instructionText = pinDialog.findViewById(R.id.instructionText)
        confirmButton = pinDialog.findViewById(R.id.confirmButton)

        // Set click listener for the confirm button
        confirmButton.setOnClickListener {
            val defaultPin = "0000"
            val enteredPin = pinInput.text.toString()

            if (enteredPin == defaultPin) {
                pinDialog.dismiss()
            } else {
                pinAttempts++
                pinInput.text.clear()
                instructionText.text = "Incorrect PIN. Attempts: $pinAttempts"
                // add logic for handling incorrect PIN attempts (e.g., show a message after a certain number of attempts)
                if (pinAttempts >= 3) {
                    Toast.makeText(this, "Three attempts reached.", Toast.LENGTH_SHORT).show()
                    pinAttempts = 0

                }
            }
            dbHandler?.getPassword()
        }
        pinDialog.show()
    }
    // Handle Folder Button Click
    fun onFolderClick(view: View) {
        // Inflate the Folder_Creation_Layout
        val folderCreationView = layoutInflater.inflate(R.layout.folder_creation_layout, null)
        // Initialize UI components
        val folderTitle: EditText = folderCreationView.findViewById(R.id.folderTitle)
        val folderDescription: EditText = folderCreationView.findViewById(R.id.folderDescription)
        val folderColor: EditText = folderCreationView.findViewById(R.id.folderColor)
        val btnSave: Button = folderCreationView.findViewById(R.id.btnSave)
        val btnCancel: Button = folderCreationView.findViewById(R.id.btnCancel)
        // Create AlertDialog
        val folderCreationDialog = AlertDialog.Builder(this)
            .setView(folderCreationView)
            .create()

        // Pass inputs to database on save
        btnSave.setOnClickListener {
            val title = folderTitle.text.toString()
            val description = folderDescription.text.toString()
            val color = folderColor.text.toString()
            folderCreationDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            folderCreationDialog.dismiss()
        }
        folderCreationDialog.show()
    }
    // Handle Password Button Click
    fun onPasswordClick(view: View) {
        // Inflate Password_Creation_Layout
        val passwordCreationView = layoutInflater.inflate(R.layout.password_creation_layout, null)
        // Initialize UI components
        val passwordTitle: EditText = passwordCreationView.findViewById(R.id.passwordTitle)
        val passwordInput: EditText = passwordCreationView.findViewById(R.id.passwordInput)
        val passwordDescription: EditText = passwordCreationView.findViewById(R.id.passwordDescription)
        val btnSave: Button = passwordCreationView.findViewById(R.id.btnSave)
        val btnCancel: Button = passwordCreationView.findViewById(R.id.btnCancel)

        // Create AlertDialog
        val passwordCreationDialog = AlertDialog.Builder(this)
            .setView(passwordCreationView)
            .create()

        // listener on password save
        btnSave.setOnClickListener {
            val pwTitle = passwordTitle.text.toString()
            val pwDesc = passwordDescription.text.toString()
            val password = passwordInput.text.toString()

            // validate text fields
            if (pwTitle.isEmpty() && password.isEmpty() && pwDesc.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter all required info.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // create Password object with the entered values
            val passwordObject = Password(0, pwTitle, pwDesc, password)
            val success = dbHandler?.addPassword(passwordObject)
            if (success != null && success) {
                passwordAdapter.notifyDataSetChanged()
                Log.d("DBHandler", "Inserted Password with ID: " + passwordObject.pwID)
                Toast.makeText(this@MainActivity, "Password has been added.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Failed to add password.", Toast.LENGTH_SHORT).show()
            }

            // clear fields
            passwordTitle.setText("")
            passwordDescription.setText("")
            passwordInput.setText("")

            passwordCreationDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            passwordCreationDialog.dismiss()
        }
        passwordCreationDialog.show()
    }
    fun onPasswordEditClick(position: Int) {
        // inflate password_creation_layout
        val passwordEditView = layoutInflater.inflate(R.layout.password_creation_layout, null)
        // initialize components
        val passwordTitle: EditText = passwordEditView.findViewById(R.id.passwordTitle)
        val passwordInput: EditText = passwordEditView.findViewById(R.id.passwordInput)
        val passwordDescription: EditText = passwordEditView.findViewById(R.id.passwordDescription)
        val btnSave: Button = passwordEditView.findViewById(R.id.btnSave)
        val btnCancel: Button = passwordEditView.findViewById(R.id.btnCancel)
        // create dialog
        val editPasswordDialog = AlertDialog.Builder(this)
            .setView(passwordEditView)
            .create()
        // listener on password update
        btnSave.setOnClickListener {
            // retrieve inputs
            val newPasswordTitle = passwordTitle.text.toString()
            val newPasswordDescription = passwordDescription.text.toString()
            val newPasswordInput = passwordInput.text.toString()
            // update the corresponding Password object in the database
            val selectedPassword = passwordAdapter.getItemAtPosition(position)
            val updatedPasswordObject = Password(selectedPassword.pwID, newPasswordTitle, newPasswordInput, newPasswordDescription)
            val updateSuccess = dbHandler?.updatePassword(updatedPasswordObject)

            if (updateSuccess != null && updateSuccess > 0) {
                // update adapter
                val updatedList = dbHandler?.getPassword() ?: emptyList()
                passwordAdapter.updateData(updatedList)
                // notify the adapter that data changed
                passwordAdapter.notifyDataSetChanged()
                editPasswordDialog.dismiss()
                Toast.makeText(this@MainActivity, "Password updated successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Failed to update password.", Toast.LENGTH_SHORT).show()
            }
            // clear fields
            passwordTitle.setText("")
            passwordDescription.setText("")
            passwordInput.setText("")
            editPasswordDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            editPasswordDialog.dismiss()
        }
        editPasswordDialog.show()
    }

    fun onPasswordDeleteClick(position: Int) {
        // Handle the "Edit" button click for the item at the given position
        // You can open an edit dialog or perform any other edit action here
    }
}

