package com.example.uglydograter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.ListRequest
import com.example.uglydograter.models.Pet
import com.example.uglydograter.models.PetsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPetsActivity : AppCompatActivity() {

    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var tvNoPets: TextView
    private lateinit var btnBackToMain: Button
    private lateinit var petsAdapter: PetsAdapter
    private var petsList = mutableListOf<Pet>() // Mutable list for pets
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pets)

        // Initialize UI components
        petsRecyclerView = findViewById(R.id.petsRecyclerView)
        tvNoPets = findViewById(R.id.tvNoPets)
        btnBackToMain = findViewById(R.id.btnBackToMain)

        // Initialize RecyclerView with adapter
        petsAdapter = PetsAdapter(petsList)
        petsRecyclerView.layoutManager = LinearLayoutManager(this)
        petsRecyclerView.adapter = petsAdapter

        // Get token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        token = sharedPreferences.getString("TOKEN", null)

        // Fetch pets if token exists
        if (token != null) {
            fetchPets(token!!)
        }

        // Back to main page
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchPets(token: String) {
        val request = ListRequest(token = token)
        RetrofitClient.apiService.listPets(request).enqueue(object : Callback<PetsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PetsResponse>, response: Response<PetsResponse>) {
                if (response.isSuccessful) {
                    val pets = response.body()?.pets ?: emptyList()
                    if (pets.isEmpty()) {
                        tvNoPets.visibility = View.VISIBLE
                        petsRecyclerView.visibility = View.GONE
                    } else {
                        tvNoPets.visibility = View.GONE
                        petsRecyclerView.visibility = View.VISIBLE

                        // Update the dataset
                        petsList.clear()
                        petsList.addAll(pets)
                        petsAdapter.notifyDataSetChanged()

                        // Force RecyclerView to refresh layout
                        petsRecyclerView.post {
                            petsRecyclerView.invalidate()
                            petsRecyclerView.requestLayout()
                        }
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
}








