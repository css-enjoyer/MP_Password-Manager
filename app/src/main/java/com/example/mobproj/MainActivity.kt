package com.example.mobproj

import DBHandler
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize lockscreen
        initPinDialog()
        showPinDialog()

        // Initialize add button
        fabAdd = findViewById(R.id.fabAdd)
        menuItems = findViewById(R.id.menuItems)
        fabAdd.setOnClickListener {
            menuItems.visibility = if (menuItems.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Initialize database
        dbHandler = DBHandler(this)
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
            // Check the entered PIN
            checkPin(pinInput.text.toString())
        }
    }
    private fun showPinDialog() {
        // Show the PIN dialog
        pinDialog.show()
    }
    private fun checkPin(enteredPin: String) {
        // Default PIN
        val defaultPin = "0000"

        // Check if the entered PIN is correct
        if (enteredPin == defaultPin) {
            pinDialog.dismiss()
        } else {
            // Incorrect PIN, increment attempts
            pinAttempts++
            // Clear PIN input
            pinInput.text.clear()
            // Update instruction text
            instructionText.text = "Incorrect PIN. Attempts: $pinAttempts"
            // Add logic for handling incorrect PIN attempts (e.g., show a message after a certain number of attempts)
            if (pinAttempts >= 3) {
                Toast.makeText(this, "Three attempts reached.", Toast.LENGTH_SHORT).show()
                pinAttempts = 0

            }
        }
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
            // call method to pass values to db
            dbHandler?.addPassword(pwTitle, pwDesc, password)

            // After adding the data, we are displaying a toast message.
            Toast.makeText(this@MainActivity, "Course has been added.", Toast.LENGTH_SHORT).show()
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
}

