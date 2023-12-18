package com.omarahmed.dogsample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.FragmentHomeBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType
import com.omarahmed.dogsample.util.AdaptiveSpacingItemDecoration
import com.omarahmed.dogsample.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var listType: ListType = ListType.LIST

    private var _binds: FragmentHomeBinding? = null
    private val binds: FragmentHomeBinding
        get() = _binds!!

    private val viewModel: DogsViewModel by viewModels()

    private val dogsAdapter = DogsAdapter(listType) {
        navigateToDetails(it)
        Log.d("HomeFragment", "dog clicked $it")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binds = FragmentHomeBinding.inflate(inflater, container, false)
        return binds.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners
        binds.btnRetry.setOnClickListener { viewModel.getAll() }
        binds.btnChangeListType.setOnClickListener { onChangeListTypeClicked() }
        binds.swipeRefresh.setOnRefreshListener { viewModel.getAll(true) }

        // Setup Recyclerview
        binds.rvDogs.layoutManager = GridLayoutManager(context, 1)
        binds.rvDogs.addItemDecoration(AdaptiveSpacingItemDecoration(12.dpToPx))
        binds.rvDogs.adapter = dogsAdapter
        updateListView()

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is DogsViewModel.UiState.Loading -> {
                            binds.viewError.visibility = View.GONE
                            binds.swipeRefresh.isRefreshing = true
                        }

                        is DogsViewModel.UiState.Error -> {
                            binds.swipeRefresh.isRefreshing = false
                            binds.viewError.visibility = View.VISIBLE
                            binds.tvErrorMessage.text = uiState.exception.localizedMessage
                        }

                        is DogsViewModel.UiState.Success -> {
                            binds.viewError.visibility = View.GONE
                            binds.swipeRefresh.isRefreshing = false
                            dogsAdapter.updateData(uiState.dogs)
                        }
                    }
                }
            }
        }

    }


    /*** Private functions ***/

    private fun navigateToDetails(dog: Dog) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(dog)
        findNavController().navigate(action)
    }


    private fun onChangeListTypeClicked() {
        listType = when (listType) {
            ListType.LIST -> ListType.GIRD
            ListType.GIRD -> ListType.LIST
        }
        updateListView()
    }

    private fun updateListView() {
        when (listType) {
            ListType.LIST -> {
                binds.btnChangeListType.setImageResource(R.drawable.ic_grid_view)
                (binds.rvDogs.layoutManager as? GridLayoutManager)?.spanCount = 1
            }

            ListType.GIRD -> {
                binds.btnChangeListType.setImageResource(R.drawable.ic_list_view)
                (binds.rvDogs.layoutManager as? GridLayoutManager)?.spanCount = 2
            }
        }
        dogsAdapter.updateListType(listType)
    }

}