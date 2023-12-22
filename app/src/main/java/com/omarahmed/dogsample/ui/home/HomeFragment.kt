package com.omarahmed.dogsample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.FragmentHomeBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType
import com.omarahmed.dogsample.util.AdaptiveSpacingItemDecoration
import com.omarahmed.dogsample.util.dpToPx


class HomeFragment : Fragment(), HomeView {

    private var listType: ListType = ListType.LIST

    private var _binds: FragmentHomeBinding? = null
    private val binds: FragmentHomeBinding
        get() = _binds!!

    private lateinit var presenter: HomePresenter
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

        val getAllDogsUseCase =
            (activity?.application as App).appComponent.provideGetAllDogsUseCase()

        // Setup click listeners
        binds.btnRetry.setOnClickListener { presenter.getDogs() }
        binds.btnChangeListType.setOnClickListener { onChangeListTypeClicked() }
        binds.swipeRefresh.setOnRefreshListener { presenter.getDogs(true) }

        binds.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("onQueryTextSubmit", "query = $query")
                val filterQuery = query.orEmpty()
                if (filterQuery.isNotBlank()) {
                    presenter.getDogs(filterTerm = filterQuery)
                    return true
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isBlank()) {
                    presenter.getDogs()
                    return true
                }

                return false
            }

        })

        // Setup Recyclerview
        binds.rvDogs.layoutManager = GridLayoutManager(context, 1)
        binds.rvDogs.addItemDecoration(AdaptiveSpacingItemDecoration(12.dpToPx))
        binds.rvDogs.adapter = dogsAdapter
        updateListView()

        presenter = HomePresenter(this, getAllDogsUseCase)
        presenter.getDogs()
    }

    override fun onDestroyView() {
        _binds = null
        presenter.cancelScope()
        super.onDestroyView()
    }


    /*** HomeView ***/

    override fun showLoading() {
        binds.swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        binds.swipeRefresh.isRefreshing = false
    }

    override fun showResult(data: List<Dog>) {
        dogsAdapter.updateData(data)
    }

    override fun showError(message: String) {
        binds.viewError.visibility = View.VISIBLE
        binds.tvErrorMessage.text = message
    }

    override fun hideError() {
        binds.viewError.visibility = View.GONE
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