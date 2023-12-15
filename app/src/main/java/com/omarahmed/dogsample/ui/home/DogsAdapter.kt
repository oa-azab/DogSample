package com.omarahmed.dogsample.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.ItemDogBinding
import com.omarahmed.dogsample.databinding.ItemDogGridBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType

class DogsAdapter(
    private var listType: ListType,
    private val onItemClick: (Dog) -> Unit
) : RecyclerView.Adapter<DogsAdapter.BaseDogViewHolder>() {

    private var dogs: List<Dog> = emptyList()

    fun updateData(newData: List<Dog>) {
        dogs = newData
        notifyDataSetChanged()
    }

    fun updateListType(newListType: ListType) {
        listType = newListType
        notifyItemRangeChanged(0, dogs.size)
    }

    override fun getItemViewType(position: Int): Int {
        return when (listType) {
            ListType.LIST -> ListType.LIST.ordinal
            ListType.GIRD -> ListType.GIRD.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDogViewHolder {
        when (viewType) {
            ListType.LIST.ordinal -> {
                val binds =
                    ItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DogViewHolderList(binds)
            }

            ListType.GIRD.ordinal -> {
                val binds =
                    ItemDogGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DogViewHolderGrid(binds)
            }

            else -> throw Exception("viewType $viewType not one of ListType")
        }

    }

    override fun getItemCount(): Int {
        return dogs.size
    }

    override fun onBindViewHolder(holder: BaseDogViewHolder, position: Int) {
        holder.bind(dogs[position], onItemClick)
    }

    abstract class BaseDogViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        abstract fun bind(dog: Dog, onItemClick: (Dog) -> Unit)

    }

    class DogViewHolderList(private val binds: ItemDogBinding) : BaseDogViewHolder(binds.root) {

        override fun bind(dog: Dog, onItemClick: (Dog) -> Unit) {
            binds.tvBreedName.text = dog.breeds.firstOrNull()?.name.orEmpty()
            Glide.with(binds.imgDog)
                .load(dog.image)
                .placeholder(R.drawable.placeholder_dog)
                .into(binds.imgDog)

            binds.root.setOnClickListener { onItemClick(dog) }
        }
    }

    class DogViewHolderGrid(private val binds: ItemDogGridBinding) : BaseDogViewHolder(binds.root) {

        override fun bind(dog: Dog, onItemClick: (Dog) -> Unit) {
            Glide.with(binds.imgDog)
                .load(dog.image)
                .placeholder(R.drawable.placeholder_dog)
                .into(binds.imgDog)

            binds.root.setOnClickListener { onItemClick(dog) }
        }
    }

}