package com.omarahmed.dogsample.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.FragmentDetailsBinding
import com.omarahmed.dogsample.databinding.FragmentHomeBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.util.AdaptiveSpacingItemDecoration
import com.omarahmed.dogsample.util.dpToPx


class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    private var _binds: FragmentDetailsBinding? = null
    private val binds: FragmentDetailsBinding
        get() = _binds!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binds = FragmentDetailsBinding.inflate(inflater, container, false)
        return binds.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binds.btnBack.setOnClickListener { activity?.onBackPressed() }

        Glide.with(binds.imgDog)
            .load(args.dog.image)
            .placeholder(R.drawable.placeholder_dog)
            .into(binds.imgDog)

        val breed = args.dog.breeds.first()
        val details = buildList {
            add("Breed" to breed.name)
            add("Group" to breed.group)
            add("Life Span" to breed.lifeSpan)
            add("Temperament" to breed.temperament)
        }

        binds.rvDogDetails.layoutManager = LinearLayoutManager(context)
        binds.rvDogDetails.adapter = DetailsAdapter(details)
        binds.rvDogDetails.addItemDecoration(AdaptiveSpacingItemDecoration(18.dpToPx))
    }


    override fun onDestroyView() {
        _binds = null
        super.onDestroyView()
    }
}