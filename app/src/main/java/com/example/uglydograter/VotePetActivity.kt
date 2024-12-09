package com.example.uglydograter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.GetVoteResponse
import com.example.uglydograter.models.SaveVoteRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VotePetActivity : AppCompatActivity() {

    private lateinit var petImage: ImageView
    private lateinit var petName: TextView
    private lateinit var petBirthdate: TextView
    private lateinit var ratingInput: EditText
    private lateinit var btnSubmitVote: Button
    private lateinit var btnBackToMainMenu: Button

    private var petId: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_pet)


        petImage = findViewById(R.id.votePetImage)
        petName = findViewById(R.id.votePetName)
        petBirthdate = findViewById(R.id.votePetBirthdate)
        ratingInput = findViewById(R.id.ratingInput)
        btnSubmitVote = findViewById(R.id.btnSubmitVote)
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu)


        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        token = sharedPreferences.getString("TOKEN", null)

        // Primes voting
        fetchRandomPet()


        btnSubmitVote.setOnClickListener {
            submitVote()
        }


        btnBackToMainMenu.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
            finish()
        }
    }

    private fun fetchRandomPet() {
        RetrofitClient.apiService.getVote().enqueue(object : Callback<GetVoteResponse> {
            override fun onResponse(call: Call<GetVoteResponse>, response: Response<GetVoteResponse>) {
                if (response.isSuccessful) {
                    val votePet = response.body()?.pet

                    if (votePet != null) {
                        petId = votePet.id
                        petName.text = "Name: ${votePet.name}"
                        petBirthdate.text = "Birthdate: ${votePet.birthdate}"
                        Glide.with(this@VotePetActivity).load(votePet.url).into(petImage)
                    } else {
                        Toast.makeText(this@VotePetActivity, "No pet data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@VotePetActivity, "Failed to fetch pet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetVoteResponse>, t: Throwable) {
                Toast.makeText(this@VotePetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitVote() {
        val rating = ratingInput.text.toString().toDoubleOrNull()

        if (rating == null || rating < 1 || rating > 5) {
            Toast.makeText(this, "Please enter a valid rating (1-5)", Toast.LENGTH_SHORT).show()
            return
        }

        if (token == null || petId == null) {
            Toast.makeText(this, "Missing token or pet ID", Toast.LENGTH_SHORT).show()
            return
        }

        val request = SaveVoteRequest(token = token!!, pet_id = petId!!, rating = rating)

        RetrofitClient.apiService.saveVote(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VotePetActivity, "Vote submitted successfully!", Toast.LENGTH_SHORT).show()
                    ratingInput.text.clear()
                    fetchRandomPet()
                } else {
                    Toast.makeText(this@VotePetActivity, "Failed to submit vote", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VotePetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

