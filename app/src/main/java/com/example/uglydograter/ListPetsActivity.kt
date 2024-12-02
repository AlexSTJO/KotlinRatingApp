package com.example.uglydograter

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uglydograter.R
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.Pet
import com.example.uglydograter.models.PetsResponse
import com.example.uglydograter.PetsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPetsActivity : AppCompatActivity() {

    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var tvNoPets: TextView
    private lateinit var btnBackToMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pets)

        petsRecyclerView = findViewById(R.id.petsRecyclerView)
        tvNoPets = findViewById(R.id.tvNoPets)
        btnBackToMain = findViewById(R.id.btnBackToMain)

        petsRecyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        if (token != null) {
            fetchPets(token)
        }

        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchPets(token: String) {
        RetrofitClient.apiService.listPets(token).enqueue(object : Callback<PetsResponse> {
            override fun onResponse(call: Call<PetsResponse>, response: Response<PetsResponse>) {
                if (response.isSuccessful) {
                    val pets = response.body()?.pets ?: emptyList()
                    if (pets.isEmpty()) {
                        showNoPetsMessage()
                    } else {
                        setupRecyclerView(pets)
                    }
                } else {
                    Toast.makeText(this@ListPetsActivity, "Failed to fetch pets", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PetsResponse>, t: Throwable) {
                Toast.makeText(this@ListPetsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(pets: List<Pet>) {
        tvNoPets.visibility = View.GONE
        petsRecyclerView.visibility = View.VISIBLE
        petsRecyclerView.adapter = PetsAdapter(pets)
    }

    private fun showNoPetsMessage() {
        petsRecyclerView.visibility = View.GONE
        tvNoPets.visibility = View.VISIBLE
    }
}



