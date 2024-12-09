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
import com.example.uglydograter.models.DeleteRequest
import com.example.uglydograter.models.DeleteResponse
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

        // adapter init
        setupAdapter()

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        token = sharedPreferences.getString("TOKEN", null)

        // Fetch pets if token
        if (token != null) {
            fetchPets(token!!)
        }

        // Button to get back
        btnBackToMain.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
            finish()
        }
    }

    private fun setupAdapter() {
        // Adapter that uses a callback when deleting
        petsAdapter = PetsAdapter(petsList) { petToDelete ->
            deletePet(petToDelete)
        }
        petsRecyclerView.layoutManager = LinearLayoutManager(this)
        petsRecyclerView.adapter = petsAdapter
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

                        petsList.clear()
                        petsList.addAll(pets)
                        petsAdapter.notifyDataSetChanged()
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

    private fun deletePet(pet: Pet) {
        val deleteRequest = DeleteRequest(token = token!!, id = pet.id)

        RetrofitClient.apiService.deletePet(deleteRequest).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    // Remove pet from list and update RecyclerView
                    petsAdapter.removePet(pet)
                    Toast.makeText(this@ListPetsActivity, "Pet deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ListPetsActivity, "Failed to delete pet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Toast.makeText(this@ListPetsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}








