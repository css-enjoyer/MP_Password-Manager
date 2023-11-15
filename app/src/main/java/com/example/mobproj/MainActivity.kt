package com.example.mobproj

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var menuItems: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAdd = findViewById(R.id.fabAdd)
        menuItems = findViewById(R.id.menuItems)

        fabAdd.setOnClickListener {
            // Toggle visibility of menu items
            menuItems.visibility = if (menuItems.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    // Handle Folder Click
    fun onFolderClick(view: View) {
        Toast.makeText(this, "Folder selected", Toast.LENGTH_SHORT).show()
        // Add your logic for handling the folder click
    }

    // Handle Password Click
    fun onPasswordClick(view: View) {
        Toast.makeText(this, "Password selected", Toast.LENGTH_SHORT).show()
        // Add your logic for handling the password click
    }
}

