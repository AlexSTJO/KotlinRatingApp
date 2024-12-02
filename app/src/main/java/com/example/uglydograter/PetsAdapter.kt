package com.example.uglydograter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uglydograter.R
import com.example.uglydograter.models.Pet

class PetsAdapter(private val pets: List<Pet>) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.petNameTextView)
        val petBreed: TextView = itemView.findViewById(R.id.petBreedTextView)
        val petAge: TextView = itemView.findViewById(R.id.petAgeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.petName.text = pet.name
        holder.petBreed.text = pet.breed
        holder.petAge.text = "Age: ${pet.age}"
    }

    override fun getItemCount(): Int {
        return pets.size
    }
}

