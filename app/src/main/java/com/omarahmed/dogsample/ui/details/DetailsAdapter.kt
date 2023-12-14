package com.omarahmed.dogsample.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.ItemDogBinding
import com.omarahmed.dogsample.databinding.ItemDogDetailsBinding
import com.omarahmed.dogsample.model.Dog

class DetailsAdapter(
    private val details: List<Pair<String, String>>,
) : RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binds =
            ItemDogDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(details[position])
    }

    class DetailsViewHolder(private val binds: ItemDogDetailsBinding) :
        RecyclerView.ViewHolder(binds.root) {

        fun bind(pair: Pair<String, String>) {
            binds.tvLabel.text = pair.first
            binds.tvValue.text = pair.second
        }
    }

}