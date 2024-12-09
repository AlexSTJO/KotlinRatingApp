package com.example.uglydograter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uglydograter.LeaderboardAdapter
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.PetsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var btnBackToMainMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)


        // Initialize this gross view
        petsRecyclerView = findViewById(R.id.petsRecyclerView)
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu)
        petsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set an empty adapter, because of errors
        petsRecyclerView.adapter = LeaderboardAdapter(emptyList())

        fetchLeaderboardPets()

        btnBackToMainMenu.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back here
        }
    }

    private fun fetchLeaderboardPets() {

        RetrofitClient.apiService.getLeaderboard().enqueue(object : Callback<PetsResponse> {
            override fun onResponse(call: Call<PetsResponse>, response: Response<PetsResponse>) {
                println(response)
                if (response.isSuccessful) {
                    val pets = response.body()?.pets ?: emptyList()
                    if (pets.isEmpty()) {
                        petsRecyclerView.visibility = View.GONE
                    } else {
                        println("here")
                        petsRecyclerView.visibility = View.VISIBLE
                        petsRecyclerView.adapter = LeaderboardAdapter(pets)
                    }
                } else {
                    Toast.makeText(this@LeaderboardActivity, "Failed to load leaderboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PetsResponse>, t: Throwable) {
                Toast.makeText(this@LeaderboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LeaderboardActivity", "Error: ${t.message}")
            }
        })

    }
}
