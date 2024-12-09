package com.example.uglydograter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

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
            val intent = Intent(this, AddPetActivity::class.java)
            startActivity(intent)
        }
    }
}
