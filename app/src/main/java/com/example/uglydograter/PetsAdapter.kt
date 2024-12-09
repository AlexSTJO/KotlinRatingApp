package com.example.uglydograter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uglydograter.models.Pet

class PetsAdapter(
    private val petsList: MutableList<Pet>,  // Made this mutable so my delete function can work
    private val onDeleteClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.petName)
        val petBirthdate: TextView = itemView.findViewById(R.id.petBirthdate)
        val petRating: TextView = itemView.findViewById(R.id.petRating)
        val petVotes: TextView = itemView.findViewById(R.id.petVotes)
        val petImage: ImageView = itemView.findViewById(R.id.petImage)
        val btnDeletePet: Button = itemView.findViewById(R.id.btnDeletePet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petsList[position]


        holder.petName.text = pet.name
        holder.petBirthdate.text = "Birthdate: ${pet.birthdate}"
        holder.petRating.text = "Rating: ${pet.rating}"
        holder.petVotes.text = "Votes: ${pet.votes}"

        Glide.with(holder.itemView.context)
            .load(pet.url)
            .into(holder.petImage)

        holder.btnDeletePet.setOnClickListener {
            onDeleteClick(pet) // Trigger callback to delete
        }
    }

    override fun getItemCount(): Int = petsList.size

    fun removePet(pet: Pet) {
        val position = petsList.indexOf(pet)
        if (position != -1) {
            petsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
