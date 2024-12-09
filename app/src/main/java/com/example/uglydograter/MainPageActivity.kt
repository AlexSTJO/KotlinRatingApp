package com.example.uglydograter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val btnListPets = findViewById<Button>(R.id.btnListPets)
        btnListPets.setOnClickListener {
            startActivity(Intent(this, ListPetsActivity::class.java))
        }

        val btnAddPet = findViewById<Button>(R.id.btnAddPet)
        btnAddPet.setOnClickListener {
            startActivity(Intent(this, AddPetActivity::class.java))
        }

        val btnLeaderboard = findViewById<Button>(R.id.btnLeaderboard)
        btnLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {

            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
