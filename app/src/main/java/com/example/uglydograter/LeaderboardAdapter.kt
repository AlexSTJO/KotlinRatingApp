package com.example.uglydograter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uglydograter.R
import com.example.uglydograter.models.Pet

class LeaderboardAdapter(
    private val petsList: List<Pet>
) : RecyclerView.Adapter<LeaderboardAdapter.PetViewHolder>() {

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petImage: ImageView = itemView.findViewById(R.id.leaderboardPetImage)
        val petName: TextView = itemView.findViewById(R.id.leaderboardPetName)
        val petRating: TextView = itemView.findViewById(R.id.leaderboardPetRating)
        val petVotes: TextView = itemView.findViewById(R.id.leaderboardPetVotes)
        val petDescription: TextView = itemView.findViewById(R.id.leaderboardPetDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petsList[position]

        holder.petName.text = pet.name
        holder.petRating.text = "Rating: ${pet.rating}"
        holder.petVotes.text = "Votes: ${pet.votes}"
        holder.petDescription.text = "Birthdate: ${pet.birthdate}"

        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(pet.url)
            .into(holder.petImage)
    }

    override fun getItemCount(): Int = petsList.size
}
