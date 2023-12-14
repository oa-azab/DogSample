package com.omarahmed.dogsample.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.ItemDogBinding
import com.omarahmed.dogsample.model.Dog

class DogsAdapter(
    private val dogs: List<Dog>,
    private val onItemClick: (Dog) -> Unit
) : RecyclerView.Adapter<DogsAdapter.DogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binds = ItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return dogs.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(dogs[position], onItemClick)
    }

    class DogViewHolder(private val binds: ItemDogBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(dog: Dog, onItemClick: (Dog) -> Unit) {
            binds.tvBreedName.text = dog.breeds.firstOrNull()?.name.orEmpty()
            Glide.with(binds.imgDog)
                .load(dog.image)
                .placeholder(R.drawable.placeholder_dog)
                .into(binds.imgDog)

            binds.root.setOnClickListener { onItemClick(dog) }
        }
    }

}